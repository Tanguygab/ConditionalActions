package io.github.tanguygab.conditionalactions.actions.types.storage;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TakeItemStorageAction extends Action {

    public TakeItemStorageAction() {
        super("(i?)take-item-storage:( )?");
    }

    @Override
    public String getSuggestion() {
        return "take-item-storage: <name>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (player == null || player.getPlayer() == null) return;

        String[] args = match.split(" ");
        String name = args[0];
        int amt = args.length > 1 ? parseInt(args[1],1) : 1;

        ItemStack item = new ItemStack(Material.STONE);//ARMenu.get().getItemStorage().getItem(name);
        //if (item == null) return;

        Player p = player.getPlayer();
        ItemStack[] items = p.getInventory().getContents();
        for (int i = 0; i < items.length; i++) {
            if (amt <= 0) break;

            if (item.isSimilar(items[i])) {
                ItemStack found = items[i];
                if (found.getAmount() <= amt) {
                    p.getInventory().setItem(i,null);
                    amt = amt-found.getAmount();
                }
                else if (found.getAmount() > amt) {
                    found.setAmount(found.getAmount()-amt);
                    amt = 0;
                }
            }
        }
    }

}
