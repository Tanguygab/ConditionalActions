package io.github.tanguygab.conditionalactions;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class CAExpansion extends PlaceholderExpansion {

    private final ConditionalActions pl = ConditionalActions.getInstance();

    private final String identifier = "conditionalactions";
    private final String author = "Tanguygab";
    private final String version = pl.getDescription().getVersion();

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] args = params.split("_");
        if (args.length < 2) return null;
        String arg = args[0];
        params = params.substring(args.length+1);
        return switch (arg) {
            case "data" -> pl.getDataManager().getData(player,params);
            case "global-data" -> pl.getDataManager().getGlobalData(params);
            case "condition" -> pl.getConditionManager().isMet(params) ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
            default -> null;
        };
    }
}
