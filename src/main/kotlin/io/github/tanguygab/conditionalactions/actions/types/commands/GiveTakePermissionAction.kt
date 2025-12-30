package io.github.tanguygab.conditionalactions.actions.types.commands

import io.github.tanguygab.conditionalactions.actions.Action
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.node.Node
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class GiveTakePermissionAction(val type: String) : Action("^(?i)$type-(permission|perm):( )?".toRegex()) {
    override fun getSuggestion() = "$type-permission: <permission>"

    override fun execute(player: OfflinePlayer?, match: String) {
        if (player !is Player) return

        val um = LuckPermsProvider.get().userManager
        val user = um.getUser(player.uniqueId) ?: return

        val permission = parsePlaceholders(player, match)
        val node = Node.builder(permission).build()

        when (type) {
            "give" -> user.data().add(node)
            "take" -> user.data().remove(node)
        }
        try { um.saveUser(user).get() }
        catch (_: Exception) {}
    }
}
