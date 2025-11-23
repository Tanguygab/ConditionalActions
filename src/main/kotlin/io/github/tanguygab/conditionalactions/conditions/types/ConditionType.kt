package io.github.tanguygab.conditionalactions.conditions.types

import io.github.tanguygab.conditionalactions.ConditionalActions
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer

abstract class ConditionType(input: List<String>) {
    val inverted: Boolean = input[0].startsWith("!")
    protected val leftSide = if (inverted) input[0].substring(1) else input[0]
    protected val rightSide = if (input.size > 1) input[1] else ""

    protected fun parse(player: OfflinePlayer?, string: String, replacements: Map<String, String>): String {
        return PlaceholderAPI.setPlaceholders(player, ConditionalActions.parseReplacements(string, replacements))
    }

    protected fun parseLeft(player: OfflinePlayer?, replacements: Map<String, String>) = parse(player, leftSide, replacements)

    protected fun parseRight(player: OfflinePlayer?, replacements: Map<String, String>) = parse(player, rightSide, replacements)

    abstract fun isMet(player: OfflinePlayer?, replacements: Map<String, String>): Boolean
}
