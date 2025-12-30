package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class MessageAction : Action("^(?i)(message|msg|tell):( )?".toRegex()) {
    override fun getSuggestion() = "message: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val match = mm.deserialize(parsePlaceholders(player, match))
        if (player is Player) player.sendMessage(match)
        else plugin.server.consoleSender.sendMessage(match)
    }
}
