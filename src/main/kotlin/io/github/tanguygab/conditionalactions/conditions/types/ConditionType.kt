package io.github.tanguygab.conditionalactions.conditions.types

import io.github.tanguygab.conditionalactions.ConditionalActions
import me.clip.placeholderapi.PlaceholderAPI
import me.neznamy.tab.shared.TAB
import me.neznamy.tab.shared.features.PlaceholderManagerImpl
import org.bukkit.OfflinePlayer

abstract class ConditionType(input: List<String>) {
    val inverted: Boolean = input[0].startsWith("!")
    protected val leftSide = if (inverted) input[0].substring(1) else input[0]
    protected val rightSide = if (input.size > 1) input[1] else ""

    protected fun parseLeft(player: OfflinePlayer?) = ConditionalActions.parse(player, leftSide)

    protected fun parseRight(player: OfflinePlayer?) = ConditionalActions.parse(player, rightSide)

    abstract fun isMet(player: OfflinePlayer?): Boolean

    fun getUsedPlaceholders() = PlaceholderManagerImpl.detectPlaceholders(leftSide) + PlaceholderManagerImpl.detectPlaceholders(rightSide)
}
