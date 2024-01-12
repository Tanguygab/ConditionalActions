package io.github.tanguygab.conditionalactions.customcommands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

public class CommandListener implements Listener {

    private final CustomCommandManager manager;

    public CommandListener(CustomCommandManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String[] args = e.getMessage().split(" ");
        String cmd = args[0].toLowerCase().substring(1);
        CustomCommand command = manager.getCommands().get(cmd);
        if (command != null && command.isForce()) {
            e.setCancelled(true);
            command.execute(e.getPlayer(),cmd,Arrays.copyOfRange(args,1,args.length));
        }
    }
}
