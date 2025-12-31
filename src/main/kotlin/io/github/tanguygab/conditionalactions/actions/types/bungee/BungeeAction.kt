package io.github.tanguygab.conditionalactions.actions.types.bungee

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import io.github.tanguygab.conditionalactions.ConditionalActions
import io.github.tanguygab.conditionalactions.actions.Action

abstract class BungeeAction(pattern: Regex) : Action(pattern) {

    protected fun sendData(call: ByteArrayDataOutput.() -> Unit) {
        val data = ByteStreams.newDataOutput()
        data.call()
        plugin.server.sendPluginMessage(plugin, ConditionalActions.CHANNEL, data.toByteArray())
    }
}