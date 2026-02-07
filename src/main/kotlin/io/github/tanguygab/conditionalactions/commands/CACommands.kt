package io.github.tanguygab.conditionalactions.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.actions.ActionData
import io.github.tanguygab.conditionalactions.actions.ActionGroup
import io.github.tanguygab.conditionalactions.actions.CAExecutable
import io.github.tanguygab.conditionalactions.commands.arguments.ActionArgumentType
import io.github.tanguygab.conditionalactions.commands.arguments.ConditionArgumentType
import io.github.tanguygab.conditionalactions.commands.arguments.GroupArgumentType
import io.github.tanguygab.conditionalactions.conditions.ConditionGroup
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.kyori.adventure.text.Component

class CACommands(private val plugin: ConditionalActions) {

    private val messages get() = plugin.messages

    private fun hasPermission(cmd: CommandSourceStack, permission: String) = cmd.sender.hasPermission("conditionalactions.$permission")
    private fun sendMessage(ctx: CommandContext<CommandSourceStack>, message: Component) {
        ctx.source.sender.sendMessage(message)
    }

    private fun literal(name: String, permission: Boolean = false, executes: ((CommandContext<CommandSourceStack>) -> Int)? = null) = Commands
        .literal(name)
        .requires { !permission || hasPermission(it, name) }
        .executes(executes)

    private val reload = literal("reload", true) {
        plugin.onDisable()
        plugin.onEnable()
        sendMessage(it, messages.commandsReloadSuccess)
        Command.SINGLE_SUCCESS
    }

    private fun execute(ctx: CommandContext<CommandSourceStack>, executable: CAExecutable): Int {
        // --console?
        val players = ctx.getArgument("players", PlayerSelectorArgumentResolver::class.java)

        plugin.actionManager.execute(players.resolve(ctx.source), executable)
        return Command.SINGLE_SUCCESS
    }

    private val execute = literal("execute", true)
        .then(Commands.argument("players", ArgumentTypes.players())
            .then(Commands.argument("action", ActionArgumentType(plugin))
                .executes {
                    val action = it.getArgument("action", ActionData::class.java)
                    execute(it, action)
                }
            )
        )

    private val group = literal("group", true)
        .then(literal("list") {
            val groups = plugin.actionManager.getGroups()

            val message = messages.commandsGroupListHeader(groups.size)
                .children(groups.map { condition -> messages.commandsGroupListLine(condition) })
            sendMessage(it, message)
            Command.SINGLE_SUCCESS
        }).then(literal("execute")
            .then(Commands.argument("players", ArgumentTypes.players())
                .then(Commands.argument("group", GroupArgumentType(plugin))
                    .executes {
                        val group = it.getArgument("group", ActionGroup::class.java)
                        execute(it, group)
                    }
                )
            )
        )


    private val condition = literal("condition", true)
        .then(literal("list") {
            val conditions = plugin.conditionManager.getConditions()

            val message = messages.commandsConditionListHeader(conditions.size)
                .children(conditions.map { condition -> messages.commandsConditionListLine(condition) })
            sendMessage(it, message)
            Command.SINGLE_SUCCESS
        }).then(literal("check")
            .then(Commands.argument("players", ArgumentTypes.players())
                .then(Commands.argument("condition", ConditionArgumentType(plugin))
                    .executes {
                        // --console?
                        val players = it.getArgument("players", PlayerSelectorArgumentResolver::class.java)
                        val condition = it.getArgument("condition", ConditionGroup::class.java)

                        players.resolve(it.source).forEach { player ->
                            sendMessage(it, if (condition.isMet(player))
                                messages.commandsConditionCheckSuccess(player.name)
                            else messages.commandsConditionCheckFail(player.name))
                        }
                        Command.SINGLE_SUCCESS
                    }
                )
            )
        )

    val main = Commands.literal("conditionalactions")
        .then(reload)
        .then(execute)
        .then(group)
        .then(condition)
        .build()!!
}