package de.cyklon.jevent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark methods as being event handler methods
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
	byte LOWEST = Byte.MIN_VALUE;
	byte LOW = -100;
	byte NORMAL = 0;
	byte HIGH = 100;
	byte HIGHEST = Byte.MAX_VALUE;

	/**
	 * Define the priority of the event.
	 * <p>
	 * Lowest priority to the Highest priority executed.
	 *
	 * @return the priority
	 */
	byte priority() default NORMAL;

	/**
	 * Define if the handler ignores a canceled event.
	 * <p>
	 * If ignoreCancelled is true and the event is cancelled, the method is still called.
	 * Otherwise, the method is only called if the event is not cancelled.
	 *
	 * @return whether canceled events should be ignored
	 */
	boolean ignoreCancelled() default false;
}
