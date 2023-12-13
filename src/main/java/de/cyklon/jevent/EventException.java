package de.cyklon.jevent;


/**
 * is called when an error occurs in the event system
 *
 * @author <a href="https://github.com/cyklon73">Cyklon73</a>
 */
public class EventException extends RuntimeException {
	/**
	 * Constructs a new EventException based on the given Exception
	 *
	 * @param throwable Exception that triggered this Exception
	 */
	public EventException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructs a new EventException
	 */
	public EventException() {
	}

	/**
	 * Constructs a new EventException with the given message
	 *
	 * @param cause   The exception that caused this
	 * @param message The message
	 */
	public EventException(Throwable cause, String message) {
		super(message, cause);
	}

	/**
	 * Constructs a new EventException with the given message
	 *
	 * @param message The message
	 */
	public EventException(String message) {
		super(message);
	}
}
