package io.github.tanguygab.conditionalactions.actions.messages;

import io.github.tanguygab.conditionalactions.actions.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TitleAction extends Action {

    public TitleAction() {
        super("(?i)title:( )?");
    }

    @Override
    public String getSuggestion() {
        return getSuggestionWithArgs("title: <title>","<subtitle>","<fadein>","<stay>","<fadeout>");
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;
        match = parsePlaceholders(p,match);
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

        p.sendTitle(title,subtitle,fadein,stay,fadeout);
    }

    private int parseInt(String str) {
        return parseInt(str,5);
    }
}
