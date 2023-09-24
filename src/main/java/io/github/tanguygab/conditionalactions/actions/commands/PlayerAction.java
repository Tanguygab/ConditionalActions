package io.github.tanguygab.conditionalactions.actions.commands;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerAction extends Action {

    public PlayerAction() {
        super("(?i)(player|cmd|command):( )?");
    }

    @Override
    public String getSuggestion() {
        return "player: <command>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;
        String command = parsePlaceholders(player,match);
        sync(()->p.performCommand(command));
    }

}
