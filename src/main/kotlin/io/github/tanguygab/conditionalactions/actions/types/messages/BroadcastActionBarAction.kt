package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class BroadcastActionBarAction : Action("^(?i)((broadcast|bc)-actionbar):( )?".toRegex()) {
    override fun getSuggestion() = "broadcast-actionbar: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val match = parsePlaceholders(player, match)
        plugin.server.sendActionBar(mm.deserialize(match))
    }
}
