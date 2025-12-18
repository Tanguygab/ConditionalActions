package io.github.tanguygab.conditionalactions.conditions

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.conditions.types.ConditionType
import me.neznamy.tab.shared.TAB
import org.bukkit.OfflinePlayer
import java.util.UUID

class ConditionGroup(private val manager: ConditionManager, args: String, var name: String? = null) {
    private val conditions = mutableListOf<MutableList<ConditionType>>()

    init {
        if (name == null) name = UUID.randomUUID().toString()

        for (arg in args.split(" *\\|\\| *".toRegex())) {
            val list = mutableListOf<ConditionType>()

            if (arg.contains("&&")) for (condition in args.split(" *&& *".toRegex()))
                add(list, condition)
            else add(list, arg)

            conditions.add(list)
        }

        if (ConditionalActions.INSTANCE.server.pluginManager.isPluginEnabled("TAB")) {
            registerTABPlaceholder()
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

    fun registerTABPlaceholder() {
        val used = conditions.flatten()
            .flatMap { it.getUsedPlaceholders() }
            .map { TAB.getInstance().placeholderManager.getPlaceholder(it)}
        val refresh = used.minOfOrNull { it.refresh } ?: -1

        val identifier = "%ca-condition:$name%"
        TAB.getInstance().placeholderManager.registerPlayerPlaceholder(identifier, refresh) {
            p -> "" + isMet(p.player as OfflinePlayer)
        }
        used.forEach { it.addParent(identifier) }
    }
}
