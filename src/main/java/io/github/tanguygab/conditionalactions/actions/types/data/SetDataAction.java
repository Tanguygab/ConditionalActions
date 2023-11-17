package io.github.tanguygab.conditionalactions.actions.types.data;

import io.github.tanguygab.conditionalactions.DataManager;
import io.github.tanguygab.conditionalactions.Utils;
import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.OfflinePlayer;

public class SetDataAction extends Action {

    public SetDataAction() {
        super("(?i)set-data: ");
    }

    @Override
    public String getSuggestion() {
        return "set-data: <player|--global> <data> <value>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        String[] args = match.split(" ");
        if (args.length < 3) return;
        String name = args[0];
        String data = args[1];
        String value = args[2];
        DataManager manager = getPlugin().getDataManager();
        if (manager.isGlobal(name)) {
            manager.setGlobalData(data,value);
            return;
        }
        player = Utils.getOfflinePlayer(name);
        if (player != null) manager.setData(player,data,value);
    }
}
