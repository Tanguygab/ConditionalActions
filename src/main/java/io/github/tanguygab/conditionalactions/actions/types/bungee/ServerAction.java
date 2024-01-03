package io.github.tanguygab.conditionalactions.actions.types.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.tanguygab.conditionalactions.actions.types.Action;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ServerAction extends Action {

    public ServerAction() {
        super("(?i)server:( )?");
    }

    @Override
    public String getSuggestion() {
        return "server: <server>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(match);
        p.sendPluginMessage(getPlugin(),"BungeeCord",out.toByteArray());
    }
}
