package io.github.tanguygab.conditionalactions.hook.papi

import io.github.tanguygab.conditionalactions.ConditionalActions
import org.bukkit.OfflinePlayer

class CAExpansion(plugin: ConditionalActions) : PAPIExpansion(plugin, "conditionalactions") {
    override fun parse(player: OfflinePlayer?, params: String): Any? {
        val args = params.split("_")
        if (args.isEmpty()) return null

        val arg = args[0]
        val params = params.substringAfter("${arg}_")

        if (arg.startsWith("arg-")) {
            return arg.split("-").getOrNull(1)?.toIntOrNull()?.let { plugin.customCommandManager.getCurrentArgs()?.getOrNull(it) } ?: 0
        }

        return when (arg) {
            "args" -> if (player == null) null else plugin.customCommandManager.getCurrentArgs()?.joinToString(" ")
            "args-size" -> if (player == null) null else plugin.customCommandManager.getCurrentArgs()?.size

            "server-online" -> params in plugin.servers
            "server-players" -> plugin.servers[params] ?: 0

            "data" -> if (player == null) null else plugin.dataManager.getData(player, params)
            "global-data" -> plugin.dataManager.getGlobalData(params)

            "output" -> plugin.conditionManager.getConditionalOutput(params)?.getOutput(player) ?: ""
            "condition" -> plugin.conditionManager.getCondition(params.replace("[{(prc)}]", "%")).isMet(player)
            else -> null
        }
    }
}
