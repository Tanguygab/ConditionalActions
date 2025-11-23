package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.ConditionalActions
import org.bukkit.OfflinePlayer
import kotlin.collections.get

class ActionGroup(
    private val manager: ActionManager,
    config: List<*>
) : CAExecutable {
    private val actions = mutableListOf<CAExecutable?>()

    init {
        config.forEach { action ->
            if (action is MutableMap<*, *>) {
                val condition = ConditionalActions.INSTANCE.conditionManager.getCondition(action["condition"] as String)
                actions.add(
                    ActionCondition(
                        condition = condition,
                        success = loadConditionList(action, "success"),
                        deny = loadConditionList(action, "deny")
                    )
                )
            } else add(actions, action as String)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadConditionList(map: MutableMap<*, *>, name: String): ActionGroup? {
        val obj = map[name] ?: return null
        val list = obj as? List<Any> ?: listOf(obj)
        return ActionGroup(manager, list)
    }

    private fun add(list: MutableList<CAExecutable?>, line: String) {
        if (line.equals("return", ignoreCase = true)) {
            list.add(null)
            return
        }
        val ac = manager.find(line)
        if (ac != null) list.add(ActionData(ac, line))
    }

    override fun execute(player: OfflinePlayer?, replacements: Map<String, String>): Boolean {
        for (executable in actions) {
            if (executable == null || !executable.execute(player, replacements)) return false
        }
        return true
    }
}
