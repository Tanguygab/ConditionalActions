package io.github.tanguygab.conditionalactions.customcommands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.actions.ActionGroup;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomCommand extends BukkitCommand {

    private final ActionGroup actions;

    public CustomCommand(@NotNull String name, List<String> aliases, ActionGroup actions) {
        super(name);
        if (aliases != null) setAliases(aliases);
        this.actions = actions;

    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (actions != null) ConditionalActions.getInstance().async(()->actions.execute(sender instanceof OfflinePlayer player ? player : null));
        return true;
    }
}
