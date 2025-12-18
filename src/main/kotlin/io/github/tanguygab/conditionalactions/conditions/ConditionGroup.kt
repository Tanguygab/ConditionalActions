package io.github.tanguygab.conditionalactions.conditions

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.conditions.types.ConditionType
import me.neznamy.tab.shared.TAB
import org.bukkit.OfflinePlayer
import java.util.UUID

class ConditionGroup(private val manager: ConditionManager, args: String, name: String? = null) {
    private val conditions = mutableListOf<MutableList<ConditionType>>()

    init {
        for (arg in args.split(" *\\|\\| *".toRegex())) {
            val list = mutableListOf<ConditionType>()

            if (arg.contains("&&")) for (condition in args.split(" *&& *".toRegex()))
                add(list, condition)
            else add(list, arg)

            conditions.add(list)
        }

        if (ConditionalActions.INSTANCE.server.pluginManager.isPluginEnabled("TAB")) {
            val placeholder = TAB.getInstance().placeholderManager.registerPlayerPlaceholder("%ca-condition:${name ?: UUID.randomUUID()}%", 1000) { p ->
                "" + isMet(p.player as OfflinePlayer)
            }
            conditions.flatten()
                .flatMap { it.getUsedPlaceholders() }
                .forEach { placeholder.addParent(it) }
        }
    }

    private fun add(list: MutableList<ConditionType>, condition: String) {
        val type = manager.find(condition)
        if (type != null) list.add(type)
    }

    fun isMet(player: OfflinePlayer?): Boolean {
        for (list in conditions) { // OR
            var met = true
            for (condition in list)  // AND
                if (condition.isMet(player) == condition.inverted)
                    met = false
            if (met) return true // if all AND conditions are met, return true
        }
        return false
    }
}
