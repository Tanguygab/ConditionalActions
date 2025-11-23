package io.github.tanguygab.conditionalactions.actions.types.data

import io.github.tanguygab.conditionalactions.Utils
import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer

class SetDataAction : Action("^(?i)set-data: ") {
    override val suggestion = "set-data: <player|--global> <data> <value>"

    override fun execute(player: OfflinePlayer?, match: String) {
        val args = match.split(" ")
        if (args.size < 3) return

        val name = args[0]
        val data = args[1]
        val value = args[2]

        val manager = plugin.dataManager
        if (manager.isGlobal(name)) {
            manager.setGlobalData(data, value)
            return
        }
        val player = Utils.getOfflinePlayer(name)
        if (player != null) manager.setData(player, data, value)
    }
}
