package io.github.tanguygab.cabridge.bungee

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import net.md_5.bungee.api.ServerPing
import net.md_5.bungee.api.plugin.Plugin
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class CABridgeBungee : Plugin() {
    internal val onlineServers = mutableMapOf<String, ServerPing>()

    override fun onEnable() {
        proxy.registerChannel(CHANNEL)
        proxy.pluginManager.registerListener(this, CAListener(this))
        proxy.scheduler.schedule(this, ::pingServers, 0, 10, TimeUnit.SECONDS)
    }
    override fun onDisable() {
        proxy.unregisterChannel(CHANNEL)
        proxy.pluginManager.unregisterListeners(this)
        proxy.scheduler.cancel(this)
    }

    internal fun sendData(call: ByteArrayDataOutput.() -> Unit) {
        val data = ByteStreams.newDataOutput().also { it.call() }.toByteArray()
        proxy.servers.forEach { (_, server) -> server.sendData(CHANNEL, data)}
    }

    internal fun sendUpdate(servers: Map<String, ServerPing?>) = sendData {
        writeUTF("update")
        writeInt(servers.size)
        servers.forEach { (name, info) -> writeUTF("$name|${info != null}|${info?.players?.online ?: 0}") }
    }

    fun pingServers() {
        val changed = mutableMapOf<String, ServerPing?>()

        proxy.servers.forEach { (server, info) ->
            val future = CompletableFuture<ServerPing?>()
            info.ping { ping, error ->
                if (server in onlineServers == (error != null) || onlineServers[server]?.players?.online != ping?.players?.online)
                    changed[server] = ping

                if (error != null) onlineServers.remove(server)
                else onlineServers[server] = ping
                future.complete(ping)
            }
            future.get()
        }

        sendUpdate(changed)
    }

    companion object {
        const val CHANNEL = "conditionalactions:channel"
    }
}