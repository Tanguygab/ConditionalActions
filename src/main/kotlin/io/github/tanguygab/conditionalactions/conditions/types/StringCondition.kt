package io.github.tanguygab.conditionalactions.conditions.types

import org.bukkit.OfflinePlayer

class StringCondition(input: List<String>, private val function: (String, String) -> Boolean) : ConditionType(input) {
    override fun isMet(player: OfflinePlayer?, replacements: Map<String, String>) = function(parseLeft(player, replacements), parseRight(player, replacements))
}
