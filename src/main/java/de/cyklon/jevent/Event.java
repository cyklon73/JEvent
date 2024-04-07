package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base class of all Events.
 * Extend this class to create your own event
 */
public abstract class Event {
	/**
	 * the event name
	 */
	private final String name;

	/**
	 * constructs the event, with the class name as the event name
	 */
	protected Event() {
		this(null);
	}

	/**
	 * constructs the event with a specific name
	 *
	 * @param name the event name. If it is null, it is set to the class name
	 */
	protected Event(@Nullable String name) {
		this.name = name == null ? this.getClass().getSimpleName() : name;
	}

	/**
	 * call all listeners for this event
	 *
	 * @return true if the event was canceled
	 * @see EventManager#callEvent(Event)
	 */
	public boolean callEvent() {
		return JEvent.DEFAULT_MANAGER.callEvent(this);
	}

	/**
	 * @return the name of the Event
	 */
	@NotNull
	public String getEventName() {
		return name;
	}

	@Override
	public String toString() {
		return Event.class + "(" + getEventName() + ")";
	}
}
