package io.github.tanguygab.conditionalactions.actions.types.bungee

import org.bukkit.OfflinePlayer

class BungeeConsoleAction : BungeeAction("^(?i)bungeeconsole:( )?".toRegex()) {
    override fun getSuggestion() = "bungeeconsole: <command>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val command = parsePlaceholders(player, match)
        sendData {
            writeUTF("command")
            writeUTF("")
            writeUTF(command)
        }
    }
}
