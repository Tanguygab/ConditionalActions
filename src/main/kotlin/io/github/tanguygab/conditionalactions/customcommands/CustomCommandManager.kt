package io.github.tanguygab.conditionalactions.customcommands

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.SimpleCommandMap
import org.bukkit.configuration.ConfigurationSection

class CustomCommandManager(plugin: ConditionalActions) {
    val commands = mutableMapOf<String, CustomCommand>()
    val aliases = mutableMapOf<String, String>()

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

        try {
            val f = plugin.server.javaClass.getDeclaredField("commandMap")
            f.isAccessible = true
            val commandMap = (f.get(plugin.server) as CommandMap)

            val f0 = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
            f0.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val map = f0.get(commandMap) as MutableMap<String, Command>

            commands.forEach { (name: String, command: CustomCommand) ->
                if (!command.register) return@forEach
                map[name] = command
                command.aliases.forEach { map[it] = command }
            }
        } catch (e: Exception) { e.printStackTrace() }

        plugin.server.pluginManager.registerEvents(CommandListener(this), plugin)
    }
}
