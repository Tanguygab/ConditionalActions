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
            if (player == null) return null

            val i = arg.substringAfter("args-").toIntOrNull()
            val args = plugin.customCommandManager.getCurrentArgs()

            return if (i == null || args == null || i >= args.size) null
            else args[i]
        }

        return when (arg) {
            "args" -> if (player == null) null else plugin.customCommandManager.getCurrentArgs()?.joinToString(" ")
            "args-size" -> if (player == null) null else plugin.customCommandManager.getCurrentArgs()?.size

            "data" -> if (player == null) null else plugin.dataManager.getData(player, params)
            "global-data" -> plugin.dataManager.getGlobalData(params)

            "output" -> plugin.conditionManager.getConditionalOutput(params)?.getOutput(player) ?: ""
            "condition" -> plugin.conditionManager.getCondition(params.replace("[{(prc)}]", "%")).isMet(player)
            else -> null
        }
    }
}
