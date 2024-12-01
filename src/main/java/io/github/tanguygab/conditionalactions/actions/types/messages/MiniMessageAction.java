package io.github.tanguygab.conditionalactions.actions.types.messages;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MiniMessageAction extends Action {

    private final MiniMessage mm = MiniMessage.miniMessage();

    public MiniMessageAction() {
        super("^(?i)(minimessage|mm):( )?");
    }

    @Override
    public String getSuggestion() {
        return "minimessage: <formatted message>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        match = parsePlaceholders(player, match);
        Component component = mm.deserialize(match);

        if (player instanceof Player p) p.sendMessage(component);
        else getPlugin().getServer().getConsoleSender().sendMessage(component);
    }
}
