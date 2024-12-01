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
    public boolean execute(@Nullable OfflinePlayer player, Map<String, String> replacements) {
        if (condition.isMet(player,replacements)) {
            if (success != null) return success.execute(player,replacements);
            return true;
        }
        if (deny != null) return deny.execute(player,replacements);
        return true;
    }

}
