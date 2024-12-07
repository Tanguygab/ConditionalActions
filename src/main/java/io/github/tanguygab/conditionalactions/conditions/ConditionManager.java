package io.github.tanguygab.conditionalactions.conditions;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.Utils;
import io.github.tanguygab.conditionalactions.conditions.types.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.function.Function;

public class ConditionManager {

    private final Map<String, ConditionGroup> conditions = new HashMap<>();
    private final Map<String, ConditionalOutput> conditionalOutputs = new HashMap<>();

    private final Map<String, Function<String,ConditionType>> types = new LinkedHashMap<>() {{
        put(">=", input -> new NumericCondition(input.split(">="), (left, right) -> left >= right));
        put(">", input -> new NumericCondition(input.split(">"), (left, right) -> left > right));
        put("<=", input -> new NumericCondition(input.split("<="), (left, right) -> left <= right));

        put("<-", input -> new StringCondition(input.split("<-"), String::contains));
        put("-|", input->new StringCondition(input.split("-\\|"), String::endsWith));
        put("|-", input->new StringCondition(input.split("\\|-"), String::startsWith));
        put("==", input->new StringCondition(input.split("=="), String::equals));
        put("=", input->new StringCondition(input.split("="), String::equalsIgnoreCase));

        put("permission:", input->new ConditionType(input.split("permission:")) {
            @Override
            public boolean isMet(OfflinePlayer p, Map<String, String> replacements) {
                String permission = ConditionalActions.parseReplacements(rightSide,replacements);
                return p != null && p.getPlayer() != null && p.getPlayer().hasPermission(permission);
            }
        });
    }};

    @SuppressWarnings("unchecked")
    public ConditionManager(ConditionalActions plugin) {
        Utils.updateFiles(plugin, "conditions.yml","conditions/default-conditions.yml");

        Utils.loadFiles("conditions", (key, value) -> {
            if (!(value instanceof ConfigurationSection section)) return;

            if (key.equalsIgnoreCase("conditions")) {
                section.getValues(false).forEach((name,cfg)->{
                    StringBuilder builder = new StringBuilder();
                    if (cfg instanceof List<?> list) {
                        list.forEach(obj -> {
                            if (obj instanceof List<?> andConditions)
                                builder.append(String.join("&&", (List<String>) andConditions));
                            else builder.append(obj);
                            builder.append("||");
                        });
                    } else builder.append(cfg);
                    if (builder.length() >= 2 && builder.substring(builder.length()-2).equals("||"))
                        builder.delete(builder.length()-2, builder.length());

                    conditions.put(name, new ConditionGroup(this, builder.toString()));
                });
                return;
            }
            if (key.equalsIgnoreCase("conditional-outputs")) {
                section.getValues(false).forEach((name,cfg)->{
                    if (cfg instanceof ConfigurationSection subsection)
                        conditionalOutputs.put(name, new ConditionalOutput(this, subsection));
                });
            }
        });
    }

    public ConditionGroup getCondition(String condition) {
        return conditions.computeIfAbsent(condition,k->new ConditionGroup(this,condition));
    }

    public ConditionalOutput getConditionalOutput(String name) {
        return conditionalOutputs.get(name);
    }

    public ConditionType find(String condition) {
        String condition0 = condition.startsWith("!") ? condition.substring(1) : condition;
        if (conditions.containsKey(condition0))
            return new GroupCondition(conditions.get(condition0),condition.startsWith("!") ? "!" : "");

        for (String separator : types.keySet())
            if (condition.contains(separator))
                return types.get(separator).apply(condition);
        return null;
    }

    public Set<String> getConditions() {
        return conditions.keySet();
    }
}
