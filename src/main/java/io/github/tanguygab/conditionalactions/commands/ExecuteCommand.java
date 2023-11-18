package io.github.tanguygab.conditionalactions.commands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.Utils;
import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ExecuteCommand extends CACommand {

    public ExecuteCommand(ConditionalActions plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sendMessage(sender,"&cYou need to provide a player!");
            return;
        }
        if (args.length < 2) {
            sendMessage(sender,"&cYou need to provide an action!");
            return;
        }
        String name = args[0];
        OfflinePlayer p = Utils.getOfflinePlayer(name);
        if (p == null && !name.equalsIgnoreCase("--console")) {
            sendMessage(sender,"&cPlayer not found!");
            return;
        }

        String action = String.join(" ", args).substring(name.length()+1);

        if (!plugin.getActionManager().findAndExecute(p,action,false))
            sendMessage(sender,"&cAction not found!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length < 2) return null;
        String arg = String.join(" ", Arrays.copyOfRange(args,2, args.length));
        Action action = plugin.getActionManager().find(arg);
        return action == null ? getArguments(plugin.getActionManager().getSuggestions(),arg) : List.of(action.getSuggestion());
    }
}
