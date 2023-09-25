package io.github.tanguygab.conditionalactions.commands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends CACommand {

    public ReloadCommand(ConditionalActions plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("conditionalactions")) {
            sendMessage(sender,"&cYou don't have the permission to do this!");
            return;
        }
        plugin.onDisable();
        plugin.onEnable();
        sendMessage(sender,"&aPlugin reloaded successfully!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
