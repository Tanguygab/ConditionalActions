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

    protected fun parse(player: OfflinePlayer?, string: String): String {
        var string = string
        if (ConditionalActions.INSTANCE.server.pluginManager.isPluginEnabled("TAB") && player?.player != null) {
            val tabPlayer = TAB.getInstance().getPlayer(player.uniqueId)
            val placeholders = PlaceholderManagerImpl.detectPlaceholders(string)
                .map { TAB.getInstance().placeholderManager.getPlaceholder(it) }

            for (placeholder in placeholders) string = placeholder.set(string, tabPlayer)
            return string
        }
        return PlaceholderAPI.setPlaceholders(player, string)
    }

    protected fun parseLeft(player: OfflinePlayer?) = parse(player, leftSide)

    protected fun parseRight(player: OfflinePlayer?) = parse(player, rightSide)

    abstract fun isMet(player: OfflinePlayer?): Boolean

    fun getUsedPlaceholders() = PlaceholderManagerImpl.detectPlaceholders(leftSide) + PlaceholderManagerImpl.detectPlaceholders(rightSide)
}
