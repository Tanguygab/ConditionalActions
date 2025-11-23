package io.github.tanguygab.conditionalactions.actions

import org.bukkit.OfflinePlayer

interface CAExecutable {
    fun execute(player: OfflinePlayer?) = execute(player, mapOf())

    fun execute(player: OfflinePlayer?, replacements: Map<String, String>): Boolean
}