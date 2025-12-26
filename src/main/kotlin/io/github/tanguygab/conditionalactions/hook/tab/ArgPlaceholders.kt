package io.github.tanguygab.conditionalactions.hook.tab

import me.neznamy.tab.shared.TAB
import me.neznamy.tab.shared.placeholders.types.TabPlaceholder
import me.neznamy.tab.shared.platform.TabPlayer

abstract class ArgPlaceholders<T: TabPlaceholder>(prefix: String) {

    val argsPlaceholder: T
    val argsSizePlaceholder: T
    val argPlaceholders = mutableListOf<T>()

    init {
        argsPlaceholder = register(new("%${prefix}args%"))
        argsSizePlaceholder = register(new("%${prefix}args-size%", "0"))
    }

    private fun register(placeholder: T) = TAB.getInstance().placeholderManager.registerPlaceholder(placeholder)!!

    protected abstract fun new(identifier: String, default: String = ""): T
    protected abstract fun update(placeholder: T, player: TabPlayer?, value: String)

    fun update(player: TabPlayer?, args: List<String> = emptyList()) {
        update(argsPlaceholder, player, args.joinToString(" "))
        update(argsSizePlaceholder, player, args.size.toString())
        args.forEachIndexed { index, arg ->
            val placeholder = if (index < argPlaceholders.size) argPlaceholders[index]
            else register(new("%menu-arg-$index%")).also { argPlaceholders.add(it) }
            update(placeholder, player, arg)
        }
        if (args.size < argPlaceholders.size) argPlaceholders.forEachIndexed { index, placeholder ->
            if (index >= args.size) update(placeholder, player, "")
        }
    }

}