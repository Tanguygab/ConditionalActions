package io.github.tanguygab.conditionalactions.actions.types.commands

import io.github.tanguygab.conditionalactions.actions.Action
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.node.Node
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

import java.util.concurrent.CompletableFuture

class PermissionAction : Action("^(?i)(permission|perm):(?<permission>[a-zA-Z0-9.*_\\- \",]+):( )?".toRegex(), false) {
    override fun getSuggestion() = "permission:<permission>: <command>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return

        val matcher = pattern.matchEntire(match) ?: return
        val permission = matcher.groups["permission"]!!.value.split(",")
        val match = match.replace(pattern, "")

        val um = LuckPermsProvider.get().userManager
        val user = um.getUser(player.uniqueId) ?: return

        val nodes = mutableListOf<Node>()
        for (perm in permission) {
            if (user.cachedData.permissionData.checkPermission(perm).asBoolean()) continue
            val node = Node.builder(perm).build()
            user.data().add(node)
            nodes.add(node)
        }
        try {
            um.saveUser(user).get()
            val command = parsePlaceholders(player, match)
            val future = CompletableFuture<ScheduledTask>()
            player.scheduler.run(plugin, {
                player.performCommand(command)
                future.complete(it)
            }, null)
            future.get()
        } catch (_: Exception) {}
        nodes.forEach { user.data().remove(it) }
        um.saveUser(user)
    }
}
