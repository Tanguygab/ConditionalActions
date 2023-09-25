package io.github.tanguygab.conditionalactions.actions.other;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;

public class DelayAction extends Action {

    public DelayAction() {
        super("(?i)delay:( )?");
    }

    @Override
    public String getSuggestion() {
        return "delay: <ms>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        match = parsePlaceholders(player,match);
        try {Thread.sleep(parseInt(match,1000));}
        catch (InterruptedException e) {throw new RuntimeException(e);}
    }
}
