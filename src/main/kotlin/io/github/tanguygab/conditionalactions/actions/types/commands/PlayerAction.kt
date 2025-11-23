package io.github.tanguygab.conditionalactions.actions.types.commands

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class PlayerAction : Action("^(?i)(player|cmd|command):( )?") {
    override fun getSuggestion() = "player: <command>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val command = parsePlaceholders(player, match)

        if (player is Player) {
            plugin.sync(player) { player.performCommand(command) }
            return
        }
        plugin.sync(null) { plugin.server.dispatchCommand(plugin.server.consoleSender, command) }
    }
}
