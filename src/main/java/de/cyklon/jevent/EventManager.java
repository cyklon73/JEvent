package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

/**
 * The EventManager to manage and execute events
 *
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
public sealed interface EventManager permits JEvent {
	/**
	 * registers all methods annotated with @{@link EventHandler}
	 *
	 * @param obj the object from which events are to be registered
	 */
	void registerListener(@NotNull Object obj);

	/**
	 * removes all listeners that have the type of the specified class
	 *
	 * @param clazz The type of listener Object to be removed
	 */
	void unregisterListener(@NotNull Class<?> clazz);

	/**
	 * removes all listeners
	 */
	void unregisterAll();

	/**
	 * calls the passed event and executes all registered listeners, as well as all listeners registered for a superclass of the event
	 *
	 * @param event the event to be executed
	 */
	void callEvent(@NotNull Event event);
}
