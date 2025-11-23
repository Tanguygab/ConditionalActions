package io.github.tanguygab.conditionalactions.commands

import io.github.tanguygab.conditionalactions.ConditionalActions
import org.bukkit.command.CommandSender

class ReloadCommand(plugin: ConditionalActions) : CACommand(plugin) {
    override fun onCommand(sender: CommandSender, args: List<String>) {
        if (!sender.hasPermission("conditionalactions")) {
            sendMessage(sender, "&cYou don't have the permission to do this!")
            return
        }
        plugin.onDisable()
        plugin.onEnable()
        sendMessage(sender, "&aPlugin reloaded successfully!")
    }

    override fun onTabComplete(sender: CommandSender, args: List<String>) = listOf<String>()
}
