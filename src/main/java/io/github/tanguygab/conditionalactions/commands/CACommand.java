package io.github.tanguygab.conditionalactions.commands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class CACommand {

    protected final ConditionalActions plugin = ConditionalActions.getInstance();

    public abstract void onCommand(CommandSender sender, String[] args);
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);
}
