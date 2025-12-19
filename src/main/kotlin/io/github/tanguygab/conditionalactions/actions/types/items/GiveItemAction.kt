package io.github.tanguygab.conditionalactions.actions.types.items

import io.github.tanguygab.conditionalactions.actions.Action
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class GiveItemAction : Action("^(i?)give-item:( )?") {
    override fun getSuggestion() = "give-item: <material> <amount> <name\\nlore>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return
        var args = match.split(" ")

        val material = args[0].uppercase()
        val mat = Material.getMaterial(material) ?: return
        val item = ItemStack(mat)


        if (args.size > 1) item.amount = parseInt(parsePlaceholders(player, args[1]), 1)
        val meta = item.itemMeta
        if (args.size > 2 && meta != null) {
            args = args.subList(2, args.size)
            args = args.joinToString(" ").split("\\n")
            meta.displayName(mm.deserialize(parsePlaceholders(player, args[0])))

            if (args.size > 1) {
                val lore = mutableListOf<Component>()
                for (line in args.subList(1, args.size))
                    lore.add(mm.deserialize(parsePlaceholders(player,line)))
                meta.lore(lore)
                item.itemMeta = meta
            }
        }
        player.inventory.addItem(item)
    }
}
