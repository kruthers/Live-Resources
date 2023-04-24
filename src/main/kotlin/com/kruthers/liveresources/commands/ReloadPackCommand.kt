package com.kruthers.liveresources.commands

import com.kruthers.liveresources.LiveResources
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadPackCommand(val plugin: LiveResources): CommandExecutor {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        //sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.config.getString("message.MESSAGE") ?: "DEFAULT"))
        when (args.size) {
            0 -> {
                if (sender is Player) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.config.getString("message.reload_sef") ?: "&2Your resourcepack is being reloaded from the server"))
                    plugin.reloadPlayerRP(sender,plugin)
                } else {
                    sender.sendMessage("${ChatColor.RED}You must be a player to run this command, use /reloadpack <player>")
                }
            }
            1 -> {
                if (sender.hasPermission("live_rp.apply.others")) {
                    val username: String = args[0]
                    val player: Player? = Bukkit.getPlayer(username)
                    if (player == null) {
                        sender.sendMessage("${ChatColor.RED}Failed to find player $username")
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.config.getString("message.reload_other") ?: "&2Force reloading {name}'s resourcepack").replace("{name}",player.name))
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.config.getString("message.reloaded_other") ?: "&2Your Resourcepack is being reloaded by {name}").replace("{name}",sender.name))
                        plugin.reloadPlayerRP(player,plugin)
                    }
                } else {
                    sender.sendMessage("${ChatColor.RED}You do not have permission to reload other people's resourepack")
                }
            }
            else -> sender.sendMessage("${ChatColor.RED}Too many arguments provided, correct usage: /reloadpack [player]")
        }

        return true
    }
}
