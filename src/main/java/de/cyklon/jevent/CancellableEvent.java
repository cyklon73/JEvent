package de.cyklon.jevent;

import org.jetbrains.annotations.Nullable;

public abstract class CancellableEvent extends Event implements Cancellable {

	private boolean cancelled = false;

	protected CancellableEvent() {
	}

	protected CancellableEvent(@Nullable String name) {
		super(name);
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
