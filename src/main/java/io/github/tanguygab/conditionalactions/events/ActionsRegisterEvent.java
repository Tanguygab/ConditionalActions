package io.github.tanguygab.conditionalactions.events;

import io.github.tanguygab.conditionalactions.actions.types.Action;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActionsRegisterEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final List<Action> actions = new ArrayList<>();

    public void addActions(@NotNull Action... actions) {
        this.actions.addAll(List.of(actions));
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
