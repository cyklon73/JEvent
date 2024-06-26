package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

/**
 * The Handler Object is the base class for any type of handler
 */
abstract class Handler<T extends Event> implements Comparable<Handler<T>> {
    protected Class<? extends Event> eventType;
    protected Class<?> wrappedType;
    private final byte priority;
    private final boolean ignoreCancelled;

    public Handler(Class<T> eventType, Class<?> wrappedType, byte priority, boolean ignoreCancelled) {
        this.eventType = eventType;
        this.wrappedType = wrappedType;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public boolean isSuitableHandler(@NotNull Class<? extends Event> event) {
        return eventType.isAssignableFrom(event) || (eventType.equals(WrappedEvent.class) && wrappedType!=null && wrappedType.isAssignableFrom(event));
    }

    @SuppressWarnings("unchecked")
    public void invoke(@NotNull EventManager manager, @NotNull Event event) {
        if(event instanceof Cancellable c && c.isCancelled() && !ignoreCancelled) return;
        if(isSuitableHandler(event.getClass())) invokeEvent(manager, (T) event);
    }

    protected abstract void invokeEvent(@NotNull EventManager manager, @NotNull T event);

    @Override
    public int compareTo(@NotNull Handler o) {
        return Byte.compare(o.priority, this.priority);
    }

    @Override
    public String toString() {
        return "Handler{eventType: %s, priority: %s, ignoreCancelled: %s, handler: %s}".formatted(eventType.equals(WrappedEvent.class) ? String.format("{%s, %s}", WrappedEvent.class, wrappedType) : eventType, priority, ignoreCancelled, "%s");
    }
}
