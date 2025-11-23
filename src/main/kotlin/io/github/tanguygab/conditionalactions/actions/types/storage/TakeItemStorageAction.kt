package io.github.tanguygab.conditionalactions.actions.types.storage

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TakeItemStorageAction : Action("^(i?)take-item-storage:( )?") {
    override fun getSuggestion() = "take-item-storage: <name>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return

        val args = match.split(" ")
        val name = args[0]
        var amt = if (args.size > 1) parseInt(args[1], 1) else 1

        val item = ItemStack(Material.STONE) //ARMenu.get().getItemStorage().getItem(name);

        //if (item == null) return;
        val items = player.inventory.contents
        for (i in items.indices) {
            if (amt <= 0) break

            if (item.isSimilar(items[i])) {
                val found = items[i] ?: continue
                if (found.amount <= amt) {
                    player.inventory.setItem(i, null)
                    amt -= found.amount
                } else if (found.amount > amt) {
                    found.amount -= amt
                    amt = 0
                }
            }
        }
    }
}
