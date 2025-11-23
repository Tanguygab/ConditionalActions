package io.github.tanguygab.conditionalactions.actions

import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.Utils
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
import io.github.tanguygab.conditionalactions.actions.types.other.DelayAction
import io.github.tanguygab.conditionalactions.events.ActionsRegisterEvent

import net.kyori.adventure.text.Component
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class ActionManager(private val plugin: ConditionalActions, val argumentSeparator: String) {
    private val actions = mutableListOf<Action>()
    private val actionGroups = mutableMapOf<String, ActionGroup>()

    init {
        register(
            PlayerAction(),
            ConsoleAction(),
            GroupAction(),

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

            DelayAction()
        )
        if (plugin.server.pluginManager.isPluginEnabled("LuckPerms")) register(PermissionAction())

        try {
            Player::class.java.getMethod("sendMessage", Component::class.java)
            register(MiniMessageAction())
        } catch (_: NoSuchMethodException) {}

        plugin.sync(null) { load() }
    }

    fun load() {
        val event = ActionsRegisterEvent()
        plugin.server.pluginManager.callEvent(event)
        registerAll(event.actions)

        Utils.updateFiles(plugin, "actiongroups.yml", "actiongroups/default-groups.yml")
        Utils.loadFiles(plugin, "actiongroups") { name: String, obj: Any ->
            if (obj is List<*>) actionGroups[name] = ActionGroup(this, obj)
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

    fun find(action: String): Action? {
        for (ac in actions) {
            val matcher = ac.pattern.matcher(action)
            if (matcher.find()) return ac
        }
        return null
    }

    fun register(vararg action: Action) = actions.addAll(action)

    fun registerAll(action: List<Action>) = actions.addAll(action)

    fun getSuggestions() = actions.map { it.getSuggestion() }

    fun getGroups() = actionGroups.keys
}
