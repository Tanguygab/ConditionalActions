package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
import io.github.tanguygab.conditionalactions.actions.types.bungee.BungeeConsoleAction
import io.github.tanguygab.conditionalactions.actions.types.bungee.BungeePlayerAction
import io.github.tanguygab.conditionalactions.actions.types.items.EnchantItemAction
import io.github.tanguygab.conditionalactions.actions.types.items.GiveItemAction
import io.github.tanguygab.conditionalactions.actions.types.items.RepairItemAction
import io.github.tanguygab.conditionalactions.actions.types.items.TakeItemAction
import io.github.tanguygab.conditionalactions.actions.types.storage.GiveItemStorageAction
import io.github.tanguygab.conditionalactions.actions.types.storage.TakeItemStorageAction
import io.github.tanguygab.conditionalactions.actions.types.bungee.ServerAction
import io.github.tanguygab.conditionalactions.actions.types.commands.*
import io.github.tanguygab.conditionalactions.actions.types.data.*
import io.github.tanguygab.conditionalactions.actions.types.messages.*
import io.github.tanguygab.conditionalactions.actions.types.other.*
import io.github.tanguygab.conditionalactions.events.ActionsRegisterEvent

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration

class ActionManager(private val plugin: ConditionalActions, val argumentSeparator: String) {
    private val actions = mutableListOf<Action>()
    private val actionGroups = mutableMapOf<String, ActionGroup>()

    init {
        register(
            PlayerAction(),
            ConsoleAction(),
            GroupAction(),

            BungeePlayerAction(),
            BungeeConsoleAction(),
            ServerAction(),

            RemoveDataAction(),
            SetDataAction(),

            EnchantItemAction("add"),
            EnchantItemAction("set"),
            EnchantItemAction("take"),
            GiveItemAction(),
            GiveItemStorageAction(),
            RepairItemAction(),
            TakeItemAction(),
            TakeItemStorageAction(),

            ActionBarAction(),
            BroadcastAction(),
            BroadcastActionBarAction(),
            BroadcastTitleAction(),
            ChatAction(),
            MessageAction(),
            TitleAction(),

            PlaceholderAction(),
            LogAction(),
            DelayAction()
        )
        if (plugin.server.pluginManager.isPluginEnabled("LuckPerms")) register(
            PermissionAction(),
            GiveTakePermissionAction("give"),
            GiveTakePermissionAction("take")
        )

        plugin.sync(null) { load() }
    }

    fun load() {
        val event = ActionsRegisterEvent()
        plugin.server.pluginManager.callEvent(event)
        registerAll(event.actions)

        Utils.updateFiles(plugin, "actiongroups.yml", "actiongroups/default-groups.yml")
        Utils.loadFiles(plugin.dataFolder.resolve("actiongroups"), "") { file, _ ->
            YamlConfiguration.loadConfiguration(file).getValues(false).forEach { (name, obj) ->
                if (obj is List<*>) actionGroups[name] = ActionGroup(this, obj)
            }
        }

        plugin.customCommandManager.commands.values.forEach { it.loadActions(this) }
    }

    fun findAndExecute(player: OfflinePlayer?, action: String, group: Boolean): Boolean {
        val executable: CAExecutable?

        if (!group) {
            val ac = find(action)
            executable = if (ac == null) null else ActionData(ac, action)
        } else executable = actionGroups[action]

        if (executable == null) return false

        if (plugin.server.isPrimaryThread) ConditionalActions.INSTANCE.async { executable.execute(player) }
        else executable.execute(player)

        return true
    }

    fun find(action: String) = actions.find { it.pattern.containsMatchIn(action) }

    fun register(vararg action: Action) = actions.addAll(action)

    fun registerAll(action: List<Action>) = actions.addAll(action)

    fun getSuggestions() = actions.map { it.getSuggestion() }

    fun getGroups() = actionGroups.keys
}
