package io.github.tanguygab.conditionalactions.actions.types.bungee

import com.google.common.io.ByteStreams
import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class ServerAction : Action("^(?i)server:( )?") {
    override fun getSuggestion() = "server: <server>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return
        ByteStreams.newDataOutput().apply {
            writeUTF("Connect")
            writeUTF(match)
            player.sendPluginMessage(plugin, "BungeeCord", toByteArray())
        }
    }
}
