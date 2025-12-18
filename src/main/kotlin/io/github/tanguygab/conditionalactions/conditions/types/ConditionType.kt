package io.github.tanguygab.conditionalactions.conditions.types

import me.clip.placeholderapi.PlaceholderAPI
import me.neznamy.tab.shared.features.PlaceholderManagerImpl
import org.bukkit.OfflinePlayer

abstract class ConditionType(input: List<String>) {
    val inverted: Boolean = input[0].startsWith("!")
    protected val leftSide = if (inverted) input[0].substring(1) else input[0]
    protected val rightSide = if (input.size > 1) input[1] else ""

    protected fun parse(player: OfflinePlayer?, string: String): String {
        return PlaceholderAPI.setPlaceholders(player, string)
    }

    protected fun parseLeft(player: OfflinePlayer?) = parse(player, leftSide)

    protected fun parseRight(player: OfflinePlayer?) = parse(player, rightSide)

    abstract fun isMet(player: OfflinePlayer?): Boolean

    fun getUsedPlaceholders() = PlaceholderManagerImpl.detectPlaceholders(leftSide) + PlaceholderManagerImpl.detectPlaceholders(rightSide)
}
