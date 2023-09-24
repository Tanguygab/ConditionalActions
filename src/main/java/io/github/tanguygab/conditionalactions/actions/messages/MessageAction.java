package io.github.tanguygab.conditionalactions.actions.messages;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MessageAction extends Action {

    public MessageAction() {
        super("(?i)(message|msg|tell):( )?");
    }

    @Override
    public String getSuggestion() {
        return "message: <message>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        match = parsePlaceholders(player,match);
        if (player instanceof Player p) p.sendMessage(match);
        else getPlugin().getServer().getConsoleSender().sendMessage(match);
    }
}
