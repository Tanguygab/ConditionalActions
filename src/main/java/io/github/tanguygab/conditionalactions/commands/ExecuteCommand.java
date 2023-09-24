package io.github.tanguygab.conditionalactions.commands;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ExecuteCommand extends CACommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("You need to provide a player!");
            return;
        }
        if (args.length < 3) {
            sender.sendMessage("You need to provide an action!");
            return;
        }
        String name = args[0];
        @SuppressWarnings("deprecation")
        OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(name);

        String action = String.join(" ", args).substring(name.length()+1);

        plugin.getActionManager().findAndExecute(p,action);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length < 3) return null;
        String arg = String.join(" ", Arrays.copyOfRange(args,1, args.length));
        Action action = plugin.getActionManager().find(arg);
        return action == null ? null : List.of(action.getSuggestion());
    }
}
