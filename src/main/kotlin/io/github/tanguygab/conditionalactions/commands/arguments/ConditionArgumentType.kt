package io.github.tanguygab.conditionalactions.commands.arguments

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.conditions.ConditionGroup
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import java.util.concurrent.CompletableFuture

class ConditionArgumentType(val plugin: ConditionalActions) : CustomArgumentType.Converted<ConditionGroup, String> {
    override fun convert(nativeType: String) = plugin.conditionManager.getCondition(nativeType)

    override fun getNativeType() = StringArgumentType.greedyString()!!

    override fun <S: Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ) = CompletableFuture.supplyAsync {
        plugin.conditionManager.getConditions().forEach {
            builder.suggest(it)
        }
        builder.build()
    }!!
}