package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.ConditionalActions
import me.clip.placeholderapi.PlaceholderAPI
import me.neznamy.tab.shared.TAB
import me.neznamy.tab.shared.features.PlaceholderManagerImpl
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.OfflinePlayer

abstract class Action(val pattern: Regex, val replaceMatch: Boolean = true) {
    protected val plugin = ConditionalActions.INSTANCE
    protected val mm get() = MiniMessage.miniMessage()

    open val description = mm.deserialize("<dark_red>No description.")
    abstract fun getSuggestion(): String

    abstract fun execute(player: OfflinePlayer?, match: String)

    protected fun parsePlaceholders(player: OfflinePlayer?, string: String): String {
        if (plugin.server.pluginManager.isPluginEnabled("TAB") && player?.player != null) {
            val tabPlayer = TAB.getInstance().getPlayer(player.uniqueId)
            val placeholders = PlaceholderManagerImpl.detectPlaceholders(string)
                .map { TAB.getInstance().placeholderManager.getPlaceholder(it) }

            var string = string
            for (placeholder in placeholders) string = placeholder.set(string, tabPlayer)
            return string
        }
        return PlaceholderAPI.setPlaceholders(player, string)
    }

    protected fun parseInt(str: String, def: Int) = str.toIntOrNull() ?: def

    protected fun split(args: String): List<String> = args.split(plugin.actionManager.argumentSeparator)

    protected fun getSuggestionWithArgs(vararg args: String?) = args.joinToString(plugin.actionManager.argumentSeparator)
}