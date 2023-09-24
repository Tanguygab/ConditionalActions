package io.github.tanguygab.conditionalactions.actions.commands;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;

public class PlayerAction extends Action {

    public PlayerAction() {
        super("(?i)(player|cmd|command):( )?",true);
    }

    @Override
    public String getSuggestion(String match) {
        return "player: <command>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (player == null || player.getPlayer() == null) return;
        match = parsePlaceholders(player,match);
        String match0 = match;
        sync(()->player.getPlayer().performCommand(match0));
    }

}
