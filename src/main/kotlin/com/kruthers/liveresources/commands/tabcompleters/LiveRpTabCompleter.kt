package com.kruthers.liveresources.commands.tabcompleters

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class LiveRpTabCompleter: TabCompleter {
    override fun onTabComplete(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): MutableList<String>? {
        var options: MutableList<String> = mutableListOf()

        when (args.size) {
            // /live_rp update <link> [hash] [reload]
            0,1 -> {
                options.add("version")
                if (sender.hasPermission("live_rp.reload")) {
                    options.add("reloadconfig")
                }
                if (sender.hasPermission("live_rp.update")) {
                    options.add("update")
                }

                if (args.size == 1) {
                    options = options.filter { it.contains(args[0].lowercase()) }.toMutableList()
                }
            }
            4 -> {
                if (args[0].lowercase() == "update" && sender.hasPermission("live_rp.update")) {
                    options.add("true")
                    options.add("false")

                    options = options.filter { it.contains(args[2].lowercase()) }.toMutableList()
                }
            }
        }

        return options
    }
}