package io.github.tanguygab.conditionalactions.conditions.types

import org.bukkit.OfflinePlayer

class NumericCondition(input: List<String>, private val function: (Double, Double) -> Boolean) : ConditionType(input) {
    private val leftSideValue = leftSide.toDoubleOrNull()
    private val rightSideValue = rightSide.toDoubleOrNull()

    private fun get(value: String) = value.replace(",", "").toDoubleOrNull() ?: 0.0

    fun getLeft(player: OfflinePlayer?) = leftSideValue ?: get(parseLeft(player))
    fun getRight(player: OfflinePlayer?) = rightSideValue ?: get(parseRight(player))

    override fun isMet(player: OfflinePlayer?) = function(getLeft(player), getRight(player))
}