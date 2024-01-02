package de.cyklon.jevent;

import org.jetbrains.annotations.Nullable;

/**
 * This class provides an implementation for the Cancellable interface
 * <p>
 * Instead of
 * <pre>{@code
 * public class MyEvent extends Event implements Cancellable {
 *   private boolean cancelled = false;
 *
 *                @Override
 * 	public boolean isCancelled() {
 * 		return cancelled;
 * 	}
 *
 * 	@Override
 * 	public void setCancelled(boolean cancelled) {
 * 		this.cancelled = cancelled;
 * 	}
 * }
 * }</pre>
 *
 * do
 *
 * <pre>{@code
 * public class MyEvent extends CancellableEvent {
 *
 * }
 * }</pre>
 *
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
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
