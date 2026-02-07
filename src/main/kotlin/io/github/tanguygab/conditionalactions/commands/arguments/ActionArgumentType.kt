package io.github.tanguygab.conditionalactions.commands.arguments

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.actions.ActionData
import io.papermc.paper.command.brigadier.MessageComponentSerializer
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import java.util.concurrent.CompletableFuture

class ActionArgumentType(val plugin: ConditionalActions) : CustomArgumentType.Converted<ActionData, String> {
    override fun convert(nativeType: String): ActionData {
        val action = plugin.actionManager.find(nativeType)!!
        return ActionData(action, nativeType)
    }

    override fun getNativeType() = StringArgumentType.greedyString()!!

    override fun <S: Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ) = CompletableFuture.supplyAsync {
        plugin.actionManager.getActions().forEach {
            builder.suggest(it.getSuggestion(), messageSerializer.serialize(it.description))
        }
        builder.build()
    }!!

    companion object {
        private val messageSerializer = MessageComponentSerializer.message()
    }
}