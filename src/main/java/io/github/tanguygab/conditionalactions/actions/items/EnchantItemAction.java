package io.github.tanguygab.conditionalactions.actions.items;

import io.github.tanguygab.conditionalactions.actions.Action;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
public class EnchantItemAction extends Action {

    private final String type;
    @Getter private final String suggestion;

    public EnchantItemAction(String type) {
        super("(i?)"+type+"-enchant-item:( )?");
        this.type = type;

        suggestion = type+"-enchant-item: <slot> <enchant> "+(type.equals("set") ? "<level>" : "[level]");
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;
        String[] args = match.split(" ");
        if (args.length < 2) return;

        PlayerInventory inv = p.getInventory();
        int slot = parseInt(args[0],-1);
        if (slot == -1) slot = inv.getHeldItemSlot();

        ItemStack item = inv.getItem(slot);
        if (item == null || item.getType().isAir()) return;

        String enchant = args[1];
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchant.toLowerCase()));
        if (enchantment == null) return;
        int lvl = parseInt(args[2],1);

        enchant(item,enchantment,lvl);
    }

    public void enchant(ItemStack item, Enchantment enchant, int lvl) {
        int oldLvl = item.getEnchantmentLevel(enchant);
        switch (type) {
            case "set" -> {
                if (lvl <= 0) item.removeEnchantment(enchant);
                else item.addUnsafeEnchantment(enchant,lvl);
            }
            case "add" -> item.addUnsafeEnchantment(enchant,oldLvl+lvl);
            case "take" -> {
                int newLvl = oldLvl-lvl;
                if (newLvl <= 0) item.removeEnchantment(enchant);
                else item.addUnsafeEnchantment(enchant,newLvl);
            }
        }
    }

}
