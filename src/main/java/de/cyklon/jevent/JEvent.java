package de.cyklon.jevent;

import de.cyklon.reflection.entities.OfflinePackage;
import de.cyklon.reflection.entities.ReflectClass;
import de.cyklon.reflection.entities.ReflectPackage;
import de.cyklon.reflection.entities.members.ReflectConstructor;
import de.cyklon.reflection.entities.members.ReflectParameter;
import de.cyklon.reflection.function.Filter;
import de.cyklon.reflection.function.Sorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * JEvent provides a powerful and lightweight event system based on the syntax of the <a href="https://www.spigotmc.org/wiki/using-the-event-api/">Spigot event</a> system
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

	private final Collection<Handler<?>> handlers = new HashSet<>();
	private final Map<String, Object> parameterInstances = new HashMap<>();
	private Consumer<String> logger = null;

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
		debug("register listener " + obj.getClass());
		Collection<MethodHandler<?>> handlers = MethodHandler.getHandlers(obj);
		this.handlers.addAll(handlers);
		if (isDebugEnabled()) {
			debug(String.format("%s handlers registered for listener %s:", handlers.size(), obj.getClass()));
			handlers.forEach(this::debug);
		}
	}

	@Override
	public void registerListener(@NotNull Class<?> clazz) {
		registerListener0(ReflectClass.wrap(clazz));
	}

	@Override
	public void registerListener(@NotNull ReflectClass<?> clazz) {
		registerListener0(clazz);
	}

	private <D> void registerListener0(@NotNull ReflectClass<D> clazz) {
		debug("search for suitable constructor in " + clazz);
		Optional<? extends ReflectConstructor<D>> constructor = clazz.getConstructors(Filter.hasNoArgs()).stream().min(Sorter.byModifier());
		D instance = null;
		if (constructor.isPresent()) {
			debug("constructor found: " + constructor.get());
			instance = constructor.get().newInstance();
		}
		else {
			constructor = clazz.getConstructors(c -> {
				List<? extends ReflectParameter<?, ?>> parameters = c.getParameters();
				for (ReflectParameter<?, ?> parameter : parameters) {
					if (!parameter.hasAnnotation(ParameterInstance.class)) return false;
				}
				return true;
			}).stream().findFirst();
			if (constructor.isEmpty()) constructor = clazz.getConstructors(Filter.all()).stream().findFirst();
			if (constructor.isPresent()) {
				debug("constructor found: " + constructor.get());
				ReflectConstructor<D> con = constructor.get();
				List<? extends ReflectParameter<D, ?>> parameters = con.getParameters();
				Object[] params = new Object[parameters.size()];
				for (int i = 0; i < parameters.size(); i++) {
					ReflectParameter<D, ?> param = parameters.get(i);
					Class<?> internal = param.getReturnType().getInternal();
					Object obj;
					ParameterInstance pi;
					if (EventManager.class.equals(internal)) obj = this;
					else if ((pi = param.getAnnotation(ParameterInstance.class)) != null) obj = getParameterInstance(pi.value());
					else obj = getParameterInstance(internal.getName());
					params[i] = obj;
				}
				instance = con.newInstance(params);
			}
		}

		if (instance!=null) registerListener(instance);
		else debug("cannot register listener " + clazz);
	}

	@Override
	public void registerListenerPackage(OfflinePackage pkg) {
		debug("register listener package " + pkg);
		pkg.loadRecursive().forEach(this::processPackage);
	}

	private void processPackage(ReflectPackage pkg) {
		Listener listener;
		if ((listener = pkg.getAnnotation(Listener.class)) != null) pkg.getLoadedClasses().forEach(c -> processClass(c, listener.includeSubclasses()));
		else {
			pkg.getLoadedClasses().stream()
					.filter(c -> c.hasAnnotation(Listener.class))
					.forEach(c -> processClass(c, false));
		}
	}

	private void processClass(ReflectClass<?> clazz, boolean includeSubclasses) {
		registerListener0(clazz);
		Listener listener;
		if (includeSubclasses || (((listener = clazz.getAnnotation(Listener.class)) != null) && listener.includeSubclasses())) clazz.getSubclasses(Filter.all()).forEach(c -> processClass(c, true));
	}

	@Override
	public <T extends Event> void registerHandler(@NotNull Class<T> event, Consumer<T> handler, byte priority, boolean ignoreCancelled) {
		RawHandler<T> rh = new RawHandler<>(event, handler, priority, ignoreCancelled);
		debug("register handler " + rh);
		handlers.add(rh);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void unregisterListener(@NotNull Class<?> clazz) {
		if (handlers.removeIf(h -> h instanceof MethodHandler mh && mh.getListener().getClass().isInstance(clazz))) debug("unregistered listener " + clazz);
		else debug("listener not unregistered because no listener matches " + clazz);
	}

	@Override
	public void unregisterAll() {
		debug("unregister all handlers");
		handlers.clear();
	}

	@Override
	public boolean callEvent(@NotNull Event event) {
		debug("call event " + event.getClass());
		getHandlers(event.getClass()).forEach(h -> h.invoke(this, event));
		return event instanceof Cancellable ce && ce.isCancelled();
	}

	@Override
	public void registerParameterInstance(@NotNull String key, Object instance) {
		debug("register parameter instance %s: %s".formatted(key, instance));
		parameterInstances.put(key, instance);
	}

	@Override
	public @Nullable Object removeParameterInstance(String key) {
		debug("remove parameter instance " + key);
		return parameterInstances.remove(key);
	}

	@Override
	public @Nullable Object getParameterInstance(String key) {
		return parameterInstances.get(key);
	}

	@Override
	public void setDebugLogger(@Nullable Consumer<String> logger) {
		this.logger = logger;
	}

	private boolean isDebugEnabled() {
		return logger!=null;
	}

	private void debug(String msg) {
		if (isDebugEnabled()) logger.accept(msg);
	}

	private void debug(Object obj) {
		debug(obj.toString());
	}
}
