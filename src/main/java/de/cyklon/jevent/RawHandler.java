package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


/**
 * The RawHandler object represents a single event handler that is represented by a consumer without an additional method or other wrapper
 */
class RawHandler<T extends Event, W> extends Handler<T> {

    private final long id;
    private final Consumer<W> consumer;

    public RawHandler(Class<T> eventType, Class<W> wrappedType, Consumer<W> consumer, byte priority, boolean ignoreCancelled) {
        super(eventType, eventType.equals(wrappedType) ? null : wrappedType, priority, ignoreCancelled);
        this.id = System.nanoTime();
        this.consumer = consumer;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void invokeEvent(@NotNull EventManager manager, @NotNull T event) {
        W eventObj;
        if (wrappedType==null) eventObj = (W) event;
        else eventObj = ((WrappedEvent<W>) event).getWrapped();
        consumer.accept(eventObj);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RawHandler<?, ?> rh && rh.id==this.id;
    }

    @Override
    public String toString() {
        return super.toString().formatted(consumer);
    }
}
