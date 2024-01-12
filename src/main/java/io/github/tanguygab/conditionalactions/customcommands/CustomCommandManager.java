package io.github.tanguygab.conditionalactions.customcommands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import io.github.tanguygab.conditionalactions.actions.ActionGroup;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCommandManager {

    private final Map<String, CustomCommand> commands = new HashMap<>();

    public CustomCommandManager(ConditionalActions plugin) {
        File file = new File(plugin.getDataFolder(),"commands.yml");
        if (!file.exists()) plugin.saveResource("commands.yml",false);
        YamlConfiguration commandsFile = YamlConfiguration.loadConfiguration(file);

        commandsFile.getValues(false).forEach((name,config)->{
            if (config instanceof ConfigurationSection section) {
                List<String> aliases = section.getStringList("aliases");
                ActionGroup actions = new ActionGroup(plugin.getActionManager(),section.getStringList("actions"));
                commands.put(name,new CustomCommand(name,aliases,actions));
            }
        });

        try {
            final Field f = plugin.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            CommandMap commandMap = (CommandMap) f.get(plugin.getServer());
            commands.values().forEach(command -> commandMap.register("conditionalactions",command));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
