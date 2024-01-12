package io.github.tanguygab.conditionalactions.conditions.types;

import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.function.BiFunction;

public class NumericCondition extends ConditionType {

    private boolean leftSideStatic;
    private double leftSideValue;

    private boolean rightSideStatic;
    private double rightSideValue;

    private final BiFunction<Double, Double, Boolean> function;

    public NumericCondition(String[] arr, BiFunction<Double, Double, Boolean> function) {
        super(arr);
        this.function = function;
        try {
            leftSideValue = Double.parseDouble(leftSide);
            leftSideStatic = true;
        } catch (NumberFormatException ignored) {}
        try {
            rightSideValue = Double.parseDouble(rightSide);
            rightSideStatic = true;
        } catch (NumberFormatException ignored) {}
    }

    private double get(String value) {
        try {return Double.parseDouble(value.replace(",", ""));}
        catch (Exception e) {return 0;}
    }

    public double getLeft(OfflinePlayer player, Map<String, String> replacements) {
        if (leftSideStatic) return leftSideValue;
        return get(parseLeft(player,replacements));
    }

    public double getRight(OfflinePlayer player, Map<String, String> replacements) {
        if (rightSideStatic) return rightSideValue;
        return get(parseRight(player,replacements));
    }

    @Override
    public boolean isMet(OfflinePlayer p, Map<String, String> replacements) {
        return function.apply(getLeft(p,replacements), getRight(p,replacements));
    }
}