package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class ActionBarAction : Action("^(?i)(actionbar):( )?") {
    override fun getSuggestion() = "actionbar: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return
        val match = parsePlaceholders(player, match)
        player.sendActionBar(mm.deserialize(match))
    }
}
