package io.github.tanguygab.conditionalactions.actions;

import io.github.tanguygab.conditionalactions.conditions.ConditionGroup;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@AllArgsConstructor
public class ActionCondition implements CAExecutable {

    private final ConditionGroup condition;
    private final ActionGroup success;
    private final ActionGroup deny;

    @Override
    public void execute(@Nullable OfflinePlayer player, Map<String, String> replacements) {
        if (condition.isMet(player,replacements)) {
            if (success != null) success.execute(player,replacements);
            return;
        }
        if (deny != null) deny.execute(player,replacements);
    }

}
