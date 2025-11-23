package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class MessageAction : Action("^(?i)(message|msg|tell):( )?") {
    override fun getSuggestion() = "message: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val match = parsePlaceholders(player, match, true)
        if (player is Player) player.sendMessage(match)
        else plugin.server.consoleSender.sendMessage(match)
    }
}
