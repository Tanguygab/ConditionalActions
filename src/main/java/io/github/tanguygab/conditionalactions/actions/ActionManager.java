package io.github.tanguygab.conditionalactions;

import io.github.tanguygab.conditionalactions.actions.Action;
import io.github.tanguygab.conditionalactions.actions.bungee.ServerAction;
import io.github.tanguygab.conditionalactions.actions.commands.*;
import io.github.tanguygab.conditionalactions.actions.data.*;
import io.github.tanguygab.conditionalactions.actions.items.*;
import io.github.tanguygab.conditionalactions.actions.messages.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ActionManager {

    private final List<Action> actions = new ArrayList<>();

    public ActionManager() {
        register(
                new PlayerAction(),
                new ConsoleAction(),

                new ServerAction(),

                new RemoveDataAction(),
                new SetDataAction(),
                new SetTempDataAction(),

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
                new TitleAction()
        );
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms"))
            register(new PermissionAction());
    }

    public Action find(String action) {
        for (Action ac : actions) {
            Matcher matcher = ac.getPattern().matcher(action);
            if (matcher.find()) return ac;
        }
        return null;
    }

    public void execute(OfflinePlayer player, String action, Action ac) {
        if (ac == null) return;
        if (ac.replaceMatch())
            action = action.replaceAll(ac.getPattern().pattern(),"");

        if (!Bukkit.isPrimaryThread()) {
            ac.execute(player, action);
            return;
        }
        String finalAction = action;
        ConditionalActions.getInstance().async(()->ac.execute(player, finalAction));
    }

    public void findAndExecute(OfflinePlayer player, String action) {
        Action ac = find(action);
        execute(player,action,ac);
    }

    public void register(Action... action) {
        actions.addAll(List.of(action));
    }
}
