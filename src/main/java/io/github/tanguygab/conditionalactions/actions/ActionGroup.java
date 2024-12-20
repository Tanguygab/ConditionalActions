package io.github.tanguygab.conditionalactions.actions;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.actions.types.Action;
import io.github.tanguygab.conditionalactions.conditions.ConditionGroup;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionGroup implements CAExecutable {

    private final ActionManager manager;
    private final List<CAExecutable> actions = new ArrayList<>();

    public ActionGroup(ActionManager manager, List<?> config) {
        this.manager = manager;
        config.forEach(action->{
            if (action instanceof Map<?,?> map) {
                ConditionGroup condition = ConditionalActions.getInstance().getConditionManager().getCondition((String) map.get("condition"));
                actions.add(new ActionCondition(condition,loadConditionList(map,"success"),loadConditionList(map,"deny")));
            } else add(actions,(String) action);
        });
    }

    private ActionGroup loadConditionList(Map<?,?> map, String name) {
        if (!map.containsKey(name)) return null;
        Object obj = map.get(name);
        List<?> list = obj instanceof List<?> l ? l : List.of(obj);
        return new ActionGroup(manager,list);
    }

    private void add(List<CAExecutable> list, String line) {
        if (line.equalsIgnoreCase("return")) {
            list.add(null);
            return;
        }
        Action ac = manager.find(line);
        if (ac != null) list.add(new ActionData(ac,line));
    }

    @Override
    public boolean execute(OfflinePlayer player, Map<String, String> replacements) {
        for (CAExecutable executable : actions) {
            if (executable == null || !executable.execute(player, replacements)) return false;
        }
        return true;
    }

}
