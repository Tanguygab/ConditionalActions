package io.github.tanguygab.conditionalactions.actions.types.storage;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class GiveItemStorageAction extends Action {

    public GiveItemStorageAction() {
        super("^(i?)give-item-storage:( )?");
    }

    @Override
    public String getSuggestion() {
        return "give-item-storage: <name>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (player == null || player.getPlayer() == null) return;

        String[] args = match.split(" ");
        //String name = args[0];
        int amt = args.length > 1 ? parseInt(args[1],1) : 1;

        ItemStack item = new ItemStack(Material.STONE);//ARMenu.get().getItemStorage().getItem(name);
        //if (item == null) return;

        for (int i = 0; i < amt; i++)
            player.getPlayer().getInventory().addItem(item);

    }

}
