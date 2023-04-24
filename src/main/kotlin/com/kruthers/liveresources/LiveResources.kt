package com.kruthers.liveresources

import com.kruthers.liveresources.commands.LiveRpCommand
import com.kruthers.liveresources.commands.ReloadPackCommand
import com.kruthers.liveresources.commands.tabcompleters.LiveRpTabCompleter
import com.kruthers.liveresources.commands.tabcompleters.ReloadPackTabCompleter
import com.kruthers.liveresources.listeners.PlayerListeners
import com.kruthers.liveresources.utils.URLUtils
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class LiveResources : JavaPlugin() {

    companion object {
        val properties: Properties = Properties();
    }
    private var link: String = ""
    private var hash: String = ""

    override fun onEnable() {
        logger.info("${ChatColor.BLUE}Loading Live Resources by kruthers")
        //load properties
        properties.load(this.classLoader.getResourceAsStream(".properties"))
        logger.info("Loading config")
        //make sure the config has been initiated & save it
        config.options().copyDefaults(true)
        this.saveConfig()
        this.loadSavedRP()

        logger.info("Loaded config, registering commands/ events")
        this.server.pluginManager.registerEvents(PlayerListeners(this),this)
        this.server.getPluginCommand("live_rp")?.setExecutor(LiveRpCommand(this))
        this.server.getPluginCommand("live_rp")?.tabCompleter = LiveRpTabCompleter()
        this.server.getPluginCommand("reloadpack")?.setExecutor(ReloadPackCommand(this))
        this.server.getPluginCommand("reloadpack")?.tabCompleter = ReloadPackTabCompleter()


    }

    fun getRPLink(): String {
        return this.link
    }

    fun getRPHash(): String {
        return this.hash
    }

    fun loadSavedRP(): Boolean {
        this.reloadConfig()
        this.hash = this.config.getString("resourepack.hash")?: ""

        this.config.getString("resourepack.link").let {
            if (it == null || it == "") {
                this.logger.warning("No resourcepack defined in config, please specify one.")
                this.link = ""
                return false
            } else {
                if (URLUtils.isValid(it)) {
                    this.link = it
                } else {
                    this.logger.warning("Invalid resourcepack defined in config, please specify one.")
                    this.link = ""
                    return false
                }
            }
        }
        return true;
    }

    fun updateSavedRP(link: String, hash: String): Boolean{
        return if (URLUtils.isValid(link)) {
            this.link = link
            this.hash = hash
            this.config.set("resourepack.link", link)
            this.config.set("resourepack.hash", hash)
            this.saveConfig()
            true
        } else {
            false
        }
    }

    fun reloadPlayerRP(player: Player, plugin: LiveResources) {
        if (this.link != "") {
            if (this.hash == "") {
                player.setResourcePack(this.link)
            } else {
                player.setResourcePack(this.link, this.hash.toByteArray())
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.config.getString("message.failed_no_rp") ?: "&4No resourepack provided to load in, please contact your system administrator"))
        }
    }


}
