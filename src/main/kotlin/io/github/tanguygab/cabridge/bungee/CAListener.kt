package io.github.tanguygab.cabridge.bungee

import com.google.common.io.ByteStreams
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class CAListener(val plugin: CABridgeBungee) : Listener {

    @EventHandler
    fun onMessageReceived(e: PluginMessageEvent) {
        if (e.tag != CABridgeBungee.CHANNEL) return
        e.isCancelled = true
        if (e.receiver !is ProxiedPlayer) return

        val `in` = ByteStreams.newDataInput(e.data)
        when (`in`.readUTF()) {
            "start" -> plugin.sendUpdate(plugin.onlineServers)
            "command" -> {
                val playerName = `in`.readUTF()
                val command = `in`.readUTF()

                val player = if (playerName.isEmpty()) plugin.proxy.console else plugin.proxy.getPlayer(playerName)
                if (player == null) {
                    plugin.logger.warning("Tried to run command \"$command\" as invalid player \"$playerName\"")
                    return
                }
                plugin.proxy.pluginManager.dispatchCommand(player, command)
            }
        }
    }
}