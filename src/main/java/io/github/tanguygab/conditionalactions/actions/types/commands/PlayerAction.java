package io.github.tanguygab.conditionalactions.actions.types.commands;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerAction extends Action {

    public PlayerAction() {
        super("^(?i)(player|cmd|command):( )?");
    }

    @Override
    public String getSuggestion() {
        return "player: <command>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        String command = parsePlaceholders(player,match);

        if (player instanceof Player p) {
            getPlugin().sync(p, () -> p.performCommand(command));
            return;
        }
        getPlugin().sync(null, () -> getPlugin().getServer().dispatchCommand(getPlugin().getServer().getConsoleSender(),command));


    }

}
