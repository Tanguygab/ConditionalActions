package io.github.tanguygab.conditionalactions.actions.messages;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ChatAction extends Action {

    public ChatAction() {
        super("(?i)chat:( )?");
    }

    @Override
    public String getSuggestion() {
        return "chat: <message>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;
        String message = parsePlaceholders(p,match);
        sync(()->p.chat(message));
    }
}
