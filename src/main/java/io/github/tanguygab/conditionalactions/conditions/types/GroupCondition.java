package io.github.tanguygab.conditionalactions.conditions.types;

import io.github.tanguygab.conditionalactions.conditions.ConditionGroup;
import org.bukkit.OfflinePlayer;

public class GroupCondition extends ConditionType {

    private final ConditionGroup group;

    public GroupCondition(ConditionGroup group, String... array) {
        super(array);
        this.group = group;
    }

    @Override
    public boolean isMet(OfflinePlayer player) {
        return group.isMet(player);
    }
}
