package io.github.tanguygab.conditionalactions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Utils {

    public static OfflinePlayer getOfflinePlayer(String name) {
        Player p = Bukkit.getServer().getPlayerExact(name);
        if (p != null) return p;
        return Arrays.stream(Bukkit.getServer().getOfflinePlayers()).filter(player->name.equalsIgnoreCase(player.getName())).findFirst().orElse(null);
    }

}
