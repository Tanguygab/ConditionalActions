package io.github.tanguygab.conditionalactions.actions.types.messages;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BroadcastTitleAction extends Action {

    public BroadcastTitleAction() {
        super("^(?i)(broadcast|bc)-title:( )?");
    }

    @Override
    public String getSuggestion() {
        return getSuggestionWithArgs("broadcast-title: <title>","<subtitle>","<fadein>","<stay>","<fadeout>");
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        match = parsePlaceholders(player, match, true);
        String[] matches = split(match);

        String title = matches[0];
        String subtitle = "";
        int fadein = 5;
        int stay = 5;
        int fadeout = 5;
        if (matches.length > 1) subtitle = matches[1];
        if (matches.length > 2) fadein = parseInt(matches[2]);
        if (matches.length > 3) stay = parseInt(matches[3]);
        if (matches.length > 4) fadeout = parseInt(matches[4]);

        for (Player p : Bukkit.getServer().getOnlinePlayers())
            p.sendTitle(title,subtitle,fadein,stay,fadeout);
    }

    private int parseInt(String str) {
        return parseInt(str,5);
    }
}
