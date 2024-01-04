package de.cyklon.jevent;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The MethodHandler object represents a single event handler method
 * <p>
 * <h2 id="method-handler">What is a EventHandler?</h2>
 * An event handler method is a method that is annotated with the {@link EventHandler} annotation
 *
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
class MethodHandler extends Handler<Event> {
	private final Object listener;
	private final Method handler;

	@SuppressWarnings("unchecked")
	private MethodHandler(@NotNull Object listener, @NotNull Method handler, byte priority, boolean ignoreCancelled) {
		super(null, priority, ignoreCancelled);
		this.listener = listener;
		this.handler = handler;
		this.handler.setAccessible(true);

		for(Class<?> parameterType : handler.getParameterTypes()) {
			if(Event.class.isAssignableFrom(parameterType)) {
				this.eventType = (Class<? extends Event>) parameterType;
				break;
			}
		}

		if(eventType == null) throw new EventException("the method must have an event as a parameter!");
	}

	@NotNull
	public Object getListener() {
		return listener;
	}

	@Override
	protected void invokeEvent(Event event) {
		try {
			handler.invoke(listener, event);
		} catch(IllegalAccessException | InvocationTargetException e) {
			throw new EventException(e);
		}
	}

	@NotNull
	public static Collection<MethodHandler> getHandlers(@NotNull Object listener) {
		Method[] methods = listener.getClass().getDeclaredMethods();
		List<MethodHandler> handlers = new LinkedList<>();

		for(Method handler : methods) {
			EventHandler annotation = handler.getAnnotation(EventHandler.class);
			if(annotation == null) continue;

			handlers.add(new MethodHandler(listener, handler, annotation.priority(), annotation.ignoreCancelled()));
		}

		return handlers;
	}
}
