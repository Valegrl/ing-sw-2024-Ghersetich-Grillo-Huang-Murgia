package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event that retrieves the list of offline games for a user.
 * This event is a type of FeedbackEvent, which carries a list of game names as data.
 */
public class GetMyOfflineGamesEvent extends FeedbackEvent<List<String>> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = "GET_MY_OFFLINE_GAMES";

    /**
     * A list of offline games.
     */
    private final List<String> games;

    /**
     * Constructor for View (client). Initializes the list of games to an empty list.
     */
    public GetMyOfflineGamesEvent() {
        super(id);
        games = new ArrayList<>();
    }

    /**
     * Constructor for Controller (server). Initializes the list of games with the provided list.
     *
     * @param feedback The feedback associated with the event.
     * @param games The list of offline games.
     * @param message The message associated with the event.
     */
    public GetMyOfflineGamesEvent(Feedback feedback, List<String> games, String message) {
        super(id, feedback, message);
        this.games = new ArrayList<>(games);
    }

    @Override
    public List<String> getData() {
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
