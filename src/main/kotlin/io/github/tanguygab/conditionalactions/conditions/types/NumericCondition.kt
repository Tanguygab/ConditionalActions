package io.github.tanguygab.conditionalactions.conditions.types

import org.bukkit.OfflinePlayer

class NumericCondition(input: List<String>, private val function: (Double, Double) -> Boolean) : ConditionType(input) {
    private val leftSideValue = leftSide.toDoubleOrNull()
    private val rightSideValue = rightSide.toDoubleOrNull()

    private fun get(value: String) = value.replace(",", "").toDoubleOrNull() ?: 0.0

    fun getLeft(player: OfflinePlayer?, replacements: Map<String, String>) = leftSideValue ?: get(parseLeft(player, replacements))
    fun getRight(player: OfflinePlayer?, replacements: Map<String, String>) = rightSideValue ?: get(parseRight(player, replacements))

    override fun isMet(player: OfflinePlayer?, replacements: Map<String, String>) = function(getLeft(player, replacements), getRight(player, replacements))
}