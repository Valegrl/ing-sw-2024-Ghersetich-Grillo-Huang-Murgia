package it.polimi.ingsw.model;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.eventUtils.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameListenersManager {
    private final Game game;
    private final Map<String, GameListener> listeners;

    public GameListenersManager(Game game, Map<String, GameListener> listeners) {
        this.game = game;
        this.listeners = new HashMap<>(listeners);
    }

    public void changeListener(String username, GameListener listener) {
        this.listeners.put(username, listener);
    }

    public void notifyListener(String username, Event event) {
        GameListener listener = this.listeners.get(username);
        if (listener != null) {
            if (game.getOnlinePlayers().contains(username))
                listener.update(event);
            else
                throw new IllegalArgumentException("The player is not online");
        } else {
            throw new IllegalArgumentException("No listener found for the provided username");
        }
    }

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
