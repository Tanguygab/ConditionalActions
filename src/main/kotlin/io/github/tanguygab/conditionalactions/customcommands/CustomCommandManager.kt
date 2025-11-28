package io.github.tanguygab.conditionalactions.customcommands

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import org.bukkit.configuration.ConfigurationSection
import java.util.concurrent.ConcurrentHashMap

class CustomCommandManager(plugin: ConditionalActions) {
    val commands = mutableMapOf<String, CustomCommand>()
    val aliases = mutableMapOf<String, String>()

    internal val runningCommandsArguments = ConcurrentHashMap<Thread, Array<String>>()

    init {
        Utils.updateFiles(plugin, "commands.yml", "commands/default-commands.yml")

        Utils.loadFiles(plugin, "commands") { name: String, obj: Any ->
            if (obj !is ConfigurationSection) return@loadFiles
            val register = obj.getBoolean("register", true)
            val force = obj.getBoolean("force", true)
            val aliases = obj.getStringList("aliases")
            val actions = obj.getList("actions") as List<*>

            commands[name] = CustomCommand(name, register, force, aliases, actions)
            aliases.forEach { this.aliases[it] = name }
        }

        val map = plugin.server.commandMap.knownCommands

        commands.forEach { (name: String, command: CustomCommand) ->
            if (!command.register) return@forEach
            map[name] = command
            command.aliases.forEach { map[it] = command }
        }

        plugin.server.pluginManager.registerEvents(CommandListener(this), plugin)
    }

    fun getCurrentArgs() = runningCommandsArguments[Thread.currentThread()]
}
