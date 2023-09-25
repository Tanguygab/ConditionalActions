package io.github.tanguygab.conditionalactions.actions.commands;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;

public class GroupAction extends Action {
    public GroupAction() {
        super("(?i)group:( )?");
    }

    @Override
    public String getSuggestion() {
        return "group: <action group>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        match = parsePlaceholders(player,match);
        getPlugin().getActionManager().findAndExecute(player,match,true);
    }
}
