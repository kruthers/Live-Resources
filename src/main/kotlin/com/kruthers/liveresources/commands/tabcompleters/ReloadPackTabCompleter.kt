package com.kruthers.liveresources.commands.tabcompleters

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class ReloadPackTabCompleter: TabCompleter {
    override fun onTabComplete(sender: CommandSender, cmd: Command, p2: String, args: Array<out String>): MutableList<String>? {
        var options: MutableList<String> = mutableListOf()
        if (sender.hasPermission("live_rp.apply.other")) {
            Bukkit.getOnlinePlayers().forEach {
                options.add(it.name)
            }

            if (args.size == 1) {
                options = options.filter { it.lowercase().contains(args[0].lowercase()) }.toMutableList()
            }
        }

        return options;
    }
}