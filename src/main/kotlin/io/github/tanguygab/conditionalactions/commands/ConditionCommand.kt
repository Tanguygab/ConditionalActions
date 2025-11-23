package io.github.tanguygab.conditionalactions.commands

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import org.bukkit.command.CommandSender

class ConditionCommand(plugin: ConditionalActions) : CACommand(plugin) {
    override fun onCommand(sender: CommandSender, args: List<String>) {
        if (args.isEmpty()) {
            sendMessage(sender, "&7Use either &8list &7or &8check&7.")
            return
        }
        if (args[0].equals("list", ignoreCase = true)) {
            sendMessage(sender, "Conditions:\n&7" + plugin.conditionManager.getConditions().joinToString(", "))
            return
        }
        if (!args[0].equals("check", ignoreCase = true)) {
            sendMessage(sender, "&7Use either &8list &7or &8check&7.")
            return
        }

        val args = args.subList(1, args.size)
        if (args.isEmpty()) {
            sendMessage(sender, "&cYou need to provide a player!")
            return
        }
        if (args.size < 2) {
            sendMessage(sender, "&cYou need to provide a condition!")
            return
        }
        val name = args[0]
        val p = Utils.getOfflinePlayer(name)
        if (p == null && !name.equals("--console", ignoreCase = true)) {
            sendMessage(sender, "&cPlayer not found!")
            return
        }

        val condition = args.joinToString(" ").substring(name.length + 1)

        val group = plugin.conditionManager.getCondition(condition)
        sendMessage(sender, "&7$name ${if (group.isMet(p)) "&ameets" else "&cdoes not meet"} the condition!")
    }

    override fun onTabComplete(sender: CommandSender, args: List<String>): List<String>? {
        if (args.isEmpty()) return null
        if (args.size == 1) return getArguments(listOf("list", "check"), args[0])
        if (args[0].equals("list", ignoreCase = true)) return listOf()
        if (!args[0].equals("check", ignoreCase = true)) return null
        return if (args.size > 2) getArguments(plugin.conditionManager.getConditions(), args[2]) else null
    }
}
