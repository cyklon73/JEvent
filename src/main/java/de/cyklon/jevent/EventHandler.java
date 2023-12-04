package de.cyklon.jevent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark methods as being event handler methods
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

	byte LOWEST = -2;
	byte LOW = -1;
	byte NORMAL = 0;
	byte HIGH = 1;
	byte HIGHEST = 2;
	byte MONITOR = 3;


	/**
	 * Define the priority of the event.
	 * <p>
	 * Lowest priority to the Highest priority executed.
	 * @return the priority
	 */
	byte priority() default NORMAL;

	/**
	 * Define if the handler ignores a cancelled event.
	 * <p>
	 * If ignoreCancelled is true and the event is cancelled, the method is
	 * not called. Otherwise, the method is always called.
	 *
	 * @return whether cancelled events should be ignored
	 */
	boolean ignoreCancelled() default false;


}
