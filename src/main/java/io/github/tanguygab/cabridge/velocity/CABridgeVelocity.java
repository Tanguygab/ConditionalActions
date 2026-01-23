package io.github.tanguygab.cabridge.velocity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.scheduler.ScheduledTask;
import io.github.tanguygab.conditionalactions.ProjectVariables;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Plugin(
        id = "cabridge",
        name = "CA-Bridge",
        version = ProjectVariables.PLUGIN_VERSION,
        authors = {"Tanguygab"},
        description = "More features for the plugin TAB !"
)
public class CABridgeVelocity {
    private static final ChannelIdentifier CHANNEL = MinecraftChannelIdentifier.from("conditionalactions:channel");
    private final ProxyServer proxy;
    private final Logger logger;
    private final Map<RegisteredServer, ServerPing> onlineServers = new HashMap<>();

    @Inject
    public CABridgeVelocity(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        proxy.getChannelRegistrar().register(CHANNEL);
        proxy.getScheduler().buildTask(this, this::pingServers).repeat(10, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {
        proxy.getChannelRegistrar().unregister(CHANNEL);
        proxy.getScheduler().tasksByPlugin(this).forEach(ScheduledTask::cancel);
    }

    private int getOnline(ServerPing ping) {
        return ping == null ? 0 : ping.getPlayers().map(ServerPing.Players::getOnline).orElse(0);
    }

    private void sendData(Consumer<ByteArrayDataOutput> call) {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        call.accept(data);
        proxy.getAllServers().forEach(server -> server.sendPluginMessage(CHANNEL, data.toByteArray()));
    }
    private void sendUpdate(Map<RegisteredServer, ServerPing> servers) {
        sendData(data -> {
            data.writeUTF("update");
            data.writeInt(servers.size());
            servers.forEach((name, info) -> data.writeUTF(name + "|" + (info != null) + "|" + (info == null ? 0 : info.getPlayers().map(ServerPing.Players::getOnline).orElse(0))));
        });
    }

    private void pingServers() {
        Map<RegisteredServer, ServerPing> changed = new HashMap<>();

        proxy.getAllServers().forEach(server -> {
            CompletableFuture<ServerPing> future = new CompletableFuture<>();
            server.ping().thenAcceptAsync(ping -> {
                if (onlineServers.containsKey(server) == (ping == null) || getOnline(onlineServers.get(server)) != getOnline(ping))
                    changed.put(server, ping);

                if (ping == null) onlineServers.remove(server);
                else onlineServers.put(server, ping);
                future.complete(ping);
            });
            try {future.get(500, TimeUnit.MILLISECONDS);}
            catch (InterruptedException | ExecutionException | TimeoutException ignored) {}
        });

        sendUpdate(changed);
    }

    @Subscribe
    public void onPluginMessageFromPlayer(PluginMessageEvent event) {
        if (!CHANNEL.equals(event.getIdentifier())) return;
        event.setResult(PluginMessageEvent.ForwardResult.handled());
        if (!(event.getSource() instanceof ServerConnection source)) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        switch (in.readUTF()) {
            case "start" -> {
                sendData(data -> {
                    data.writeUTF("name");
                    data.writeUTF(source.getServerInfo().getName());
                });
                sendUpdate(onlineServers);
            }
            case "command" -> {
                String playerName = in.readUTF();
                String command = in.readUTF();

                CommandSource player = playerName.isEmpty() ? proxy.getConsoleCommandSource() : proxy.getPlayer(playerName).orElse(null);
                if (player == null) {
                    logger.warn("Tried to run command \"{}\" as invalid player \"{}\"", command, playerName);
                    break;
                }
                proxy.getCommandManager().executeAsync(player, command);
            }
        }
    }
}