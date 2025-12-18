package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import me.clip.placeholderapi.PlaceholderAPI
import me.neznamy.tab.shared.TAB
import me.neznamy.tab.shared.features.PlaceholderManagerImpl
import org.bukkit.OfflinePlayer
import org.intellij.lang.annotations.Language
import java.util.regex.Pattern

abstract class Action @JvmOverloads constructor(@Language("RegExp") regex: String, val replaceMatch: Boolean = true) {
    val pattern: Pattern = Pattern.compile(regex)
    protected val plugin = ConditionalActions.INSTANCE
    abstract fun getSuggestion(): String

    abstract fun execute(player: OfflinePlayer?, match: String)

    protected fun parsePlaceholders(player: OfflinePlayer?, string: String, colors: Boolean = false): String {
        var string = string
        if (plugin.server.pluginManager.isPluginEnabled("TAB") && player?.player != null) {
            val tabPlayer = TAB.getInstance().getPlayer(player.uniqueId)
            val placeholders = PlaceholderManagerImpl.detectPlaceholders(string)
                .map { TAB.getInstance().placeholderManager.getPlaceholder(it) }

            for (placeholder in placeholders) string = placeholder.set(string, tabPlayer)
        } else string = PlaceholderAPI.setPlaceholders(player, string)
        return if (colors) Utils.colors(string) else string
    }

    protected fun parseInt(str: String, def: Int) = str.toIntOrNull() ?: def

    protected fun split(args: String): List<String> = args.split(plugin.actionManager.argumentSeparator)

    protected fun getSuggestionWithArgs(vararg args: String?) = args.joinToString(plugin.actionManager.argumentSeparator)
}