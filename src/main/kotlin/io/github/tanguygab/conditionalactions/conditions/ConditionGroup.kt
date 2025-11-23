package io.github.tanguygab.conditionalactions.conditions

import io.github.tanguygab.conditionalactions.conditions.types.ConditionType
import org.bukkit.OfflinePlayer

class ConditionGroup(private val manager: ConditionManager, args: String) {
    private val conditions = mutableListOf<MutableList<ConditionType>>()

    init {
        for (arg in args.split(" *\\|\\| *".toRegex())) {
            val list = mutableListOf<ConditionType>()

            if (arg.contains("&&")) for (condition in args.split(" +&& +".toRegex()))
                add(list, condition)
            else add(list, arg)

            conditions.add(list)
        }
    }

    private fun add(list: MutableList<ConditionType>, condition: String) {
        val type = manager.find(condition)
        if (type != null) list.add(type)
    }

    fun isMet(player: OfflinePlayer?) = isMet(player, mapOf())

    fun isMet(player: OfflinePlayer?, replacements: Map<String, String>): Boolean {
        for (list in conditions) { // OR
            var met = true
            for (condition in list)  // AND
                if (condition.isMet(player, replacements) == condition.inverted)
                    met = false
            if (met) return true // if all AND conditions are met, return true
        }
        return false
    }
}
