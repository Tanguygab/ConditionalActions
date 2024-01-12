package io.github.tanguygab.conditionalactions.conditions;

import io.github.tanguygab.conditionalactions.conditions.types.ConditionType;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionGroup {

    private final List<List<ConditionType>> conditions = new ArrayList<>();

    private final ConditionManager manager;

    public ConditionGroup(ConditionManager manager, String args) {
        this.manager = manager;
        for (String arg : args.split("\\|\\|")) {
            List<ConditionType> list = new ArrayList<>();

            if (arg.contains("&&"))
                for (String condition : args.split("&&"))
                    add(list,condition);
            else add(list,arg);

            conditions.add(list);
        }
    }

    private void add(List<ConditionType> list, String condition) {
        ConditionType type = manager.find(condition);
        if (type != null) list.add(type);
    }

    public boolean isMet(OfflinePlayer player) {
        return isMet(player,Map.of());
    }

    public boolean isMet(OfflinePlayer player, Map<String, String> replacements) {
        for (List<ConditionType> list : conditions) { // OR
            boolean met = true;
            for (ConditionType condition : list) // AND
                if (condition.isMet(player,replacements) == condition.isInverted())
                    met = false;
            if (met) return true; // if all AND conditions are met, return true
        }
        return false;
    }

}
