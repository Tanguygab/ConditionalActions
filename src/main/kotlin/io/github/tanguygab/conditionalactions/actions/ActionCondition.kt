package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.conditions.ConditionGroup
import org.bukkit.OfflinePlayer

data class ActionCondition(
    private val condition: ConditionGroup,
    private val success: ActionGroup? = null,
    private val deny: ActionGroup? = null
) : CAExecutable {

    override fun execute(player: OfflinePlayer?): Boolean {
        if (condition.isMet(player))
            return success?.execute(player) == true
        return deny?.execute(player) == true
    }
}
