package io.github.tanguygab.conditionalactions.hooks.papi;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.conditions.ConditionalOutput;
import org.bukkit.OfflinePlayer;

public class CAExpansion extends PAPIExpansion {

    public CAExpansion(ConditionalActions plugin) {
        super(plugin, "conditionalactions");
    }

    @Override
    public Object parse(OfflinePlayer player, String params) {
        String[] args = params.split("_");
        if (args.length < 2) return null;
        String arg = args[0];
        params = params.substring(arg.length()+1);
        return switch (arg) {
            case "data" -> plugin.getDataManager().getData(player,params);
            case "global-data" -> plugin.getDataManager().getGlobalData(params);
            case "output" -> {
                ConditionalOutput output = plugin.getConditionManager().getConditionalOutput(params);
                yield output == null ? "" : output.getOutput(player);
            }
            default -> null;
        };
    }
}
