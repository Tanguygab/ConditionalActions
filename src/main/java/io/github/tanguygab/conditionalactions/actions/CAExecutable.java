package io.github.tanguygab.conditionalactions.actions;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public interface CAExecutable {

    void execute(@Nullable OfflinePlayer player);

}