package io.github.tanguygab.conditionalactions.customcommands

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandListener(private val manager: CustomCommandManager) : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val args = e.message.split(" ")
        val cmd = args[0].lowercase().substring(1)
        var command = manager.commands[cmd]
        if (command == null && manager.aliases.containsKey(cmd))
            command = manager.commands[manager.aliases[cmd]]

        if (command != null && command.force) {
            e.isCancelled = true
            command.execute(e.player, cmd, args.subList(1, args.size).toTypedArray())
        }
    }
}
