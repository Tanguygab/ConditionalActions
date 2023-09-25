package io.github.tanguygab.conditionalactions.actions;

import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;

@AllArgsConstructor
public class ActionCondition {

    private final String condition;
    private final ActionGroup success;
    private final ActionGroup deny;

    public void execute(OfflinePlayer player) {
        if (condition.equals("yes")) { // testing since I didn't make conditions yet
            if (success != null) success.execute(player);
            return;
        }
        if (deny != null) deny.execute(player);
    }

}
