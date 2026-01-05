package io.github.tanguygab.conditionalactions

import com.google.common.io.ByteStreams
import io.github.tanguygab.conditionalactions.actions.ActionManager
import io.github.tanguygab.conditionalactions.commands.*
import io.github.tanguygab.conditionalactions.conditions.ConditionManager
import io.github.tanguygab.conditionalactions.customcommands.CustomCommandManager
import io.github.tanguygab.conditionalactions.hook.papi.CAExpansion
import io.github.tanguygab.conditionalactions.hook.papi.PAPIExpansion
import io.github.tanguygab.conditionalactions.listener.PlayerJoinListener
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener

class ConditionalActions : JavaPlugin(), PluginMessageListener {
    private val subcommands = mutableMapOf<String, CACommand>()
    lateinit var expansion: PAPIExpansion
    val servers = mutableMapOf<String, Int>()

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

        expansion = CAExpansion(this).also { it.register() }

        subcommands["reload"] = ReloadCommand(this)
        subcommands["execute"] = ExecuteCommand(this)
        subcommands["group"] = GroupCommand(this)
        subcommands["condition"] = ConditionCommand(this)

        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        server.messenger.registerOutgoingPluginChannel(this, CHANNEL)
        server.messenger.registerIncomingPluginChannel(this, CHANNEL, this)
        server.sendPluginMessage(this, CHANNEL, ByteStreams.newDataOutput().apply { writeUTF("start") }.toByteArray())
        server.pluginManager.registerEvents(PlayerJoinListener(this), this)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        server.asyncScheduler.cancelTasks(this)
        server.globalRegionScheduler.cancelTasks(this)

        server.messenger.unregisterOutgoingPluginChannel(this)
        server.messenger.unregisterIncomingPluginChannel(this)

        subcommands.clear()
        servers.clear()
        expansion.unregister()
    }

    fun async(run: Runnable) {
        server.asyncScheduler.runNow(this) { run.run() }
    }

    fun sync(player: Player?, run: Runnable) {
        if (player == null) server.globalRegionScheduler.run(this) { run.run() }
        else player.scheduler.run(this, { run.run() }, null)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val arg = if (args.isNotEmpty()) args[0] else ""
        subcommands[arg]?.onCommand(sender, args.slice(IntRange(1, args.size - 1)))
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        val arg = if (args.isNotEmpty()) args[0] else ""
        if (!subcommands.containsKey(arg)) return listOf("execute", "group", "condition", "reload")
        return subcommands[arg]?.onTabComplete(sender, args.slice(IntRange(1, args.size - 1)))
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != CHANNEL) return
        ByteStreams.newDataInput(message).apply {
            if (readUTF() != "update") return
            val size = readInt()
            (0..<size).forEach { _ ->
                val info = readUTF().split("|")
                val server = info[0]
                if (info[1] == "false")
                    servers.remove(server)
                else servers[server] = info[2].toInt()
            }
        }
    }

    companion object {
        lateinit var INSTANCE: ConditionalActions
        const val CHANNEL = "conditionalactions:channel"
    }
}
