package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class ActionBarAction : Action("^(?i)(actionbar):( )?") {
    override val suggestion = "actionbar: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return
        val match = parsePlaceholders(player, match, true)
        @Suppress("DEPRECATION")
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(match))
    }
}
