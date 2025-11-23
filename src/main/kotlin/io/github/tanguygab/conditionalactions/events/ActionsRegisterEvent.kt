package io.github.tanguygab.conditionalactions.events

import io.github.tanguygab.conditionalactions.actions.Action
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ActionsRegisterEvent : Event() {
    val actions = mutableListOf<Action>()

    fun addActions(vararg actions: Action) = this.actions.addAll(actions)

    override fun getHandlers() = Companion.handlers

    companion object {
        val handlers = HandlerList()
    }
}
