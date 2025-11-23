package io.github.tanguygab.conditionalactions.actions.types.storage

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveItemStorageAction : Action("^(i?)give-item-storage:( )?") {
    override fun getSuggestion() = "give-item-storage: <name>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return

        val args = match.split(" ")
        //String name = args[0];
        val amt = if (args.size > 1) parseInt(args[1], 1) else 1

        val item = ItemStack(Material.STONE) //ARMenu.get().getItemStorage().getItem(name);

        //if (item == null) return;
        for (i in 0..<amt) player.inventory.addItem(item)
    }
}
