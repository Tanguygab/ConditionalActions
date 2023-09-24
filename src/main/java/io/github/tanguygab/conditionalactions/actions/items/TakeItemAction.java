package io.github.tanguygab.conditionalactions.actions.items;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakeItemAction extends Action {

    public TakeItemAction() {
        super("(i?)take-item:( )?");
    }

    @Override
    public String getSuggestion() {
        return "take-item: <material> <amount> <name> <lore>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;

        String[] args = match.split(" ");
        String material = args[0].toUpperCase();

        Material mat = Material.getMaterial(material);
        if (mat == null) return;

        int amt = args.length > 1 ? parseInt(parsePlaceholders(p,args[1]),1) : 99999;
        if (amt <= 0) return;

        String name = null;
        List<String> lore = new ArrayList<>();

        if (args.length > 2) {
            args = Arrays.copyOfRange(args,2,args.length);
            args = String.join(" ",args).split("\\n");
            name = parsePlaceholders(p,args[0]);

            if (args.length > 1)
                for (String line : Arrays.copyOfRange(args,1,args.length))
                    lore.add(parsePlaceholders(p, line));
        }

        PlayerInventory inv = p.getInventory();
        ItemStack[] items = inv.getContents();
        for (int i = 0; i < items.length; i++) {
            if (amt <= 0) break;
            if (!check(items[i],mat,name,lore)) continue;
            ItemStack found = items[i];
            if (found.getAmount() <= amt) {
                inv.setItem(i,null);
                amt = amt-found.getAmount();
                continue;
            }
            if (found.getAmount() > amt) {
                found.setAmount(found.getAmount()-amt);
                amt = 0;
            }
        }
    }

    public boolean check(ItemStack item, Material mat, String name, List<String> lore) {
        if (item == null || item.getType() != mat) return false;
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (name != null && !meta.getDisplayName().equals(name)) return false;
        if (!lore.isEmpty()) {
            if (!meta.hasLore()) return false;
            List<String> itemLore = meta.getLore();
            if (itemLore == null) return false;
            if (lore.size() != itemLore.size()) return false;

            for (String line : itemLore) {
                int pos = itemLore.indexOf(line);
                if (!lore.get(pos).equals(line)) return false;
            }
        }

        return true;
    }

}
