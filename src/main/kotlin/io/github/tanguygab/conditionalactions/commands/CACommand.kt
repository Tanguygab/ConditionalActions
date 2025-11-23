package io.github.tanguygab.conditionalactions.commands

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import org.bukkit.command.CommandSender

abstract class CACommand(protected val plugin: ConditionalActions) {

    abstract fun onCommand(sender: CommandSender, args: List<String>)
    abstract fun onTabComplete(sender: CommandSender, args: List<String>): List<String>?

    protected fun sendMessage(sender: CommandSender, message: String) {
        sender.sendMessage(Utils.colors(message))
    }

    fun getArguments(suggestions: Collection<String>, arg: String) = suggestions.filter { it.startsWith(arg.lowercase()) }
}
