package io.github.tanguygab.conditionalactions.conditions.types

import io.github.tanguygab.conditionalactions.conditions.ConditionGroup
import org.bukkit.OfflinePlayer

class GroupCondition(private val group: ConditionGroup, input: String) : ConditionType(listOf(input, "")) {
    override fun isMet(player: OfflinePlayer?, replacements: Map<String, String>) = group.isMet(player, replacements)
}
