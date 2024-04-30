package it.polimi.ingsw.eventUtils.listener;

import it.polimi.ingsw.eventUtils.event.Event;

/**
 * The ViewListener interface defines the methods that a class must implement to receive and handle view events.
 * These events are related to various actions performed within the view.
 */
public interface ViewListener {
    /**
     * Notifies the listener with the given event.
     * @param event The event to be handled.
     */
    void handle(Event event);
}
