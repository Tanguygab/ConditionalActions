package io.github.tanguygab.conditionalactions.actions.types.other

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class DelayAction : Action("^(?i)delay:( )?".toRegex()) {
    override fun getSuggestion() = "delay: <ms>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val match = parsePlaceholders(player, match)
        Thread.sleep(parseInt(match, 1000).toLong())
    }
}
