package io.github.tanguygab.conditionalactions.commands.arguments

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.actions.ActionGroup
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import java.util.concurrent.CompletableFuture

class GroupArgumentType(val plugin: ConditionalActions) : CustomArgumentType.Converted<ActionGroup, String> {
    override fun convert(nativeType: String) = plugin.actionManager.getGroup(nativeType)!!

    override fun getNativeType() = StringArgumentType.string()!!

    override fun <S: Any> listSuggestions(context: CommandContext<S>,
        builder: SuggestionsBuilder
    ) = CompletableFuture.supplyAsync {
        plugin.actionManager.getGroups()
            .map { if (it.matches("[A-Za-z0-9_\\-+.]+".toRegex())) it else "\"$it\"" }
            .forEach { builder.suggest(it) }
        builder.build()
    }!!
}