package io.github.tanguygab.conditionalactions.actions.types.items

import io.github.tanguygab.conditionalactions.actions.Action
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TakeItemAction : Action("^(i?)take-item:( )?".toRegex()) {
    private val mmLegacy get() = LegacyComponentSerializer.legacyAmpersand()

    override fun getSuggestion() = "take-item: <material> <amount> <name> <lore>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return

        var args = match.split(" ")
        val material = args[0].uppercase()

        val mat = Material.getMaterial(material) ?: return

        var amt = if (args.size > 1) parseInt(parsePlaceholders(player, args[1]), 1) else 99999
        if (amt <= 0) return

        var name: String? = null
        val lore = mutableListOf<String>()

        if (args.size > 2) {
            args = args.subList(2, args.size).joinToString(" ").split("\\n")
            name = parsePlaceholders(player, args[0])

            if (args.size > 1) for (line in args.subList(1, args.size))
                lore.add(parsePlaceholders(player, line))
        }

        val inv = player.inventory
        val items = inv.contents
        for (i in items.indices) {
            if (amt <= 0) break
            val found = items[i]
            if (found == null || !check(found, mat, name, lore)) continue

            if (found.amount <= amt) {
                inv.setItem(i, null)
                amt -= found.amount
                continue
            }
            if (found.amount > amt) {
                found.amount -= amt
                amt = 0
            }
        }
    }

    fun check(item: ItemStack, mat: Material, name: String?, lore: List<String>): Boolean {
        if (item.type !== mat) return false
        val meta = item.itemMeta
        if (name != null && meta != null && mmLegacy.serialize(meta.displayName() ?: Component.empty()) != name) return false
        if (lore.isNotEmpty() && meta != null) {
            if (!meta.hasLore()) return false
            val itemLore = meta.lore() ?: return false
            if (lore.size != itemLore.size) return false

            for (line in itemLore) {
                val pos = itemLore.indexOf(line)
                if (lore[pos] != mmLegacy.serialize(line)) return false
            }
        }

        return true
    }
}
