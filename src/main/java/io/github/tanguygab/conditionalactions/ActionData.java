package io.github.tanguygab.conditionalactions;

import io.github.tanguygab.conditionalactions.actions.Action;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

@AllArgsConstructor
public class ActionData {

    private final Action action;
    private final String arguments;

    public void execute(OfflinePlayer player) {
        String args = arguments;
        if (action.replaceMatch())
            args = args.replaceAll(action.getPattern().pattern(),"");

        if (!Bukkit.isPrimaryThread()) {
            action.execute(player, args);
            return;
        }
        String finalArgs = args;
        ConditionalActions.getInstance().async(()->action.execute(player, finalArgs));
    }

}
