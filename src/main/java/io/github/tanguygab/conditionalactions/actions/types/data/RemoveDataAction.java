package io.github.tanguygab.conditionalactions.actions.types.data;

import io.github.tanguygab.conditionalactions.DataManager;
import io.github.tanguygab.conditionalactions.Utils;
import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.OfflinePlayer;

public class RemoveDataAction extends Action {

    public RemoveDataAction() {
        super("^(?i)remove-data: ");
    }

    @Override
    public String getSuggestion() {
        return "remove-data: <player|--global> <data>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        String[] args = match.split(" ");
        if (args.length < 2) return;
        String name = args[0];
        String data = args[1];
        DataManager manager = getPlugin().getDataManager();
        if (manager.isGlobal(name)) {
            manager.removeGlobalData(data);
            return;
        }
        player = Utils.getOfflinePlayer(name);
        if (player != null) manager.removeData(player,data);
    }
}
