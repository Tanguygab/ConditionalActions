package io.github.tanguygab.conditionalactions.actions;

import io.github.tanguygab.conditionalactions.conditions.ConditionGroup;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;

@AllArgsConstructor
public class ActionCondition {

    private final ConditionGroup condition;
    private final ActionGroup success;
    private final ActionGroup deny;

    public void execute(OfflinePlayer player) {
        if (condition.isMet(player)) {
            if (success != null) success.execute(player);
            return;
        }
        if (deny != null) deny.execute(player);
    }

}
