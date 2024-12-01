package io.github.tanguygab.conditionalactions.actions.types.messages;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ChatAction extends Action {

    public ChatAction() {
        super("^(?i)chat:( )?");
    }

    @Override
    public String getSuggestion() {
        return "chat: <message>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;
        String message = parsePlaceholders(p,match);
        getPlugin().getScheduler().entity(p).run(()->p.chat(message));
    }
}
