package io.github.tanguygab.conditionalactions;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private final Map<OfflinePlayer,Map<String,String>> data = new HashMap<>();
    private final Map<String,String> globalData = new HashMap<>();

    public DataManager() {
        // load data.yml
    }

    public boolean isGlobal(String name) {
        return name.equalsIgnoreCase("--global");
    }

    public void setData(OfflinePlayer player, String data, String value) {
        this.data.computeIfAbsent(player,k->new HashMap<>()).put(data,value);
    }
    public void removeData(OfflinePlayer player, String data) {
        this.data.computeIfAbsent(player,k->new HashMap<>()).remove(data);
    }

    public void setGlobalData(String data, String value) {
        globalData.put(data,value);
    }
    public void removeGlobalData(String data) {
        globalData.remove(data);
    }

    public String getData(OfflinePlayer player, String data) {
        return this.data.getOrDefault(player,Map.of()).getOrDefault(data,"");
    }
    public String getGlobalData(String data) {
        return globalData.getOrDefault(data,"");
    }
}
