package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.OfflinePlayer

class BroadcastAction : Action("^(?i)(broadcast|bc):( )?".toRegex()) {
    override fun getSuggestion() = "broadcast: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val message = parsePlaceholders(player, match)
        plugin.server.sendMessage(mm.deserialize(message))
    }
}
