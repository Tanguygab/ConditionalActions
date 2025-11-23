package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import org.intellij.lang.annotations.Language
import java.util.regex.Pattern

abstract class Action @JvmOverloads constructor(@Language("RegExp") regex: String, val replaceMatch: Boolean = true) {
    val pattern: Pattern = Pattern.compile(regex)
    protected val plugin = ConditionalActions.INSTANCE
    abstract fun getSuggestion(): String

    abstract fun execute(player: OfflinePlayer?, match: String)

    protected fun parsePlaceholders(player: OfflinePlayer?, string: String, colors: Boolean = false): String {
        val string = PlaceholderAPI.setPlaceholders(player, string)
        return if (colors) Utils.colors(string) else string
    }

    protected fun parseInt(str: String, def: Int) = str.toIntOrNull() ?: def

    protected fun split(args: String): List<String> = args.split(plugin.actionManager.argumentSeparator)

    protected fun getSuggestionWithArgs(vararg args: String?) = args.joinToString(plugin.actionManager.argumentSeparator)
}