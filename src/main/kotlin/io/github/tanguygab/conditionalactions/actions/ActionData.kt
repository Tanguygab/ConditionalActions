package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.ConditionalActions
import org.bukkit.OfflinePlayer

data class ActionData(
    private val action: Action,
    private val arguments: String
) : CAExecutable {

    override fun execute(player: OfflinePlayer?, replacements: Map<String, String>): Boolean {
        var args: String = ConditionalActions.parseReplacements(arguments, replacements)

        if (action.replaceMatch) args = args.replace(action.pattern.pattern().toRegex(), "")

        action.execute(player, args)
        return true
    }
}
