package io.github.tanguygab.conditionalactions.conditions.types;

import org.bukkit.OfflinePlayer;

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

    public double getLeft(OfflinePlayer player) {
        if (leftSideStatic) return leftSideValue;
        return get(parseLeft(player));
    }

    public double getRight(OfflinePlayer player) {
        if (rightSideStatic) return rightSideValue;
        return get(parseRight(player));
    }

    @Override
    public boolean isMet(OfflinePlayer p) {
        return function.apply(getLeft(p), getRight(p));
    }
}