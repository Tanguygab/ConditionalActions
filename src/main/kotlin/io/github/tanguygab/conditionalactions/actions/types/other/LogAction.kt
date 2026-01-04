package io.github.tanguygab.conditionalactions.actions.types.other

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import java.util.logging.Level

class LogAction : Action("^(?i)log:((?<level>[a-zA-Z]+):)?( )?".toRegex(), false) {
    override fun getSuggestion() = "log: <message>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val matcher = pattern.find(match)
        val level = matcher?.groups["level"]?.value?.uppercase() ?: "INFO"
        val message = parsePlaceholders(player, match.replace(pattern, ""))

        val lvl = try { Level.parse(level) } catch (_: Exception) { Level.INFO }
        plugin.logger.log(lvl, message)
    }
}
