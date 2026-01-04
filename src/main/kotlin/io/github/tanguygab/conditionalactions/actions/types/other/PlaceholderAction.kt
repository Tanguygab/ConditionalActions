package io.github.tanguygab.conditionalactions.actions.types.other

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class PlaceholderAction : Action("^(?i)(placeholder|papi|parse):( )?".toRegex()) {
    override fun getSuggestion() = "placeholder: <command>"

    override fun execute(player: OfflinePlayer?, match: String) {
        parsePlaceholders(player, match)
    }
}