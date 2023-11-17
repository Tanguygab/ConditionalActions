package io.github.tanguygab.conditionalactions.conditions;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.conditions.types.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
            public boolean isMet(OfflinePlayer p) {
                return p != null && p.getPlayer() != null && p.getPlayer().hasPermission(rightSide);
            }
        });
    }};

    @SuppressWarnings("unchecked")
    public ConditionManager(ConditionalActions plugin) {
        File file = new File(plugin.getDataFolder(),"conditions.yml");
        if (!file.exists()) plugin.saveResource("conditions.yml",false);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection conditionsSection = config.getConfigurationSection("conditions");
        if (conditionsSection != null) conditionsSection.getValues(false).forEach((name,cfg)->{
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
                builder.delete(builder.length()-2,builder.length());

            conditions.put(name,new ConditionGroup(this,builder.toString()));
        });


        ConfigurationSection outputsSection = config.getConfigurationSection("conditional-outputs");
        if (outputsSection != null) outputsSection.getValues(false).forEach((name,cfg)->{
            if (cfg instanceof ConfigurationSection section)
                conditionalOutputs.put(name,new ConditionalOutput(this,section));
        });
    }

    public ConditionGroup getCondition(String condition) {
        return conditions.computeIfAbsent(condition,k->new ConditionGroup(this,condition));
    }

    public ConditionalOutput getConditionalOutput(String name) {
        return conditionalOutputs.get(name);
    }

    public ConditionType find(String condition) {
        for (String separator : types.keySet())
            if (condition.contains(separator))
                return types.get(separator).apply(condition);
        return null;
    }
}
