package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * JEvent provides a powerful and lightweight event system based on the syntax of the <a href="https://www.spigotmc.org/wiki/using-the-event-api/">Spigot event</a> system
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
public final class JEvent implements EventManager {

	static final EventManager MANAGER = new JEvent();

	/**
	 * @return The EventManager instance
	 */
	public static EventManager getManager() {
		return MANAGER;
	}

	private final Collection<Handler> HANDLER_LIST = new ArrayList<>();

	private JEvent() {}

	private Collection<Handler> getHandlers(Class<? extends Event> event) {
		return HANDLER_LIST.stream()
				.filter(h -> h.isSuitableHandler(event))
				.sorted()
				.toList();
	}


	public void registerListener(@NotNull Object obj) {
		HANDLER_LIST.addAll(Handler.getHandlers(obj));
	}

	public void unregisterListener(@NotNull Class<?> clazz) {
		HANDLER_LIST.removeIf(h -> h.getListener().getClass().isInstance(clazz));
	}

	public void unregisterAll() {
		HANDLER_LIST.clear();
	}

	public void callEvent(@NotNull Event event) {
		getHandlers(event.getClass()).forEach(h -> h.invoke(event));
	}

}
