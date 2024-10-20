package de.cyklon.jevent;

import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * The EventManager to manage and execute events
 */
public sealed interface EventManager permits JEvent {

	/**
	 * Each EventManager has a unique identifier so that it can be uniquely identified for internal events, for example
	 * @return The unique id of the EventManager
	 */
	UUID getId();

	/**
	 * Can only be activated when created
	 * @return true if internal events enabled
	 */
	boolean internalEventsEnabled();

	/**
	 * registers all {@link MethodHandler EventHandlers} in the listener Class
	 *
	 * @param obj the object from which events are to be registered
	 */
	void registerListener(@NotNull Object obj);


	/**
	 * registers all {@link MethodHandler EventHandlers} in the listener Class
	 * <p>
	 * The class must have a no args constructor.
	 * @param clazz the class from which events are to be registered
	 */
	void registerListener(@NotNull Class<?> clazz);

	/**
	 * registers all {@link MethodHandler EventHandlers} in the listener Class
	 * <p>
	 * The class must have a no args constructor.
	 * @param clazz the class from which events are to be registered
	 */
	void registerListener(@NotNull ReflectClass<?> clazz);

	/**
	 * registers a package as Listener package.
	 * <p>
	 * In a listener package, every class that is annotated with {@link Listener} is registered as a listener
	 *
	 * @param packageName the name of the package to be registered as a listener package
	 */
	default void registerListenerPackage(@NotNull String packageName) {
		registerListenerPackage(OfflinePackage.get(packageName));
	}

	/**
	 * registers a package as Listener package.
	 * <p>
	 * In a listener package, every class that is annotated with {@link Listener} is registered as a listener
	 *
	 * @param pkg the package
	 */
	void registerListenerPackage(@NotNull OfflinePackage pkg);

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param priority the event {@link EventHandler#priority() priority}
	 * @param ignoreCancelled if true, the handler is not called for {@link EventHandler#ignoreCancelled() canceled events}
	 * @param <T> the event type
	 */
	<T extends Event> void registerHandler(@NotNull Class<T> event, @NotNull Consumer<T> handler, byte priority, boolean ignoreCancelled);

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param priority the event {@link EventHandler#priority() priority}
	 * @param <T> the event type
	 */
	default <T extends Event> void registerHandler(@NotNull Class<T> event, @NotNull Consumer<T> handler, byte priority) {
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
	default <T extends Event> void registerHandler(@NotNull Class<T> event, @NotNull Consumer<T> handler, boolean ignoreCancelled) {
		registerHandler(event, handler, EventHandler.NORMAL, ignoreCancelled);
	}

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param <T> the event type
	 */
	default <T extends Event> void registerHandler(@NotNull Class<T> event, @NotNull Consumer<T> handler) {
		registerHandler(event, handler, EventHandler.NORMAL, false);
	}

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param priority the event {@link EventHandler#priority() priority}
	 * @param ignoreCancelled if true, the handler is not called for {@link EventHandler#ignoreCancelled() canceled events}
	 * @param <T> the event type
	 */
	<T> void registerWrappedHandler(@NotNull Class<T> event, Consumer<T> handler, byte priority, boolean ignoreCancelled);

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param priority the event {@link EventHandler#priority() priority}
	 * @param <T> the event type
	 */
	default <T> void registerWrappedHandler(@NotNull Class<T> event, Consumer<T> handler, byte priority) {
		registerWrappedHandler(event, handler, priority, false);
	}

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param ignoreCancelled if true, the handler is not called for {@link EventHandler#ignoreCancelled() canceled events}
	 * @param <T> the event type
	 */
	default <T> void registerWrappedHandler(@NotNull Class<T> event, Consumer<T> handler, boolean ignoreCancelled) {
		registerWrappedHandler(event, handler, EventHandler.NORMAL, ignoreCancelled);
	}

	/**
	 * registers a listener for a specific event, with a consumer instead of a method
	 *
	 * @param event the event for which the listener is to be registered
	 * @param handler The consumer to be executed when the event is called
	 * @param <T> the event type
	 */
	default <T> void registerWrappedHandler(@NotNull Class<T> event, Consumer<T> handler) {
		registerWrappedHandler(event, handler, EventHandler.NORMAL, false);
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

	/**
	 * registers the given instance as a Parameter instance to the key
	 * <p>
	 * Parameter instances can be used to add custom parameters to events and to instantiate classes that use parameter instances as parameters
	 *
	 * @param key the key of the instance
	 * @param instance the parameter instance
	 */
	void registerParameterInstance(@NotNull String key, @Nullable Object instance);

	/**
	 * registers the given instance as a Parameter instance to the given type
	 * <p>
	 * Parameter instances can be used to add custom parameters to events and to instantiate classes that use parameter instances as parameters
	 *
	 * @param type the type to register the instance
	 * @param instance the parameter instance
	 */
	default <T> void registerParameterInstance(@NotNull Class<? extends T> type, @Nullable T instance) {
		registerParameterInstance(type.getName(), instance);
	}

	/**
	 * registers the given instance as a Parameter instance to the type of the instance
	 * <p>
	 * Parameter instances can be used to add custom parameters to events and to instantiate classes that use parameter instances as parameters
	 *
	 * @param instance the parameter instance
	 */
	default void registerParameterInstance(@NotNull Object instance) {
		registerParameterInstance(instance.getClass(), instance);
	}


	/**
	 * remove the parameter instance registered to the given key
	 * @param key the key of the instance
	 * @return the instance registered to this key, or null if no instance is registered to this key
	 */
	@Nullable
	Object removeParameterInstance(@NotNull String key);

	/**
	 * returns the parameter instance registered to the given key
	 * @param key the key of the instance
	 * @return the value or null if there is no instance for this key, or the value is set to null
	 */
	@Nullable
	Object getParameterInstance(@NotNull String key);

	/**
	 * Sets a logger for debug prints
	 * <p>
	 * example for java.util.logging and SLF4J:
	 * <pre>{@code
	 *  public class Main {
	 *
	 *      public static void main(String[] args) {
	 *          EventManager manager = JEvent.getDefaultManager();
	 *
	 *          //java.util.logging
	 *          manager.setDebugLogger(msg -> logger.log(Level.FINE, msg));
	 *
	 *          //SLF4J
	 *          manager.setDebugLogger(logger::debug);
	 *      }
	 *
	 *  }
	 * }</pre>
	 * @param logger the consumer with the corresponding log method or null to disable debug messages
	 */
	void setDebugLogger(@Nullable Consumer<String> logger);

	/**
	 * @return true if this EventManager is the Default Manager
	 */
	default boolean isDefaultManager() {
		return JEvent.DEFAULT_MANAGER.getId().equals(getId());
	}
}
