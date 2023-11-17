package io.github.tanguygab.conditionalactions.actions;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;

@AllArgsConstructor
public class ActionData implements CAExecutable {

    private final Action action;
    private final String arguments;

    @Override
    public void execute(OfflinePlayer player) {
        String args = arguments;
        if (action.replaceMatch())
            args = args.replaceAll(action.getPattern().pattern(),"");

        String finalArgs = args;
        action.execute(player,finalArgs);
    }

}
