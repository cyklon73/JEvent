package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * the event base class.
 * Extend this class to create your own event
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
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
	 * @param name the event name. If it is null, it is set to the class name
	 */
	protected Event(@Nullable String name) {
		this.name = name==null ? this.getClass().getSimpleName() : name;
	}

	/**
	 * call all listeners for this event
	 * @see EventManager#callEvent(Event)
	 * @return true if the event was canceled
	 */
	public boolean callEvent() {
		JEvent.MANAGER.callEvent(this);
		if (this instanceof Cancellable) return ((Cancellable)this).isCancelled();
		return false;
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
