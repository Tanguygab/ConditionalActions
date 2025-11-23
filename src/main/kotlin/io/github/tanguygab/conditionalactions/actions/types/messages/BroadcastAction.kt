package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class BroadcastAction : Action("^(?i)(broadcast|bc):( )?") {
    override val suggestion = "broadcast: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val message = parsePlaceholders(player, match, true)
        plugin.server.onlinePlayers.forEach { it.sendMessage(message) }
    }
}
