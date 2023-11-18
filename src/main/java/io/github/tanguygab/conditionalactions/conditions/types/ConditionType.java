package io.github.tanguygab.conditionalactions.conditions.types;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public abstract class ConditionType {

    protected final String leftSide;
    protected final String rightSide;

    @Getter private final boolean inverted;

    public ConditionType(String[] input) {
        inverted = input[0].startsWith("!");
        leftSide = inverted ? input[0].substring(1) : input[0];
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
