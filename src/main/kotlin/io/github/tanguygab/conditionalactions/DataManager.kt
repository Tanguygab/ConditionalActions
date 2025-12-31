package io.github.tanguygab.conditionalactions

import org.bukkit.OfflinePlayer

class DataManager {
    private val data = mutableMapOf<OfflinePlayer, MutableMap<String, String>>()
    private val globalData = mutableMapOf<String, String>()

    fun isGlobal(name: String) = name.equals("--global", ignoreCase = true)

    fun setData(player: OfflinePlayer, data: String, value: String) {
        this.data.computeIfAbsent(player) { mutableMapOf() }[data] = value
    }
    fun removeData(player: OfflinePlayer, data: String) {
        this.data.computeIfAbsent(player) { mutableMapOf() }.remove(data)
    }

    fun setGlobalData(data: String, value: String) = globalData.put(data, value)
    fun removeGlobalData(data: String) = globalData.remove(data)

    fun getData(player: OfflinePlayer, data: String) = (this.data[player] ?: mutableMapOf())[data] ?: ""
    fun getGlobalData(data: String?) = globalData[data] ?: ""
}