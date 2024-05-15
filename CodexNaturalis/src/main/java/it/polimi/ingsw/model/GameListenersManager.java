package it.polimi.ingsw.model;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.eventUtils.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class manages the game listeners for each player in the game.
 * It is responsible for changing, notifying, and notifying all listeners.
 */
public class GameListenersManager {
    /**
     * The game instance associated with this manager.
     */
    private final Game game;

    /**
     * A map of listeners associated with each player's username.
     */
    private final Map<String, GameListener> listeners;

    /**
     * Constructs a new GameListenersManager with the given game and listeners.
     *
     * @param game the game instance associated with this manager
     * @param listeners a map of listeners associated with each player's username
     */
    public GameListenersManager(Game game, Map<String, GameListener> listeners) {
        this.game = game;
        this.listeners = new HashMap<>(listeners);
    }

    /**
     * Changes the listener associated with the given username.
     *
     * @param username the username of the player whose listener is to be changed
     * @param listener the new listener to be associated with the username
     */
    public void changeListener(String username, GameListener listener) {
        this.listeners.put(username, listener);
    }

    /**
     * Notifies the listener associated with the given username with the given event.
     *
     * @param username the username of the player whose listener is to be notified
     * @param event the event to be sent to the listener
     */
    public void notifyListener(String username, Event event) {
        GameListener listener = this.listeners.get(username);
        if (listener != null) {
            if (game.getOnlinePlayers().contains(username))
                listener.update(event);
            /*else
                log?
            */
        } else {
            throw new IllegalArgumentException("No listener found for the provided username");
        }
    }

    /**
     * Notifies all listeners except the one associated with the given username with the given event.
     *
     * @param username the username of the player whose listener is not to be notified
     * @param event the event to be sent to all other listeners
     */
    public void notifyAllExceptOne(String username, Event event) {
        Set<String> onlinePlayers = game.getOnlinePlayers();
        for (Map.Entry<String, GameListener> entry : this.listeners.entrySet()) {
            if (!entry.getKey().equals(username) && onlinePlayers.contains(entry.getKey())) {
                GameListener listener = entry.getValue();
                if (listener != null)
                    listener.update(event);
                else
                    throw new IllegalArgumentException("No listener found for the provided username");
            }
        }
    }

    /**
     * Notifies all listeners with the given event.
     *
     * @param event the event to be sent to all listeners
     */
    public void notifyAllListeners(Event event) {
        Set<String> onlinePlayers = game.getOnlinePlayers();
        for (Map.Entry<String, GameListener> entry : this.listeners.entrySet()) {
            if (onlinePlayers.contains(entry.getKey())) {
                GameListener listener = entry.getValue();
                if (listener != null)
                    listener.update(event);
                else
                    throw new IllegalArgumentException("No listener found for the provided username");
            }
        }
    }
}
