package io.github.tanguygab.conditionalactions

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

import java.io.File

object Utils {
    fun getOfflinePlayer(name: String): OfflinePlayer? {
        val p = Bukkit.getServer().getPlayerExact(name)
        return p ?: Bukkit.getServer().getOfflinePlayerIfCached(name)

    }

    fun updateFiles(plugin: ConditionalActions, oldPath: String, newPath: String) {
        val file = File(plugin.dataFolder, newPath)
        val oldFile = File(plugin.dataFolder, oldPath)
        val folder = File(plugin.dataFolder, newPath.split("/")[0])
        if (!folder.exists()) folder.mkdir()

        if (!file.exists() && (!oldFile.exists() || !oldFile.renameTo(file))) {
            plugin.saveResource(newPath, false)
        }
    }

    fun loadFiles(file: File, path: String, callable: (File, String) -> Unit) {
        if (file.isDirectory) {
            file.listFiles().forEach { loadFiles(it, "$path${file.name}/", callable) }
            return
        }
        if (!file.name.endsWith(".yml")) return
        callable(file, path)
    }
}
