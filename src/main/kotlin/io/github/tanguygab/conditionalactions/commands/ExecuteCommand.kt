package io.github.tanguygab.conditionalactions.commands

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import org.bukkit.command.CommandSender

class ExecuteCommand(plugin: ConditionalActions) : CACommand(plugin) {
    override fun onCommand(sender: CommandSender, args: List<String>) {
        if (args.isEmpty()) {
            sendMessage(sender, "&cYou need to provide a player!")
            return
        }
        if (args.size < 2) {
            sendMessage(sender, "&cYou need to provide an action!")
            return
        }
        val name = args[0]
        val p = Utils.getOfflinePlayer(name)
        if (p == null && !name.equals("--console", ignoreCase = true)) {
            sendMessage(sender, "&cPlayer not found!")
            return
        }

        val action = args.joinToString(" ").substring(name.length + 1)

        if (!plugin.actionManager.findAndExecute(p, action, false)) sendMessage(sender, "&cAction not found!")
    }

    override fun onTabComplete(sender: CommandSender, args: List<String>): List<String>? {
        if (args.size < 2) return null
        val arg = args.subList(2, args.size).joinToString(" ")
        val action = plugin.actionManager.find(arg)
        return if (action == null) getArguments(plugin.actionManager.getSuggestions(), arg)
        else listOf(action.getSuggestion())
    }
}
