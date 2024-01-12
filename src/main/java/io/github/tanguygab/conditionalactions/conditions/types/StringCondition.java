package io.github.tanguygab.conditionalactions.conditions.types;

import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.function.BiFunction;

public class StringCondition extends ConditionType {

    private final BiFunction<String, String, Boolean> function;

    public StringCondition(String[] input, BiFunction<String,String,Boolean> function) {
        super(input);
        this.function = function;
    }

    @Override
    public boolean isMet(OfflinePlayer p, Map<String, String> replacements) {
        return function.apply(parseLeft(p,replacements), parseRight(p,replacements));
    }

}
