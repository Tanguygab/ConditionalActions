package io.github.tanguygab.conditionalactions

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration

import java.io.File

object Utils {
    fun getOfflinePlayer(name: String): OfflinePlayer? {
        val p = Bukkit.getServer().getPlayerExact(name)
        return p ?: Bukkit.getServer().getOfflinePlayerIfCached(name)

    }

    fun colors(string: String) = ChatColor.translateAlternateColorCodes('&', string)

    fun updateFiles(plugin: ConditionalActions, oldPath: String, newPath: String) {
        val file = File(plugin.dataFolder, newPath)
        val oldFile = File(plugin.dataFolder, oldPath)
        val folder = File(plugin.dataFolder, newPath.split("/")[0])
        if (!folder.exists()) folder.mkdir()

        if (!file.exists() && (!oldFile.exists() || !oldFile.renameTo(file))) {
            plugin.saveResource(newPath, false)
        }
    }

    fun loadFiles(
        plugin: ConditionalActions,
        path: String,
        callable: (String, Any) -> Unit
    ) {
        val folder = File(plugin.dataFolder, path)

        for (file in folder.listFiles()) {
            if (file.isDirectory) {
                loadFiles(plugin, path + "/" + file.getName(), callable)
                continue
            }
            if (!file.name.endsWith(".yml")) return

            val config = YamlConfiguration.loadConfiguration(file)
            config.getValues(false).forEach(callable)
        }
    }
}
