package io.github.tanguygab.conditionalactions.actions;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.actions.types.Action;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;

import java.util.Map;

@AllArgsConstructor
public class ActionData implements CAExecutable {

    private final Action action;
    private final String arguments;

    @Override
    public boolean execute(OfflinePlayer player, Map<String, String> replacements) {
        String args = ConditionalActions.parseReplacements(arguments,replacements);

        if (action.replaceMatch())
            args = args.replaceAll(action.getPattern().pattern(),"");

        String finalArgs = args;
        action.execute(player,finalArgs);
        return true;
    }

}
