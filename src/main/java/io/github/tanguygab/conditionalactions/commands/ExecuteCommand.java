package io.github.tanguygab.conditionalactions.commands;

import io.github.tanguygab.conditionalactions.Utils;
import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ExecuteCommand extends CACommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("You need to provide a player!");
            return;
        }
        if (args.length < 2) {
            sender.sendMessage("You need to provide an action!");
            return;
        }
        String name = args[0];
        OfflinePlayer p = Utils.getOfflinePlayer(name);
        if (p == null && !name.equalsIgnoreCase("--console")) {
            sender.sendMessage("Player not found");
            return;
        }

        String action = String.join(" ", args).substring(name.length()+1);

        plugin.getActionManager().findAndExecute(p,action,false);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length < 2) return null;
        String arg = String.join(" ", Arrays.copyOfRange(args,1, args.length));
        Action action = plugin.getActionManager().find(arg);
        return action == null ? plugin.getActionManager().getSuggestions() : List.of(action.getSuggestion());
    }
}
