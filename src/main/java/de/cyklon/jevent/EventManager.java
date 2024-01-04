package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The EventManager to manage and execute events
 *
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
public sealed interface EventManager permits JEvent {
	/**
	 * registers all {@link MethodHandler EventHandlers} in the listener Class
	 *
	 * @param obj the object from which events are to be registered
	 */
	void registerListener(@NotNull Object obj);

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param priority the event {@link EventHandler#priority() priority}
	 * @param ignoreCancelled if true, the handler is not called for {@link EventHandler#ignoreCancelled() canceled events}
	 * @param <T> the event type
	 */
	<T extends Event> void registerHandler(@NotNull Class<T> event, Consumer<T> handler, byte priority, boolean ignoreCancelled);

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param priority the event {@link EventHandler#priority() priority}
	 * @param <T> the event type
	 */
	default <T extends Event> void registerHandler(@NotNull Class<T> event, Consumer<T> handler, byte priority) {
		registerHandler(event, handler, priority, false);
	}

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param ignoreCancelled if true, the handler is not called for {@link EventHandler#ignoreCancelled() canceled events}
	 * @param <T> the event type
	 */
	default <T extends Event> void registerHandler(@NotNull Class<T> event, Consumer<T> handler, boolean ignoreCancelled) {
		registerHandler(event, handler, EventHandler.NORMAL, ignoreCancelled);
	}

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param <T> the event type
	 */
	default <T extends Event> void registerHandler(@NotNull Class<T> event, Consumer<T> handler) {
		registerHandler(event, handler, EventHandler.NORMAL, false);
	}

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
	 * @return Whether the event was canceled
	 */
	boolean callEvent(@NotNull Event event);
}
