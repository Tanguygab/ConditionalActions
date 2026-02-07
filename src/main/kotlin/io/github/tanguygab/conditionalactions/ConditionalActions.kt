package io.github.tanguygab.conditionalactions

import com.google.common.io.ByteStreams
import io.github.tanguygab.conditionalactions.actions.ActionManager
import io.github.tanguygab.conditionalactions.commands.CACommands
import io.github.tanguygab.conditionalactions.conditions.ConditionManager
import io.github.tanguygab.conditionalactions.customcommands.CustomCommandManager
import io.github.tanguygab.conditionalactions.hook.papi.CAExpansion
import io.github.tanguygab.conditionalactions.hook.papi.PAPIExpansion
import io.github.tanguygab.conditionalactions.listener.PlayerJoinListener
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import java.io.File

class ConditionalActions : JavaPlugin(), PluginMessageListener {
    lateinit var expansion: PAPIExpansion
    val servers = mutableMapOf<String, Int>()
    var serverName = "Loading..."

    val mm = MiniMessage.miniMessage()
    lateinit var messages: MessagesFile

    lateinit var dataManager: DataManager
    lateinit var actionManager: ActionManager
    lateinit var conditionManager: ConditionManager
    lateinit var customCommandManager: CustomCommandManager

    override fun onLoad() {
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) {
            it.registrar().register(CACommands(this).main, listOf("ca"))
        }
    }

    override fun onEnable() {
        INSTANCE = this
        saveDefaultConfig()
        reloadConfig()
        val messagesFiles = File(dataFolder, "messages.yml")
        if (!messagesFiles.exists()) saveResource("messages.yml", true)
        messages = MessagesFile(messagesFiles, mm)

        dataManager = DataManager()
        conditionManager = ConditionManager(this)
        customCommandManager = CustomCommandManager(this)
        actionManager = ActionManager(this, config.getString("argument-separator") ?: ",")

        expansion = CAExpansion(this).also { it.register() }

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

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != CHANNEL) return
        ByteStreams.newDataInput(message).apply {
            when (readUTF()) {
                "name" -> serverName = readUTF()
                "update" -> {
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
        }
    }

    companion object {
        lateinit var INSTANCE: ConditionalActions
        const val CHANNEL = "conditionalactions:channel"
    }
}
