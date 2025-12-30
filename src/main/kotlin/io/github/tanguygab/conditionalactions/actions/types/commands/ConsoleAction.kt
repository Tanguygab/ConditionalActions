package io.github.tanguygab.conditionalactions.actions.types.commands

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class ConsoleAction : Action("^(?i)console:( )?".toRegex()) {
    override fun getSuggestion() = "console: <command>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val command = parsePlaceholders(player, match)
        plugin.sync(null) { plugin.server.dispatchCommand(plugin.server.consoleSender, command) }
    }
}
