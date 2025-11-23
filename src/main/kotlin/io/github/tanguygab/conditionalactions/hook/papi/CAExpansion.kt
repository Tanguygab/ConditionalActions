package io.github.tanguygab.conditionalactions.hook.papi

import io.github.tanguygab.conditionalactions.ConditionalActions
import org.bukkit.OfflinePlayer

class CAExpansion(plugin: ConditionalActions) : PAPIExpansion(plugin, "conditionalactions") {
    override fun parse(player: OfflinePlayer?, params: String): Any? {
        val args = params.split("_")
        if (args.size < 2) return null

        val arg = args[0]
        val params = params.substring(arg.length + 1)

        return when (arg) {
            "data" -> if (player == null) null else plugin.dataManager.getData(player, params)
            "global-data" -> plugin.dataManager.getGlobalData(params)
            "output" -> plugin.conditionManager.getConditionalOutput(params)?.getOutput(player) ?: ""
            else -> null
        }
    }
}
