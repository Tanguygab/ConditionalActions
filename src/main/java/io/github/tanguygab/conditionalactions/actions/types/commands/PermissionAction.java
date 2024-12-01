package io.github.tanguygab.conditionalactions.actions.types.commands;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class PermissionAction extends Action {

    public PermissionAction() {
        super("^(?i)(permission|perm):(?<permission>[a-zA-Z0-9.*_\\- \",]+):( )?",false);
    }

    @Override
    public String getSuggestion() {
        return "permission:<permission>: <command>";
    }

    @Override
    public void execute(OfflinePlayer player, String match) {
        if (!(player instanceof Player p)) return;
        Matcher matcher = getPattern().matcher(match);
        if (!matcher.find()) return;
        String[] permission = matcher.group("permission").split(",");
        match = match.replaceAll(matcher.pattern().pattern(),"");

        UserManager um = LuckPermsProvider.get().getUserManager();
        User user = um.getUser(p.getUniqueId());
        if (user == null) return;
        List<Node> nodes = new ArrayList<>();
        for (String perm : permission) {
            if (user.getCachedData().getPermissionData().checkPermission(perm).asBoolean()) continue;
            Node node = Node.builder(perm).build();
            user.data().add(node);
            nodes.add(node);
        }
        try {
            um.saveUser(user).get();
            String command = parsePlaceholders(p,match);
            getPlugin().getScheduler().entity(p).run(()->p.performCommand(command)).asFuture().get();
        } catch (Exception ignored) {}
        nodes.forEach(node->user.data().remove(node));
        um.saveUser(user);
    }
}
