package io.github.tanguygab.conditionalactions.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.actions.CAExecutable
import io.github.tanguygab.conditionalactions.commands.arguments.ActionArgumentType
import io.github.tanguygab.conditionalactions.commands.arguments.ConditionArgumentType
import io.github.tanguygab.conditionalactions.commands.arguments.GroupArgumentType
import io.github.tanguygab.conditionalactions.conditions.ConditionGroup
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
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
        val players = if (ctx.nodes.any { it.node.name == "@c" }) listOf(null)
        else ctx.getArgument("players", PlayerSelectorArgumentResolver::class.java)
            .resolve(ctx.source)

        plugin.actionManager.execute(players, executable)
        return Command.SINGLE_SUCCESS
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.playersArgument(arg: RequiredArgumentBuilder<CommandSourceStack, *>): LiteralArgumentBuilder<CommandSourceStack> {
        listOf(Commands.argument("players", ArgumentTypes.players()), Commands.literal("@c")).forEach {
            then(it.then(arg))
        }
        return this
    }

    private fun executeCommand(argumentName: String, argumentType: CustomArgumentType.Converted<*, *>) = literal("execute", true)
        .playersArgument(Commands.argument(argumentName, argumentType).executes {
            val executable = it.getArgument(argumentName, CAExecutable::class.java)
            execute(it, executable)
        })

    private val execute = executeCommand("action", ActionArgumentType(plugin))

    private val group = literal("group", true)
        .then(executeCommand("group", GroupArgumentType(plugin)))
        .then(literal("list") {
            val groups = plugin.actionManager.getGroups()

            val message = messages.commandsGroupListHeader(groups.size)
                .children(groups.map { condition -> messages.commandsGroupListLine(condition) })
            sendMessage(it, message)
            Command.SINGLE_SUCCESS
        })

    private val condition = literal("condition", true)
        .then(literal("list") {
            val conditions = plugin.conditionManager.getConditions()

            val message = messages.commandsConditionListHeader(conditions.size)
                .children(conditions.map { condition -> messages.commandsConditionListLine(condition) })
            sendMessage(it, message)
            Command.SINGLE_SUCCESS
        }).then(literal("check")
            .playersArgument(Commands.argument("condition", ConditionArgumentType(plugin))
                .executes { ctx ->
                    val players = if (ctx.nodes.any { it.node.name == "@c" }) listOf(null)
                    else ctx.getArgument("players", PlayerSelectorArgumentResolver::class.java)
                        .resolve(ctx.source)

                    val condition = ctx.getArgument("condition", ConditionGroup::class.java)

                    players.forEach { player ->
                        val name = player?.name ?: "Console"
                        sendMessage(ctx, if (condition.isMet(player))
                            messages.commandsConditionCheckSuccess(name)
                        else messages.commandsConditionCheckFail(name))
                    }
                    Command.SINGLE_SUCCESS
                }
            )
        )

    val main = Commands.literal("conditionalactions")
        .then(reload)
        .then(execute)
        .then(group)
        .then(condition)
        .build()!!
}