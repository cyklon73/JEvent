package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

public final class WrappedEvent<T> extends Event {

    private final T wrapped;

    public WrappedEvent(@NotNull T wrapped) {
        super(WrappedEvent.class.getSimpleName() + "{" + wrapped.getClass().getName() + "}");
        this.wrapped = wrapped;
    }

    public T getWrapped() {
        return wrapped;
    }

    @Override
    public String toString() {
        return WrappedEvent.class.getName() + "(" + wrapped.getClass().getName() + ")";
    }
}
