package io.github.tanguygab.conditionalactions.actions;

import io.github.tanguygab.conditionalactions.ActionData;
import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.actions.commands.GroupAction;
import io.github.tanguygab.conditionalactions.actions.bungee.ServerAction;
import io.github.tanguygab.conditionalactions.actions.commands.*;
import io.github.tanguygab.conditionalactions.actions.data.*;
import io.github.tanguygab.conditionalactions.actions.items.*;
import io.github.tanguygab.conditionalactions.actions.messages.*;
import io.github.tanguygab.conditionalactions.actions.other.DelayAction;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class ActionManager {

    @Getter private final String argumentSeparator;
    private final List<Action> actions = new ArrayList<>();
    private final Map<String,ActionGroup> actionGroups = new HashMap<>();

    public ActionManager(ConditionalActions plugin, String argumentSeparator) {
        this.argumentSeparator = argumentSeparator;
        register(
                new PlayerAction(),
                new ConsoleAction(),
                new GroupAction(),

                new ServerAction(),

                new RemoveDataAction(),
                new SetDataAction(),

                new EnchantItemAction("add"),
                new EnchantItemAction("set"),
                new EnchantItemAction("take"),
                new GiveItemAction(),
                //new GiveItemStorageAction(),
                new RepairItemAction(),
                new TakeItemAction(),
                //new TakeItemStorageAction(),

                new ActionBarAction(),
                new BroadcastAction(),
                new BroadcastActionBarAction(),
                new BroadcastTitleAction(),
                new ChatAction(),
                new MessageAction(),
                new TitleAction(),

                new DelayAction()
        );
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms"))
            register(new PermissionAction());

        File file = new File(plugin.getDataFolder(),"actiongroups.yml");
        if (!file.exists()) plugin.saveResource("actiongroups.yml",false);
        YamlConfiguration groups = YamlConfiguration.loadConfiguration(file);
        groups.getValues(false).forEach((name,config)->{
            if (config instanceof List<?> list) actionGroups.put(name,new ActionGroup(this,list));
        });
    }

    public void findAndExecute(OfflinePlayer player, String action, boolean group) {
        if (group) {
            ActionGroup g = actionGroups.get(action);
            if (g != null) g.execute(player);
            return;
        }
        Action ac = find(action);
        if (ac == null) return;
        new ActionData(ac,action).execute(player);
    }

    public Action find(String action) {
        for (Action ac : actions) {
            Matcher matcher = ac.getPattern().matcher(action);
            if (matcher.find()) return ac;
        }
        return null;
    }

    public void register(Action... action) {
        actions.addAll(List.of(action));
    }

    public List<String> getSuggestions() {
        return actions.stream().map(Action::getSuggestion).toList();
    }

}
