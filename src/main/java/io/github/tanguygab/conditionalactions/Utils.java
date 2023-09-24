package io.github.tanguygab.conditionalactions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;

public class Utils {

    public static OfflinePlayer getOfflinePlayer(String name) {
        return Arrays.stream(Bukkit.getServer().getOfflinePlayers()).filter(player->name.equalsIgnoreCase(player.getName())).findFirst().orElse(null);
    }

}
