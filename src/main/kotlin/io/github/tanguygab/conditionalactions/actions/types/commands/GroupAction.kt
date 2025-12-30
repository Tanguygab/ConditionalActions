package io.github.tanguygab.conditionalactions.actions.types.commands

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class GroupAction : Action("^(?i)group:( )?".toRegex()) {
    override fun getSuggestion() = "group: <action group>"

    override fun execute(player: OfflinePlayer?, match: String) {
        var match = parsePlaceholders(player, match)
        plugin.actionManager.findAndExecute(player, match, true)
    }
}
