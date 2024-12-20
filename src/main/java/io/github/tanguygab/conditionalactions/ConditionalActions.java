package io.github.tanguygab.conditionalactions;

import io.github.tanguygab.conditionalactions.actions.ActionManager;
import io.github.tanguygab.conditionalactions.commands.*;
import io.github.tanguygab.conditionalactions.conditions.ConditionManager;
import io.github.tanguygab.conditionalactions.customcommands.CustomCommandManager;
import io.github.tanguygab.conditionalactions.hooks.papi.CAExpansion;
import io.github.tanguygab.conditionalactions.hooks.papi.PAPIExpansion;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ConditionalActions extends JavaPlugin {

    @Getter private static ConditionalActions instance;
    private final Map<String, CACommand> subcommands = new HashMap<>();
    private final List<PAPIExpansion> expansions = new ArrayList<>();

    @Getter private DataManager dataManager;
    @Getter private ActionManager actionManager;
    @Getter private ConditionManager conditionManager;
    @Getter private CustomCommandManager customCommandManager;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();

        dataManager = new DataManager();
        conditionManager = new ConditionManager(this);
        customCommandManager = new CustomCommandManager(this);
        actionManager = new ActionManager(this,getConfig().getString("argument-separator",","));

        expansions.add(new CAExpansion(this));
        expansions.add(new PAPIExpansion(this,"condition") {
            @Override
            public Object parse(OfflinePlayer player, String params) {
                return plugin.getConditionManager().getCondition(params).isMet(player);
            }
        });
        expansions.forEach(PAPIExpansion::register);

        subcommands.put("reload",new ReloadCommand(this));
        subcommands.put("execute",new ExecuteCommand(this));
        subcommands.put("group",new GroupCommand(this));
        subcommands.put("condition",new ConditionCommand(this));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        try {
            getServer().getAsyncScheduler().cancelTasks(this);
            getServer().getGlobalRegionScheduler().cancelTasks(this);
        } catch (NoSuchMethodError e) {
            getServer().getScheduler().cancelTasks(this);
        }

        subcommands.clear();

        expansions.forEach(PAPIExpansion::unregister);
        expansions.clear();
    }

    public void async(Runnable run) {
        try {
            getServer().getAsyncScheduler().runNow(this, task -> run.run());
        } catch (NoSuchMethodError e) {
            getServer().getScheduler().runTaskAsynchronously(this, run);
        }
    }

    public void sync(Player player, Runnable run) {
        try {
            if (player == null) getServer().getGlobalRegionScheduler().run(this, task -> run.run());
            else player.getScheduler().run(this, task -> run.run(), null);
        } catch (NoSuchMethodError e) {
            getServer().getScheduler().runTask(this, run);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String arg = args.length > 0 ? args[0] : "";
        if (subcommands.containsKey(arg)) {
            args = Arrays.copyOfRange(args,1, args.length);
            subcommands.get(arg).onCommand(sender,args);
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        String arg = args.length > 0 ? args[0] : "";
        if (!subcommands.containsKey(arg)) return List.of("execute","group","condition","reload");
        args = Arrays.copyOfRange(args,1, args.length);
        return subcommands.get(arg).onTabComplete(sender,args);
    }

    public static String parseReplacements(String string, Map<String, String> replacements) {
        for (String replacement : replacements.keySet())
            string = string.replace(replacement, replacements.get(replacement));
        return string;
    }
}
