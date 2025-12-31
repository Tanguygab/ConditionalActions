package io.github.tanguygab.cabridge

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import net.md_5.bungee.api.ServerPing
import net.md_5.bungee.api.plugin.Plugin
import java.util.concurrent.TimeUnit

class CABridge : Plugin() {
    internal val onlineServers = mutableMapOf<String, ServerPing>()

    override fun onEnable() {
        proxy.registerChannel(CHANNEL)
        proxy.pluginManager.registerListener(this, CAListener(this))
        proxy.scheduler.schedule(this, ::pingServers, 10, TimeUnit.SECONDS)
    }
    override fun onDisable() {
        proxy.unregisterChannel(CHANNEL)
        proxy.pluginManager.unregisterListeners(this)
        proxy.scheduler.cancel(this)
    }

    fun pingServers() {
        val changed = mutableMapOf<String, ServerPing?>()

        proxy.servers.forEach { (server, info) -> info.ping { ping, error ->
            if (server in onlineServers == (error != null) || onlineServers[server]?.players?.online != ping?.players?.online)
                changed[server] = ping

            if (error != null) onlineServers.remove(server)
            else onlineServers[server] = ping
        } }

        sendData {
            writeUTF("update")
            writeInt(changed.size)
            changed.forEach { (name, info) -> writeUTF("$name|${info != null}|${info?.players?.online ?: 0}") }
        }
    }

    internal fun sendData(call: ByteArrayDataOutput.() -> Unit) {
        val data = ByteStreams.newDataOutput().also { it.call() }.toByteArray()
        proxy.servers.forEach { (_, server) -> server.sendData(CHANNEL, data)}
    }

    companion object {
        const val CHANNEL = "conditionalactions:channel"
    }
}