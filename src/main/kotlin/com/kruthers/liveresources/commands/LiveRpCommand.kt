package com.kruthers.liveresources.commands

import com.kruthers.liveresources.LiveResources
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class LiveRpCommand(val plugin: LiveResources): CommandExecutor {

    private fun reloadEveryone(plugin: LiveResources) {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.config.getString("message.reloaded_auto") ?: "&2Your resourcepack has been automatically reloaded"))
            plugin.reloadPlayerRP(player,plugin)
        }
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (args == null) {
            sender.sendMessage("You are running Live Resourcepacks version ${LiveResources.properties.getProperty("version")}")
        } else {
            if (args.isEmpty()) {
                sender.sendMessage("You are running Live Resourcepacks version ${LiveResources.properties.getProperty("version")}")
            } else {
                when (args[0].lowercase()) {
                    "reloadconfig" -> {
                        if (sender.hasPermission("live_rp.reload")) {
                            if (args.size > 1) {
                                sender.sendMessage("${ChatColor.RED}Invalid agument given at: /${label} reloadconfig <---- Expected usage: /live_rp reloadconfig")
                            } else {
                                sender.sendMessage("${ChatColor.GRAY}Reloading config")
                                if (!plugin.loadSavedRP()) {
                                    sender.sendMessage("${ChatColor.RED}Invalid resourcepack link defined in config, please update.")
                                }
                                sender.sendMessage("${ChatColor.GRAY}Reloaded config.")
                            }
                        } else {
                            sender.sendMessage(cmd.permissionMessage)
                        }
                    }
                    "update" -> {
                        if (sender.hasPermission("live_rp.update")) {
                            when (args.size-1) {
                                0 -> sender.sendMessage("${ChatColor.RED}Expected argument at: /${label} update <--- Correct usage: /live_rp update <link> [hash] [reload]")
                                1,2,3 -> {
                                    val link: String = args[1]
                                    val hash: String = if (args.size == 3) args[2] else ""
                                    val reloadEveryone = if (args.size == 4) {
                                        when(args[3]) {
                                            "true" -> true
                                            "false" -> false
                                            else -> {
                                                sender.sendMessage("${ChatColor.RED}Invalid argument at: /${label} update ${args[1]} ${args[2]} <--- Expected \"true\" or \"false\". Proceeding as if it was true")
                                                true
                                            }
                                        }
                                    } else {
                                        true
                                    }

                                    if (plugin.updateSavedRP(link,hash)) {
                                        sender.sendMessage("${ChatColor.GREEN}Resourcepack link updated, reloading all user resourcepacks")
                                        if (reloadEveryone) {
                                            this.reloadEveryone(plugin)
                                        }
                                    } else {
                                        sender.sendMessage("${ChatColor.RED}Resourcepack link invalid, failed to update")
                                    }
                                }
                                else -> {
                                    sender.sendMessage("${ChatColor.RED}Unexpected argument at: /${label} update ${args[1]} ${args[2]} ${args[3]} <--- Correct usage: /live_rp update <link> [hash] [reload]")
                                }
                            }
                        } else {
                            sender.sendMessage(cmd.permissionMessage)
                        }
                    }
                    else -> {
                        sender.sendMessage("${ChatColor.RED}Invalid usage at: /${label} <--- expected argument, correct usage: ${cmd.usage}")
                    }
                }
            }
        }

        return true
    }
}