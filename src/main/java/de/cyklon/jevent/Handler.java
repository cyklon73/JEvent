package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The handler object represents a single event listener method
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
class Handler implements Comparable<Handler> {

	private final Object listener;
	private final Method handler;
	private Class<? extends Event> eventType = null;
	private final byte priority;
	private final boolean ignoreCancelled;

	@SuppressWarnings("unchecked")
	private Handler(Object listener, Method handler, byte priority, boolean ignoreCancelled) {
		this.listener = listener;
		this.handler = handler;
		this.handler.setAccessible(true);
		for (Class<?> parameterType : handler.getParameterTypes()) {
			if (Event.class.isAssignableFrom(parameterType)) {
				this.eventType = (Class<? extends Event>) parameterType;
				break;
			}
		}
		if (eventType==null) throw new EventException("the method must have an event as a parameter!");
		this.priority = priority;
		this.ignoreCancelled = ignoreCancelled;
	}

	public Object getListener() {
		return listener;
	}

	public boolean isSuitableHandler(Class<? extends  Event> event) {
		return eventType.isAssignableFrom(event);
	}

	public void invoke(Event event) {
		if (event instanceof Cancellable && ((Cancellable)event).isCancelled() && !ignoreCancelled) return;
		if (isSuitableHandler(event.getClass())) {
			try {
				handler.invoke(listener, event);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new EventException(e);
			}
		}
	}

	@Override
	public int compareTo(@NotNull Handler o) {
		return Byte.compare(o.priority, this.priority);
	}

	public static Collection<Handler> getHandlers(Object listener) {
		Method[] methods = listener.getClass().getDeclaredMethods();
		List<Handler> handlers = new LinkedList<>();
		for (Method handler : methods) {
			EventHandler annotation = handler.getAnnotation(EventHandler.class);
			if (annotation==null) continue;
			handlers.add(new Handler(listener, handler, annotation.priority(), annotation.ignoreCancelled()));
		}
		return handlers;
	}
}
