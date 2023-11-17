package io.github.tanguygab.conditionalactions.conditions.types;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public abstract class ConditionType {

    protected final String leftSide;
    protected final String rightSide;

    public ConditionType(String[] input) {
        leftSide = input[0];
        rightSide = input.length > 1 ? input[1] : "";
    }

    protected String parse(OfflinePlayer player, String string) {
        return PlaceholderAPI.setPlaceholders(player,string);
    }

    protected String parseLeft(OfflinePlayer player) {
        return parse(player, leftSide);
    }
    protected String parseRight(OfflinePlayer player) {
        return parse(player, rightSide);
    }

    public abstract boolean isMet(OfflinePlayer player);

}
