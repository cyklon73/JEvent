package de.cyklon.jevent.internal;

import de.cyklon.jevent.Event;
import de.cyklon.jevent.EventManager;

public class EventCallJEvent extends InternalJEvent {

	private final Event initialEvent;
	private Event event;

	public EventCallJEvent(EventManager manager, Event event) {
		super(manager);
		this.event = event;
		this.initialEvent = event;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Event getInitialEvent() {
		return initialEvent;
	}

	public boolean isEventModified() {
		return !initialEvent.equals(event);
	}
}
