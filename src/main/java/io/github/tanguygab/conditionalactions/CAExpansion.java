package io.github.tanguygab.conditionalactions;

import io.github.tanguygab.conditionalactions.conditions.ConditionalOutput;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CAExpansion extends PlaceholderExpansion {

    private final ConditionalActions pl = ConditionalActions.getInstance();

    @Getter private final String identifier = "conditionalactions";
    @Getter private final String author = "Tanguygab";
    @Getter private final String version = pl.getDescription().getVersion();

    private final String TRUE = PlaceholderAPIPlugin.booleanTrue();
    private final String FALSE = PlaceholderAPIPlugin.booleanFalse();

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] args = params.split("_");
        if (args.length < 2) return null;
        String arg = args[0];
        params = params.substring(arg.length()+1);
        return switch (arg) {
            case "data" -> pl.getDataManager().getData(player,params);
            case "global-data" -> pl.getDataManager().getGlobalData(params);
            case "condition" -> pl.getConditionManager().getCondition(params).isMet(player) ? TRUE : FALSE;
            case "output" -> {
                ConditionalOutput output = pl.getConditionManager().getConditionalOutput(params);
                yield output == null ? "" : output.getOutput(player);
            }
            default -> null;
        };
    }
}
