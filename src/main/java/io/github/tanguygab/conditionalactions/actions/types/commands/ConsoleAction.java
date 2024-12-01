package io.github.tanguygab.conditionalactions.actions.types.commands;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.OfflinePlayer;

public class ConsoleAction extends Action {

    public ConsoleAction() {
        super("(?i)console:( )?");
    }

    @Override
    public String getSuggestion() {
        return "console: <command>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        String command = parsePlaceholders(player,match);
        getPlugin().getScheduler().global().run(()-> getPlugin().getServer().dispatchCommand(getPlugin().getServer().getConsoleSender(),command));
    }

}
