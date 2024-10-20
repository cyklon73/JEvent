package de.cyklon.jevent.internal;

import de.cyklon.jevent.CancellableEvent;
import de.cyklon.jevent.Event;
import de.cyklon.jevent.EventManager;
import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectPackage;

public abstract class InternalJEvent extends CancellableEvent {

	private static final ReflectPackage INTERNAL_PACKAGE = ReflectClass.wrap(InternalJEvent.class).getPackage();

	private final EventManager manager;

	protected InternalJEvent(EventManager manager) {
		this.manager = manager;
	}

	public EventManager getManager() {
		return manager;
	}

	public static <T extends Event> boolean isInternal(T event) {
		return ReflectClass.getClass(event).getPackage().equals(INTERNAL_PACKAGE);
	}
}
