package io.github.tanguygab.conditionalactions.actions.types.proxy

import org.bukkit.OfflinePlayer

class ProxyConsoleAction : ProxyAction("^(?i)(proxy|bungee|velocity)console:( )?".toRegex()) {
    override fun getSuggestion() = "proxyconsole: <command>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val command = parsePlaceholders(player, match)
        sendData {
            writeUTF("command")
            writeUTF("")
            writeUTF(command)
        }
    }
}
