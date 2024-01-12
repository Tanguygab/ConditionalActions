package io.github.tanguygab.conditionalactions.actions;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface CAExecutable {

    default void execute(@Nullable OfflinePlayer player) {
        execute(player, Map.of());
    }

    void execute(@Nullable OfflinePlayer player, Map<String,String> replacements);

}