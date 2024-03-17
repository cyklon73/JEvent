package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


/**
 * The RawHandler object represents a single event handler that is represented by a consumer without an additional method or other wrapper
 */
class RawHandler<T extends Event> extends Handler<T> {

    private final Consumer<T> consumer;

    public RawHandler(Class<T> eventType, Consumer<T> consumer, byte priority, boolean ignoreCancelled) {
        super(eventType, priority, ignoreCancelled);
        this.consumer = consumer;
    }

    @Override
    protected void invokeEvent(@NotNull EventManager manager, @NotNull T event) {
        consumer.accept(event);
    }
}
