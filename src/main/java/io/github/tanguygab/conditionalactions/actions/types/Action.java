package io.github.tanguygab.conditionalactions.actions.types;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.Utils;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

@Getter
public abstract class Action {

    private final Pattern pattern;
    @Accessors(fluent = true) private final boolean replaceMatch;

    public Action(@Language("RegExp") String regex, boolean replaceMatch) {
        this.pattern = Pattern.compile(regex);
        this.replaceMatch = replaceMatch;
    }

    public Action(@Language("RegExp") String regex) {
        this(regex,true);
    }

    public abstract String getSuggestion();
    public abstract void execute(OfflinePlayer player, String match);

    protected ConditionalActions getPlugin() {
        return ConditionalActions.getInstance();
    }

    protected String parsePlaceholders(OfflinePlayer player, String string) {
        return parsePlaceholders(player, string, false);
    }
    protected String parsePlaceholders(OfflinePlayer player, String string, boolean colors) {
        string = PlaceholderAPI.setPlaceholders(player, string);
        return colors ? Utils.colors(string) : string;
    }
    protected int parseInt(String str, int def) {
        try {return Integer.parseInt(str);}
        catch (Exception e) {return def;}
    }

    protected String getArgumentSeparator() {
        return getPlugin().getActionManager().getArgumentSeparator();
    }
    protected String[] split(String args) {
        return args.split(Pattern.quote(getArgumentSeparator()));
    }
    protected String getSuggestionWithArgs(String... args) {
        return String.join(getArgumentSeparator(),args);
    }
}
