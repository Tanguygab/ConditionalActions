package io.github.tanguygab.conditionalactions.actions;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;

import java.util.Map;

@AllArgsConstructor
public class ActionData implements CAExecutable {

    private final Action action;
    private final String arguments;

    @Override
    public void execute(OfflinePlayer player, Map<String, String> replacements) {
        String args = arguments;
        for (String replacement : replacements.keySet())
            args = args.replace(replacement, replacements.get(replacement));

        if (action.replaceMatch())
            args = args.replaceAll(action.getPattern().pattern(),"");

        String finalArgs = args;
        action.execute(player,finalArgs);
    }

}
