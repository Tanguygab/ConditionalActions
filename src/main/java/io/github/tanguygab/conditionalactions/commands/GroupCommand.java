package io.github.tanguygab.conditionalactions.commands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupCommand extends CACommand {

    public GroupCommand(ConditionalActions plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendMessage(sender,"&7Use either &8list &7or &8execute&7.");
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            sendMessage(sender,"Groups:\n&7"+String.join(", ",plugin.getActionManager().getGroups()));
            return;
        }
        if (!args[0].equalsIgnoreCase("execute")) {
            sendMessage(sender,"&7Use either &8list &7or &8execute&7.");
            return;
        }

        args = Arrays.copyOfRange(args,1,args.length);
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

        plugin.getActionManager().findAndExecute(p,action,true);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length < 1 || args[0].equalsIgnoreCase("list")) return List.of();
        if (args.length < 2 || !args[0].equalsIgnoreCase("execute")) return null;
        return new ArrayList<>(plugin.getActionManager().getGroups());
    }
}
