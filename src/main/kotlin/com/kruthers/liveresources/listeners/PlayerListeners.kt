package com.kruthers.liveresources.listeners

import com.kruthers.liveresources.LiveResources
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerListeners(val plugin: LiveResources): Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player;

        plugin.reloadPlayerRP(player, plugin)
    }

}