package de.cyklon.jevent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotate a class in a Listener package to register the class
 * <p>
 * annotate a package in a lístener package (or the listener package itself) to register all classes inside of it without annotate every single class
 */
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

	boolean includeSubclasses() default true;

}
