package io.github.tanguygab.conditionalactions.hook.tab

import me.neznamy.tab.shared.platform.TabPlayer
import me.neznamy.tab.shared.placeholders.types.TabPlaceholder
import java.util.concurrent.ConcurrentHashMap

class ThreadPlaceholder(identifier: String) : TabPlaceholder(identifier, -1) {

    private val lastPlaceholderValues = ConcurrentHashMap<Thread, String>()

    fun updateValue(value: String?) {
        if (value === null) lastPlaceholderValues.remove(Thread.currentThread())
        else lastPlaceholderValues[Thread.currentThread()] = value
    }

    override fun updateFromNested(p: TabPlayer) {

    }
    override fun getLastValue(p: TabPlayer?) = lastPlaceholderValues[Thread.currentThread()] ?: identifier
    override fun getLastValueSafe(p: TabPlayer) = lastPlaceholderValues[Thread.currentThread()] ?: identifier

}