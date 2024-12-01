package io.github.tanguygab.conditionalactions.actions;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.actions.types.Action;
import io.github.tanguygab.conditionalactions.actions.types.commands.GroupAction;
import io.github.tanguygab.conditionalactions.actions.types.bungee.ServerAction;
import io.github.tanguygab.conditionalactions.actions.types.commands.*;
import io.github.tanguygab.conditionalactions.actions.types.data.*;
import io.github.tanguygab.conditionalactions.actions.types.items.*;
import io.github.tanguygab.conditionalactions.actions.types.messages.*;
import io.github.tanguygab.conditionalactions.actions.types.other.DelayAction;
import io.github.tanguygab.conditionalactions.actions.types.storage.*;
import io.github.tanguygab.conditionalactions.events.ActionsRegisterEvent;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;

public class ActionManager {

    @Getter private final String argumentSeparator;
    private final ConditionalActions plugin;
    private final List<Action> actions = new ArrayList<>();
    private final Map<String,ActionGroup> actionGroups = new HashMap<>();

    public ActionManager(ConditionalActions plugin, String argumentSeparator) {
        this.argumentSeparator = argumentSeparator;
        this.plugin = plugin;
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
                new GiveItemStorageAction(),
                new RepairItemAction(),
                new TakeItemAction(),
                new TakeItemStorageAction(),

                new ActionBarAction(),
                new BroadcastAction(),
                new BroadcastActionBarAction(),
                new BroadcastTitleAction(),
                new ChatAction(),
                new MessageAction(),
                new TitleAction(),

                new DelayAction()
        );
        if (plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms"))
            register(new PermissionAction());

        try {
            Player.class.getMethod("sendMessage", Component.class);
            register(new MiniMessageAction());
        } catch (NoSuchMethodException ignored) {}

        plugin.sync(null, this::load);
    }

    public void load() {
        ActionsRegisterEvent event = new ActionsRegisterEvent();
        plugin.getServer().getPluginManager().callEvent(event);
        registerAll(event.getActions());

        File file = new File(plugin.getDataFolder(),"actiongroups.yml");
        if (!file.exists()) plugin.saveResource("actiongroups.yml",false);
        YamlConfiguration groups = YamlConfiguration.loadConfiguration(file);
        groups.getValues(false).forEach((name,config)->{
            if (config instanceof List<?> list) actionGroups.put(name,new ActionGroup(this,list));
        });

        plugin.getCustomCommandManager().getCommands().values().forEach(command->command.loadActions(this));
    }

    public boolean findAndExecute(OfflinePlayer player, String action, boolean group) {
        CAExecutable executable;

        if (!group) {
            Action ac = find(action);
            executable = ac == null ? null : new ActionData(ac,action);
        } else executable = actionGroups.get(action);

        if (executable == null) return false;

        if (plugin.getServer().isPrimaryThread())
            ConditionalActions.getInstance().async(()->executable.execute(player));
        else executable.execute(player);

        return true;
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
    public void registerAll(List<Action> action) {
        actions.addAll(action);
    }

    public List<String> getSuggestions() {
        return actions.stream().map(Action::getSuggestion).toList();
    }

    public Set<String> getGroups() {
        return actionGroups.keySet();
    }
}
