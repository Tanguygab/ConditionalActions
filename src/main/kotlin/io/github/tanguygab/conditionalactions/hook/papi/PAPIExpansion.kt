package io.github.tanguygab.conditionalactions.hook.papi

import io.github.tanguygab.conditionalactions.ConditionalActions
import me.clip.placeholderapi.PlaceholderAPIPlugin
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

abstract class PAPIExpansion(protected val plugin: ConditionalActions, private val identifier: String) : PlaceholderExpansion() {

    override fun getIdentifier() = identifier
    override fun getAuthor() = plugin.description.authors.joinToString(", ")
    override fun getVersion() = plugin.description.version

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        val output = parse(player, params) ?: return null
        if (output is Boolean) {
            return if (output)
                PlaceholderAPIPlugin.booleanTrue()
            else PlaceholderAPIPlugin.booleanFalse()
        }
        return output.toString()
    }

    abstract fun parse(player: OfflinePlayer?, params: String): Any?
}
