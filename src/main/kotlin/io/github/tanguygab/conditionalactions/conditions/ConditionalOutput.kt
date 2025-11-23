package io.github.tanguygab.conditionalactions.conditions

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection

class ConditionalOutput(manager: ConditionManager, section: ConfigurationSection) {
    private val outputs = mutableMapOf<ConditionGroup, String>()

    init {
        section.getValues(false).forEach { (output: String, condition: Any) ->
            outputs[manager.getCondition(condition.toString())] = output
        }
    }

    fun getOutput(player: OfflinePlayer?): String? {
        for (condition in outputs.keys)
            if (condition.isMet(player))
                return outputs[condition]
        return ""
    }
}
