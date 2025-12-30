package io.github.tanguygab.conditionalactions.actions.types.items

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class EnchantItemAction(private val type: String) : Action("^(i?)$type-enchant-item:( )?".toRegex()) {
    override fun getSuggestion() =
        "$type-enchant-item: <slot> <enchant> " + (if (type == "set") "<level>" else "[level]")

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return

        val args = match.split(" ")
        if (args.size < 2) return

        val inv = player.inventory
        var slot = parseInt(args[0], -1)
        if (slot == -1) slot = inv.heldItemSlot

        val item = inv.getItem(slot)
        if (item == null || item.type.isAir) return

        val enchant = args[1]
        val enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchant.lowercase())) ?: return
        val lvl = parseInt(args[2], 1)

        enchant(item, enchantment, lvl)
    }

    fun enchant(item: ItemStack, enchant: Enchantment, lvl: Int) {
        val oldLvl = item.getEnchantmentLevel(enchant)
        when (type) {
            "set" -> {
                if (lvl <= 0) item.removeEnchantment(enchant)
                else item.addUnsafeEnchantment(enchant, lvl)
            }

            "add" -> item.addUnsafeEnchantment(enchant, oldLvl + lvl)
            "take" -> {
                val newLvl = oldLvl - lvl
                if (newLvl <= 0) item.removeEnchantment(enchant)
                else item.addUnsafeEnchantment(enchant, newLvl)
            }
        }
    }
}
