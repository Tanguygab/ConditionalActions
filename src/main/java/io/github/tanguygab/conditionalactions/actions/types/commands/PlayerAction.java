package io.github.tanguygab.conditionalactions.actions.types.commands;

import io.github.tanguygab.conditionalactions.actions.types.Action;
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
        getPlugin().getScheduler().entity(p).run(()->p.performCommand(command));
    }

}
