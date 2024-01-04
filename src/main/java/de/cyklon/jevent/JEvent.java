package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * JEvent provides a powerful and lightweight event system based on the syntax of the <a href="https://www.spigotmc.org/wiki/using-the-event-api/">Spigot event</a> system
 *
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
public final class JEvent implements EventManager {
	static final EventManager DEFAULT_MANAGER = new JEvent();

	/**
	 * @return The Default EventManager instance
	 */
	@NotNull
	public static EventManager getDefaultManager() {
		return DEFAULT_MANAGER;
	}

	/**
	 * create a new EventManager
	 * @return the new Created Manager
	 */
	@NotNull
	public static EventManager createManager() {
		return new JEvent();
	}

	private final Collection<Handler<?>> handlers = new ArrayList<>();

	private JEvent() {}

	@NotNull
	@SuppressWarnings("unchecked")
	private <T extends Event> Collection<Handler<T>> getHandlers(@NotNull Class<T> event) {
		return handlers.stream()
				.filter(h -> h.isSuitableHandler(event))
				.map(h -> (Handler<T>) h)
				.sorted()
				.toList();
	}

	@Override
	public void registerListener(@NotNull Object obj) {
		handlers.addAll(MethodHandler.getHandlers(obj));
	}

	@Override
	public <T extends Event> void registerHandler(@NotNull Class<T> event, Consumer<T> handler, byte priority, boolean ignoreCancelled) {
		handlers.add(new RawHandler<>(event, handler, priority, ignoreCancelled));
	}

	@Override
	public void unregisterListener(@NotNull Class<?> clazz) {
		handlers.removeIf(h -> h instanceof MethodHandler mh && mh.getListener().getClass().isInstance(clazz));
	}

	@Override
	public void unregisterAll() {
		handlers.clear();
	}

	@Override
	public boolean callEvent(@NotNull Event event) {
		getHandlers(event.getClass()).forEach(h -> h.invoke(event));
		return event instanceof Cancellable ce && ce.isCancelled();
	}
}
