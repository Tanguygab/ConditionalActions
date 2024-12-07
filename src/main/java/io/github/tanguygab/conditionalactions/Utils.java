package io.github.tanguygab.conditionalactions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class Utils {

    public static OfflinePlayer getOfflinePlayer(String name) {
        Player p = Bukkit.getServer().getPlayerExact(name);
        if (p != null) return p;
        return Arrays.stream(Bukkit.getServer().getOfflinePlayers()).filter(player->name.equalsIgnoreCase(player.getName())).findFirst().orElse(null);
    }

    public static String colors(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }

    public static void updateFiles(ConditionalActions plugin, String oldPath, String newPath) {
        File file = new File(plugin.getDataFolder(),newPath);
        File oldFile = new File(plugin.getDataFolder(), oldPath);
        File folder = new File(plugin.getDataFolder(), newPath.split("/")[0]);
        if (!folder.exists()) folder.mkdir();

        if (!file.exists() && (!oldFile.exists() || !oldFile.renameTo(file))) {
            plugin.saveResource(newPath, false);
        }

    }

    public static void loadFiles(ConditionalActions plugin, String path, BiConsumer<String, Object> callable) {
        File folder = new File(plugin.getDataFolder(), path);

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                loadFiles(plugin, path + "/" + file.getName(), callable);
                continue;
            }
            if (!file.getName().endsWith(".yml")) return;

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.getValues(false).forEach(callable);
        }
    }

}
