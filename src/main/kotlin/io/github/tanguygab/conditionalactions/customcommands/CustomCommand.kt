package io.github.tanguygab.conditionalactions.customcommands

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.actions.ActionGroup
import io.github.tanguygab.conditionalactions.actions.ActionManager
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class CustomCommand(
    name: String,
    val register: Boolean,
    val force: Boolean,
    aliases: List<String>,
    private val actionList: List<*>
) : BukkitCommand(name) {
    private lateinit var actions: ActionGroup

    init {
        setAliases(aliases)
    }

    fun loadActions(actionManager: ActionManager) {
        actions = ActionGroup(actionManager, actionList)
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        ConditionalActions.INSTANCE.async {
            val args = args.map { it.replace("%", "") }
            ConditionalActions.INSTANCE.customCommandManager.apply {
                runningCommandsArguments[Thread.currentThread()] = args
                tabPlaceholders?.update(null, args)
                actions.execute(sender as? OfflinePlayer)
                tabPlaceholders?.update(null)
                runningCommandsArguments.remove(Thread.currentThread())
            }
        }
        return true
    }
}
