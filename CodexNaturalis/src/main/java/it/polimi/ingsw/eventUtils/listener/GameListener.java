package it.polimi.ingsw.eventUtils.listener;

import it.polimi.ingsw.eventUtils.event.Event;

/**
 * The GameListener interface defines the methods that a class must implement to receive and handle game events.
 * These events are related to various actions performed within the game.
 */
public interface GameListener {
    /**
     * Notifies the listener with the given event.
     * @param event The event to be handled.
     */
    void update(Event event);
}
