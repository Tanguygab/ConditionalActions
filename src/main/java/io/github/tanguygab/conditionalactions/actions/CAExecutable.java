package io.github.tanguygab.conditionalactions.actions;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface CAExecutable {

    default boolean execute(@Nullable OfflinePlayer player) {
        return execute(player, Map.of());
    }

    boolean execute(@Nullable OfflinePlayer player, Map<String,String> replacements);

}