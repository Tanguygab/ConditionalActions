package io.github.tanguygab.conditionalactions.conditions.types;

import io.github.tanguygab.conditionalactions.conditions.ConditionGroup;
import org.bukkit.OfflinePlayer;

import java.util.Map;

public class GroupCondition extends ConditionType {

    private final ConditionGroup group;

    public GroupCondition(ConditionGroup group, String... array) {
        super(array);
        this.group = group;
    }

    @Override
    public boolean isMet(OfflinePlayer player, Map<String, String> replacements) {
        return group.isMet(player,replacements);
    }
}
