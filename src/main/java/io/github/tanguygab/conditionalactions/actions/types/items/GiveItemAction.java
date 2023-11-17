package io.github.tanguygab.conditionalactions.actions.types.items;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GiveItemAction extends Action {

    public GiveItemAction() {
        super("(i?)give-item:( )?");
    }

    @Override
    public String getSuggestion() {
        return "give-item: <material> <amount> <name\\nlore>";
    }

    @Override
    public void execute(OfflinePlayer player,String match) {
        if (!(player instanceof Player p)) return;
        String[] args = match.split(" ");

        String material = args[0].toUpperCase();
        Material mat = Material.getMaterial(material);
        if (mat == null) return;
        ItemStack item = new ItemStack(mat);

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        if (args.length > 1) item.setAmount(parseInt(parsePlaceholders(p,args[1]),1));
        if (args.length > 2) {
            args = Arrays.copyOfRange(args,2,args.length);
            args = String.join(" ",args).split("\\n");
            meta.setDisplayName(parsePlaceholders(p,args[0]));

            if (args.length > 1) {
                List<String> lore = new ArrayList<>();
                for (String line : Arrays.copyOfRange(args,1,args.length))
                    lore.add(parsePlaceholders(p, line));
                meta.setLore(lore);
            }
        }
        item.setItemMeta(meta);
        p.getInventory().addItem(item);
    }
}
