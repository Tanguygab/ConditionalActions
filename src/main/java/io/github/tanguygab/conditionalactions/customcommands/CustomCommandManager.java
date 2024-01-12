package io.github.tanguygab.conditionalactions.customcommands;

import io.github.tanguygab.conditionalactions.ConditionalActions;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CustomCommandManager {

    private final Map<String, CustomCommand> commands = new HashMap<>();
    private final Map<String, String> aliases = new HashMap<>();

    public CustomCommandManager(ConditionalActions plugin) {
        File file = new File(plugin.getDataFolder(),"commands.yml");
        if (!file.exists()) plugin.saveResource("commands.yml",false);
        YamlConfiguration commandsFile = YamlConfiguration.loadConfiguration(file);

        commandsFile.getValues(false).forEach((name,config)->{
            if (config instanceof ConfigurationSection section) {
                boolean force = section.getBoolean("force",true);
                List<String> aliases = section.getStringList("aliases");
                List<?> actions = section.getList("actions");
                commands.put(name,new CustomCommand(name,force,aliases,actions));
                aliases.forEach(alias->this.aliases.put(alias,name));
            }
        });

        try {
            final Field f = plugin.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            Field mapField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            mapField.setAccessible(true);

            CommandMap commandMap = (CommandMap) f.get(plugin.getServer());
            Map<String, Command> map = (Map<String, Command>) mapField.get(commandMap);
            map.putAll(commands);
            commands.forEach((name,command)->{
                map.put(name,command);
                command.getAliases().forEach(alias->map.put(alias,command));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        plugin.getServer().getPluginManager().registerEvents(new CommandListener(this),plugin);
    }

}
