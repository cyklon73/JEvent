package de.cyklon.jevent;

/**
 * This interface can be implemented to make an event cancelable
 * <p>
 * If the event is marked as cancelled, all listeners that would be executed afterwards will not be executed unless ignoreCancelled is set to true in the @{@link EventHandler} annotation of the handler.
 */
public interface Cancellable {
	/**
	 * returns true if the event was marked as canceled
	 * @return true if event was canceled
	 */
	boolean isCancelled();

	/**
	 * sets the canceled status of the event to the specified value
	 * @param cancelled the new status
	 */
	void setCancelled(boolean cancelled);
}
