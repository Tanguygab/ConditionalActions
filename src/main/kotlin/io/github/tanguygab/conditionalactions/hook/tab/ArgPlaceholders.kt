package io.github.tanguygab.conditionalactions.hook.tab

import me.neznamy.tab.shared.TAB
import me.neznamy.tab.shared.placeholders.PlaceholderReference
import me.neznamy.tab.shared.placeholders.types.TabPlaceholder
import me.neznamy.tab.shared.platform.TabPlayer

abstract class ArgPlaceholders<T: TabPlaceholder>(val prefix: String) {

    val argsPlaceholder: PlaceholderReference
    val argsSizePlaceholder: PlaceholderReference
    val argPlaceholders = mutableListOf<PlaceholderReference>()

    init {
        argsPlaceholder = register(new("%${prefix}args%"))
        argsSizePlaceholder = register(new("%${prefix}args-size%", "0"))
    }

    private fun register(placeholder: T) = TAB.getInstance().placeholderManager.registerPlaceholder(placeholder)!!

    protected abstract fun new(identifier: String, default: String = ""): T
    protected abstract fun update(placeholder: T, player: TabPlayer?, value: String)

    @Suppress("CAST_NEVER_SUCCEEDS")
    fun update(player: TabPlayer?, args: List<String> = emptyList()) {
        update(argsPlaceholder as T, player, args.joinToString(" "))
        update(argsSizePlaceholder as T, player, args.size.toString())
        args.forEachIndexed { index, arg ->
            val placeholder = if (index < argPlaceholders.size) argPlaceholders[index]
            else register(new("%${prefix}arg-$index%")).also { argPlaceholders.add(it) }
            update(placeholder as T, player, arg)
        }
        if (args.size < argPlaceholders.size) argPlaceholders.forEachIndexed { index, placeholder ->
            if (index >= args.size) update(placeholder as T, player, "")
        }
    }

}