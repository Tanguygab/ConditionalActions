package io.github.tanguygab.conditionalactions.hooks.papi;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PAPIExpansion extends PlaceholderExpansion {

    private final String TRUE = PlaceholderAPIPlugin.booleanTrue();
    private final String FALSE = PlaceholderAPIPlugin.booleanFalse();

    protected final ConditionalActions plugin;

    public PAPIExpansion(ConditionalActions plugin, String identifier) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.version = plugin.getDescription().getVersion();
    }

    @Getter private final String identifier;
    @Getter private final String author = "Tanguygab";
    @Getter private final String version;

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Object output = parse(player,params);
        if (output instanceof Boolean bool) return bool ? TRUE : FALSE;
        return String.valueOf(output);
    }

    public abstract Object parse(OfflinePlayer player, String params);
}
