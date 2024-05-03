package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an event that is triggered when the offline games of a specific user are requested.
 * These are the games from which the user was disconnected due to network issues, not due to quitting the game.
 */
public class GetMyOfflineGamesEvent extends FeedbackEvent {

    /**
     * The unique identifier for a GetMyOfflineGamesEvent.
     */
    private final static String id = EventID.GET_MY_OFFLINE_GAMES.getID();

    /**
     * The list of offline games.
     */
    private final List<LobbyState> games;

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and initializes the games list.
     */
    public GetMyOfflineGamesEvent() {
        super(id);
        games = new ArrayList<>();
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     * It also initializes the games list with the provided list.
     *
     * @param feedback The feedback for the event.
     * @param games The list of games.
     * @param message The message for the event.
     */
    public GetMyOfflineGamesEvent(Feedback feedback, List<LobbyState> games, String message) {
        super(id, feedback, message);
        this.games = new ArrayList<>(games);
    }

    /**
     * @return A new list containing the games that the user was disconnected from due to network issues.
     */
    public List<LobbyState> getGames() {
        return games;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {
        viewEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
