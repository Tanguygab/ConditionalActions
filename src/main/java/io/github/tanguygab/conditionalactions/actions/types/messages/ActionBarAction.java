package io.github.tanguygab.conditionalactions.actions.types.messages;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ActionBarAction extends Action {

    public ActionBarAction() {
        super("^(?i)(actionbar):( )?");
    }

    @Override
    public String getSuggestion() {
        return "actionbar: <message>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;
        match = parsePlaceholders(p, match, true);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(match));
    }
}
