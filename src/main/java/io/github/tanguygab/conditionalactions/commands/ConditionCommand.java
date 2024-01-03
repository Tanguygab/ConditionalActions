package io.github.tanguygab.conditionalactions.commands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.Utils;
import io.github.tanguygab.conditionalactions.conditions.ConditionGroup;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ConditionCommand extends CACommand {

    public ConditionCommand(ConditionalActions plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendMessage(sender,"&7Use either &8list &7or &8check&7.");
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            sendMessage(sender,"Conditions:\n&7"+String.join(", ",plugin.getConditionManager().getConditions()));
            return;
        }
        if (!args[0].equalsIgnoreCase("check")) {
            sendMessage(sender,"&7Use either &8list &7or &8check&7.");
            return;
        }

        args = Arrays.copyOfRange(args,1,args.length);
        if (args.length < 1) {
            sendMessage(sender,"&cYou need to provide a player!");
            return;
        }
        if (args.length < 2) {
            sendMessage(sender,"&cYou need to provide a condition!");
            return;
        }
        String name = args[0];
        OfflinePlayer p = Utils.getOfflinePlayer(name);
        if (p == null && !name.equalsIgnoreCase("--console")) {
            sendMessage(sender,"&cPlayer not found!");
            return;
        }

        String condition = String.join(" ", args).substring(name.length()+1);

        ConditionGroup group = plugin.getConditionManager().getCondition(condition);
        sendMessage(sender, "&7"+name+" "+(group.isMet(p)
                ? "&ameets the condition!"
                : "&cdoes not meet the condition!"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length < 2) return getArguments(List.of("list","check"),args.length == 0 ? "" : args[0]);
        if (args[0].equalsIgnoreCase("list")) return List.of();
        if (!args[0].equalsIgnoreCase("check")) return null;
        return args.length > 2 ? getArguments(plugin.getConditionManager().getConditions(),args[2]) : null;
    }
}
