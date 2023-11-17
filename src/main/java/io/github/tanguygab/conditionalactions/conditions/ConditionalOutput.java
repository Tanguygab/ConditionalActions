package io.github.tanguygab.conditionalactions.conditions;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConditionalOutput {

    private final Map<ConditionGroup,String> outputs = new LinkedHashMap<>();

    public ConditionalOutput(ConditionManager manager, ConfigurationSection section) {
        section.getValues(false).forEach((output,condition)-> outputs.put(manager.getCondition(String.valueOf(condition)), output));
    }

    public String getOutput(OfflinePlayer player) {
        for (ConditionGroup condition : outputs.keySet())
            if (condition.isMet(player))
                return outputs.get(condition);
        return "";
    }

}
