package io.github.tanguygab.conditionalactions;

import io.github.tanguygab.conditionalactions.actions.ActionManager;
import io.github.tanguygab.conditionalactions.commands.CACommand;
import io.github.tanguygab.conditionalactions.commands.ExecuteCommand;
import io.github.tanguygab.conditionalactions.conditions.ConditionManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class ConditionalActions extends JavaPlugin {

    @Getter private static ConditionalActions instance;
    @Getter(AccessLevel.PRIVATE) private final Map<String, CACommand> subcommands = new HashMap<>();

    private DataManager dataManager;
    private ActionManager actionManager;
    private ConditionManager conditionManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();

        dataManager = new DataManager();
        actionManager = new ActionManager(getConfig().getString("argument-separator",","));
        conditionManager = new ConditionManager();

        subcommands.put("execute",new ExecuteCommand());
    }

    @Override
    public void onDisable() {
        subcommands.clear();
    }

    public void async(Runnable run) {
        getServer().getScheduler().runTaskAsynchronously(this,run);
    }
    public void sync(Runnable run) {
        getServer().getScheduler().runTask(this,run);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String arg = args.length > 0 ? args[0] : "";
        args = Arrays.copyOfRange(args,1, args.length);
        if (subcommands.containsKey(arg)) {
            subcommands.get(arg).onCommand(sender,args);
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        String arg = args.length > 0 ? args[0] : "";
        args = Arrays.copyOfRange(args,1, args.length);
        return subcommands.containsKey(arg) ? subcommands.get(arg).onTabComplete(sender,args) : null;
    }
}
