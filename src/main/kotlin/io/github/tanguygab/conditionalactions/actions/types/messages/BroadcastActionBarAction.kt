package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent

class BroadcastActionBarAction : Action("^(?i)((broadcast|bc)-actionbar):( )?") {
    override val suggestion = "broadcast-actionbar: <message>"

    @Suppress("DEPRECATION")
    override fun execute(player: OfflinePlayer?, match: String) {
        val match = parsePlaceholders(player, match, true)
        val components = TextComponent.fromLegacyText(match)
        plugin.server.onlinePlayers.forEach{ it.spigot().sendMessage(ChatMessageType.ACTION_BAR, *components) }
    }
}
