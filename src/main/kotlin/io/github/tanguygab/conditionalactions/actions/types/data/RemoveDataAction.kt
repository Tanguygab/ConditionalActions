package io.github.tanguygab.conditionalactions.actions.types.data

import io.github.tanguygab.conditionalactions.Utils
import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class RemoveDataAction : Action("^(?i)remove-data: ") {
    override fun getSuggestion() = "remove-data: <player|--global> <data>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val args = match.split(" ")
        if (args.size < 2) return

        val name = args[0]
        val data = args[1]

        val manager = plugin.dataManager
        if (manager.isGlobal(name)) {
            manager.removeGlobalData(data)
            return
        }
        val player = Utils.getOfflinePlayer(name)
        if (player != null) manager.removeData(player, data)
    }
}
