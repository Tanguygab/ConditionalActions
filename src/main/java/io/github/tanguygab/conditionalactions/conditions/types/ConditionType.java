package io.github.tanguygab.conditionalactions.conditions.types;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.Map;

public abstract class ConditionType {

    protected final String leftSide;
    protected final String rightSide;

    @Getter private final boolean inverted;

    public ConditionType(String[] input) {
        inverted = input[0].startsWith("!");
        leftSide = inverted ? input[0].substring(1) : input[0];
        rightSide = input.length > 1 ? input[1] : "";
    }

    protected String parse(OfflinePlayer player, String string, Map<String, String> replacements) {
        return PlaceholderAPI.setPlaceholders(player,ConditionalActions.parseReplacements(string,replacements));
    }

    protected String parseLeft(OfflinePlayer player, Map<String, String> replacements) {
        return parse(player, leftSide,replacements);
    }
    protected String parseRight(OfflinePlayer player, Map<String, String> replacements) {
        return parse(player, rightSide,replacements);
    }

    public abstract boolean isMet(OfflinePlayer player, Map<String, String> replacements);

}
