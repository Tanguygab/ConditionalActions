package io.github.tanguygab.conditionalactions.customcommands

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import io.github.tanguygab.conditionalactions.hook.tab.ArgPlaceholders
import io.github.tanguygab.conditionalactions.hook.tab.ThreadPlaceholder
import me.neznamy.tab.shared.platform.TabPlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class CustomCommandManager(plugin: ConditionalActions) {
    val commands = mutableMapOf<String, CustomCommand>()
    val aliases = mutableMapOf<String, String>()

    internal val runningCommandsArguments = ConcurrentHashMap<Thread, List<String>>()
    internal val tabPlaceholders: ArgPlaceholders<ThreadPlaceholder>?

    init {
        Utils.updateFiles(plugin, "commands.yml", "commands/default-commands.yml")


        Utils.loadFiles(plugin.dataFolder.resolve("commands"), "") { file, _ ->
            YamlConfiguration.loadConfiguration(file).getValues(false).forEach { (name, obj) ->
                if (obj !is ConfigurationSection) return@loadFiles
                val register = obj.getBoolean("register", true)
                val force = obj.getBoolean("force", true)
                val aliases = obj.getStringList("aliases")
                val actions = obj.getList("actions") as List<*>

                commands[name] = CustomCommand(name, register, force, aliases, actions)
                aliases.forEach { this.aliases[it] = name }
            }
        }

        val map = plugin.server.commandMap.knownCommands

        commands.forEach { (name: String, command: CustomCommand) ->
            if (!command.register) return@forEach
            map[name] = command
            command.aliases.forEach { map[it] = command }
        }
        tabPlaceholders = if (plugin.server.pluginManager.isPluginEnabled("TAB")) {
            object : ArgPlaceholders<ThreadPlaceholder>("conditionalactions_") {
                override fun new(identifier: String, default: String) = ThreadPlaceholder(identifier)
                override fun update(placeholder: ThreadPlaceholder, player: TabPlayer?, value: String) = placeholder.updateValue(value)
            }
        } else null

        plugin.server.pluginManager.registerEvents(CommandListener(this), plugin)
    }

    fun getCurrentArgs() = runningCommandsArguments[Thread.currentThread()]
}
