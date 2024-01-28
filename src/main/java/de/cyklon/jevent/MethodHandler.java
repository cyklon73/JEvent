package de.cyklon.jevent;

import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.members.ReflectMethod;
import de.cyklon.reflection.entities.members.ReflectParameter;
import de.cyklon.reflection.function.Filter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The MethodHandler object represents a single event handler method
 * <p>
 * <h2 id="method-handler">What is a EventHandler?</h2>
 * An event handler method is a method that is annotated with the {@link EventHandler} annotation
 *
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
class MethodHandler<D> extends Handler<Event> {
	private final D listener;
	private final ReflectMethod<D, ?> handler;
	private final String[] parameterInstances;
	private final int eventIndex;

	@SuppressWarnings("unchecked")
	private MethodHandler(@NotNull D listener, @NotNull ReflectMethod<D, ?> handler, byte priority, boolean ignoreCancelled) {
		super(null, priority, ignoreCancelled);
		this.listener = listener;
		this.handler = handler;

		List<String> pInstances = new ArrayList<>();
		int eventIndex = -1;
		List<? extends ReflectParameter<D, ?>> parameters = handler.getParameters();
		for(int i = 0; i < parameters.size(); i++) {
			ReflectParameter<D, ?> parameter = parameters.get(i);
			Class<?> c = parameter.getReturnType().getInternal();
			if(eventIndex==-1 && Event.class.isAssignableFrom(c)) {
				this.eventType = (Class<? extends Event>) c;
				eventIndex = i;
			} else {
				ParameterInstance pi = parameter.getAnnotation(ParameterInstance.class);
				if (pi==null) {
					if (EventManager.class.equals(c)) pInstances.add(null);
					else pInstances.add(c.getName());
				}
				else pInstances.add(pi.value());
			}
		}
		if(eventIndex==-1) throw new EventException("the method must have an event as a parameter!");

		this.eventIndex = eventIndex;
		this.parameterInstances = pInstances.toArray(String[]::new);
	}

	@NotNull
	public Object getListener() {
		return listener;
	}

	@Override
	protected void invokeEvent(@NotNull EventManager manager, @NotNull Event event) {
		Object[] params = new Object[parameterInstances.length+1];
		for (int i = 0; i < params.length; i++) {
			int i1 = i;
			if (i>=eventIndex) i1--;
			params[i] = i==eventIndex ? event : parameterInstances[i1]==null ? manager : manager.getParameterInstance(parameterInstances[i1]);
		}
		handler.invoke(listener, params);
	}

	@NotNull
	public static <D> Collection<MethodHandler<?>> getHandlers(@NotNull D listener) {
		Set<? extends ReflectMethod<D, ?>> methods = ReflectClass.getClass(listener).getMethods(Filter.all());
		List<MethodHandler<?>> handlers = new LinkedList<>();

		for(ReflectMethod<D, ?> handler : methods) {
			EventHandler annotation = handler.getAnnotation(EventHandler.class);
			if(annotation == null) continue;

			handlers.add(new MethodHandler<>(listener, handler, annotation.priority(), annotation.ignoreCancelled()));
		}

		return handlers;
	}
}
