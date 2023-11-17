package io.github.tanguygab.conditionalactions.actions.types.messages;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.OfflinePlayer;

public class BroadcastAction extends Action {

    public BroadcastAction() {
        super("(?i)(broadcast|bc):( )?");
    }

    @Override
    public String getSuggestion() {
        return "broadcast: <message>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        String message = parsePlaceholders(player,match);
        getPlugin().getServer().getOnlinePlayers().forEach(p->p.sendMessage(message));
    }
}
