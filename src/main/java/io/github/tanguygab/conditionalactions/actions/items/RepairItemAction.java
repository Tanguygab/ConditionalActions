package io.github.tanguygab.conditionalactions.actions.items;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

public class RepairItemAction extends Action {

    public RepairItemAction() {
        super("(i?)repair-item:( )?");
    }

    @Override
    public String getSuggestion() {
        return "repair-item: <slot>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;

        int slot = parseInt(match,-1);
        PlayerInventory inv = p.getInventory();
        if (slot == -1) slot = inv.getHeldItemSlot();

        ItemStack item = inv.getItem(slot);
        if (item == null || item.getType().isAir()) return;
        if (item.getItemMeta() instanceof Damageable d && d.hasDamage()) {
            d.setDamage(0);
            item.setItemMeta(d);
        }
    }
}
