package io.github.tanguygab.conditionalactions.actions

import org.bukkit.OfflinePlayer

data class ActionData(
    private val action: Action,
    private val arguments: String
) : CAExecutable {

    override fun execute(player: OfflinePlayer?): Boolean {
        var args = arguments

        if (action.replaceMatch) args = args.replace(action.pattern.pattern().toRegex(), "")

        action.execute(player, args)
        return true
    }
}
