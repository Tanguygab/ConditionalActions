package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class MiniMessageAction : Action("^(?i)(minimessage|mm):( )?") {
    private val mm = MiniMessage.miniMessage()

    override fun getSuggestion() = "minimessage: <formatted message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val match = parsePlaceholders(player, match)
        val component = mm.deserialize(match)

        if (player is Player) player.sendMessage(component)
        else plugin.server.consoleSender.sendMessage(component)
    }
}
