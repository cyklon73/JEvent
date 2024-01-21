package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

/**
 * The Handler Object is the base class for any type of handler
 *
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
abstract class Handler<T extends Event> implements Comparable<Handler<T>> {
    protected Class<? extends Event> eventType;
    private final byte priority;
    private final boolean ignoreCancelled;

    public Handler(Class<T> eventType, byte priority, boolean ignoreCancelled) {
        this.eventType = eventType;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public boolean isSuitableHandler(@NotNull Class<? extends Event> event) {
        return eventType.isAssignableFrom(event);
    }

    @SuppressWarnings("unchecked")
    public void invoke(@NotNull EventManager manager, @NotNull Event event) {
        if(event instanceof Cancellable && ((Cancellable) event).isCancelled() && !ignoreCancelled) return;
        if(isSuitableHandler(event.getClass())) invokeEvent(manager, (T) event);
    }

    protected abstract void invokeEvent(@NotNull EventManager manager, @NotNull T event);

    @Override
    public int compareTo(@NotNull Handler o) {
        return Byte.compare(o.priority, this.priority);
    }
}
