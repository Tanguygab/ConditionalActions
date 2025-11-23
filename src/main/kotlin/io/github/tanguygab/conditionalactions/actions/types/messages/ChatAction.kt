package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class ChatAction : Action("^(?i)chat:( )?") {
    override val suggestion = "chat: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return
        val message = parsePlaceholders(player, match)
        plugin.sync(player) { player.chat(message) }
    }
}
