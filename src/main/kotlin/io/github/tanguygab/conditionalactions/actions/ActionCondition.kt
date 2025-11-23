package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.conditions.ConditionGroup
import org.bukkit.OfflinePlayer

data class ActionCondition(
    private val condition: ConditionGroup,
    private val success: ActionGroup? = null,
    private val deny: ActionGroup? = null
) : CAExecutable {

    override fun execute(player: OfflinePlayer?, replacements: Map<String, String>): Boolean {
        if (condition.isMet(player, replacements))
            return success?.execute(player, replacements) == true
        return deny?.execute(player, replacements) == true
    }
}
