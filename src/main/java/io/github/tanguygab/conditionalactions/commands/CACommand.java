package io.github.tanguygab.conditionalactions.commands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.Utils;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public abstract class CACommand {

    protected final ConditionalActions plugin;

    public abstract void onCommand(CommandSender sender, String[] args);
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    protected void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(Utils.colors(message));
    }

    public List<String> getArguments(Collection<String> suggestions, String arg) {
        return suggestions.stream().filter(suggestion -> suggestion.startsWith(arg.toLowerCase())).toList();
    }
}
