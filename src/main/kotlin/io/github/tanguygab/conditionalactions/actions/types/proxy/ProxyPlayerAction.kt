package io.github.tanguygab.conditionalactions.actions.types.proxy

import org.bukkit.OfflinePlayer

class ProxyPlayerAction : ProxyAction("^(?i)(proxy|bungee|velocity)(player|cmd|command):( )?".toRegex()) {
    override fun getSuggestion() = "proxyplayer: <command>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val command = parsePlaceholders(player, match)

        val sender = if (player == null) "" else player.name
        if (sender == null) {
            plugin.logger.warning("Tried to run command \"$command\" as invalid player")
            return
        }
        sendData {
            writeUTF("command")
            writeUTF(sender)
            writeUTF(command)
        }
    }
}
