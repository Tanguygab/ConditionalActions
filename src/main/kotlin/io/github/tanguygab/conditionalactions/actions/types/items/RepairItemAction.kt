package io.github.tanguygab.conditionalactions.actions.types.items

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.Damageable

class RepairItemAction : Action("^(i?)repair-item:( )?") {
    override val suggestion = "repair-item: <slot>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return

        var slot = parseInt(match, -1)
        val inv = player.inventory
        if (slot == -1) slot = inv.heldItemSlot

        val item = inv.getItem(slot)
        if (item == null || item.type.isAir) return
        val meta = item.itemMeta
        if (meta is Damageable && meta.hasDamage()) {
            meta.damage = 0
            item.itemMeta = meta
        }
    }
}
