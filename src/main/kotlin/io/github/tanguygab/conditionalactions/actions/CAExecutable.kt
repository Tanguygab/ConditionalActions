package io.github.tanguygab.conditionalactions.actions

import org.bukkit.OfflinePlayer

interface CAExecutable {
    fun execute(player: OfflinePlayer?): Boolean
}