package io.github.tanguygab.conditionalactions.commands

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import org.bukkit.command.CommandSender

class GroupCommand(plugin: ConditionalActions) : CACommand(plugin) {
    override fun onCommand(sender: CommandSender, args: List<String>) {
        if (args.isEmpty()) {
            sendMessage(sender, "&7Use either &8list &7or &8execute&7.")
            return
        }
        if (args[0].equals("list", ignoreCase = true)) {
            sendMessage(sender, "Groups:\n&7" + plugin.actionManager.getGroups().joinToString(", "))
            return
        }
        if (!args[0].equals("execute", ignoreCase = true)) {
            sendMessage(sender, "&7Use either &8list &7or &8execute&7.")
            return
        }

        val args = args.subList(1, args.size)
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

        if (!plugin.actionManager.findAndExecute(p, action, true)) sendMessage(sender, "&cGroup not found!")
    }

    override fun onTabComplete(sender: CommandSender, args: List<String>): List<String>? {
        if (args.size == 1) return getArguments(listOf("list", "execute"), args[0])
        if (args[0].equals("list", ignoreCase = true)) return listOf()
        if (!args[0].equals("execute", ignoreCase = true)) return null
        return if (args.size > 2) getArguments(plugin.actionManager.getGroups(), args[2]) else null
    }
}
