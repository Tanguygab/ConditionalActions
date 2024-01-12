package io.github.tanguygab.conditionalactions.customcommands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.actions.ActionGroup;
import io.github.tanguygab.conditionalactions.actions.ActionManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCommand extends BukkitCommand {

    @Getter private final boolean force;
    private final List<String> actionsList;
    private ActionGroup actions;

    public CustomCommand(@NotNull String name, boolean force, List<String> aliases, List<String> actions) {
        super(name);
        this.force = force;
        if (aliases != null) setAliases(aliases);
        actionsList = actions;
    }

    public void loadActions(ActionManager actionManager) {
        actions = new ActionGroup(actionManager,actionsList);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (actions != null) ConditionalActions.getInstance().async(()-> {
            Map<String,String> replacements = new HashMap<>();
            for (int i = 0; i < args.length; i++) replacements.put("%arg-"+i+"%",args[i]);
            replacements.put("%arg-length%",args.length+"");
            actions.execute(sender instanceof OfflinePlayer player ? player : null,replacements);
        });
        return true;
    }
}
