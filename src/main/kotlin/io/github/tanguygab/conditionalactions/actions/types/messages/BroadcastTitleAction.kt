package io.github.tanguygab.conditionalactions.actions.types.messages

import io.github.tanguygab.conditionalactions.actions.Action
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.OfflinePlayer

class BroadcastTitleAction : Action("^(?i)(broadcast|bc)-title:( )?".toRegex()) {
    override fun getSuggestion() = getSuggestionWithArgs("broadcast-title: <title>", "<subtitle>", "<fadein>", "<stay>", "<fadeout>")

    override fun execute(player: OfflinePlayer?, match: String) {
        val args = split(parsePlaceholders(player, match))

        val title = mm.deserialize(args[0])
        val subtitle = if (args.size > 1) mm.deserialize(args[1]) else Component.empty()

        if (args.size < 3) {
            plugin.server.showTitle(Title.title(title, subtitle))
            return
        }

        val fadein = args[2].toIntOrNull() ?: 10
        val stay = args[3].toIntOrNull() ?: 70
        val fadeout = args[4].toIntOrNull() ?: 20
        plugin.server.showTitle(Title.title(title, subtitle, fadein, stay, fadeout))
    }
}
