package io.github.tanguygab.conditionalactions

import io.github.tanguygab.conditionalactions.actions.ActionManager
import io.github.tanguygab.conditionalactions.commands.*
import io.github.tanguygab.conditionalactions.conditions.ConditionManager
import io.github.tanguygab.conditionalactions.customcommands.CustomCommandManager
import io.github.tanguygab.conditionalactions.hook.papi.CAExpansion
import io.github.tanguygab.conditionalactions.hook.papi.PAPIExpansion
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class ConditionalActions : JavaPlugin() {
    private val subcommands = mutableMapOf<String, CACommand>()
    private val expansions = mutableListOf<PAPIExpansion>()

    lateinit var dataManager: DataManager
    lateinit var actionManager: ActionManager
    lateinit var conditionManager: ConditionManager
    lateinit var customCommandManager: CustomCommandManager


    override fun onEnable() {
        INSTANCE = this
        saveDefaultConfig()
        reloadConfig()

        dataManager = DataManager()
        conditionManager = ConditionManager(this)
        customCommandManager = CustomCommandManager(this)
        actionManager = ActionManager(this, config.getString("argument-separator") ?: ",")

        expansions.add(CAExpansion(this))
        expansions.add(object : PAPIExpansion(this@ConditionalActions, "condition") {
            override fun parse(player: OfflinePlayer?, params: String) = conditionManager.getCondition(params).isMet(player)
        })
        expansions.forEach { it.register() }

        subcommands["reload"] = ReloadCommand(this)
        subcommands["execute"] = ExecuteCommand(this)
        subcommands["group"] = GroupCommand(this)
        subcommands["condition"] = ConditionCommand(this)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        server.asyncScheduler.cancelTasks(this)
        server.globalRegionScheduler.cancelTasks(this)

        subcommands.clear()

        expansions.forEach { it.unregister() }
        expansions.clear()
    }

    fun async(run: Runnable) {
        server.asyncScheduler.runNow(this) { run.run() }
    }

    fun sync(player: Player?, run: Runnable) {
        if (player == null) server.globalRegionScheduler.run(this) { run.run() }
        else player.scheduler.run(this, { run.run() }, null)
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        val arg = if (args.isNotEmpty()) args[0] else ""
        subcommands[arg]?.onCommand(sender, args.slice(IntRange(1, args.size - 1)))
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        val arg = if (args.isNotEmpty()) args[0] else ""
        if (!subcommands.containsKey(arg)) return listOf(
            "execute",
            "group",
            "condition",
            "reload"
        )
        return subcommands[arg]?.onTabComplete(sender, args.slice(IntRange(1, args.size - 1)))
    }

    companion object {
        lateinit var INSTANCE: ConditionalActions
    }
}
