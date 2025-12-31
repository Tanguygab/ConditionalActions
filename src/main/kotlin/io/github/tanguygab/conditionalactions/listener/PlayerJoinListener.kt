package io.github.tanguygab.conditionalactions.listener

import com.google.common.io.ByteStreams
import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.ConditionalActions.Companion.CHANNEL
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(val plugin: ConditionalActions) : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (plugin.server.onlinePlayers.size == 1)
            plugin.server.sendPluginMessage(plugin, CHANNEL, ByteStreams.newDataOutput().apply { writeUTF("start") }.toByteArray())
    }

}