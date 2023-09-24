package io.github.tanguygab.conditionalactions.actions.messages;

import io.github.tanguygab.conditionalactions.actions.Action;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;

public class BroadcastActionBarAction extends Action {

    public BroadcastActionBarAction() {
        super("(?i)((broadcast|bc)-actionbar):( )?");
    }

    @Override
    public String getSuggestion() {
        return "broadcast-actionbar: <message>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        match = parsePlaceholders(player,match);
        BaseComponent[] components = TextComponent.fromLegacyText(match);
        getPlugin().getServer().getOnlinePlayers().forEach(p->p.spigot().sendMessage(ChatMessageType.ACTION_BAR,components));
    }
}
