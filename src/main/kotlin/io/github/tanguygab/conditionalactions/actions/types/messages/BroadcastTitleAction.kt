package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class BroadcastTitleAction : Action("^(?i)(broadcast|bc)-title:( )?") {
    override val suggestion = getSuggestionWithArgs("broadcast-title: <title>", "<subtitle>", "<fadein>", "<stay>", "<fadeout>")

    override fun execute(player: OfflinePlayer?, match: String) {
        var match = match
        match = parsePlaceholders(player, match, true)
        val matches = split(match)

        val title = matches[0]
        val subtitle = if (matches.size > 1) matches[1] else ""
        val fadein = parseInt(matches, 2)
        val stay = parseInt(matches, 3)
        val fadeout = parseInt(matches, 4)

        @Suppress("DEPRECATION")
        for (p in plugin.server.onlinePlayers) p.sendTitle(title, subtitle, fadein, stay, fadeout)
    }

    private fun parseInt(args: List<String>, i: Int) = if (args.size > i) parseInt(args[i], 5) else 5
}
