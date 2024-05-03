package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event that retrieves the list of offline games for a user.
 * Each game is represented as a pair, where the key is the game name and the value is another pair.
 * This inner pair represents the number of online players and the maximum number of players.
 * The offline games are those from which the user was disconnected due to network issues.
 */
public class GetMyOfflineGamesEvent extends FeedbackEvent<List<Pair<String, Pair<Integer, Integer>>>> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = EventID.GET_MY_OFFLINE_GAMES.getID();

    /**
     * A list of offline games. Each game is represented as a pair, where the key is the game name
     * and the value is another pair representing the number of online players and the maximum number of players.
     */
    private final List<Pair<String, Pair<Integer, Integer>>> games;

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
     * @param games The list of offline games. Each game is represented as a pair, where the key
     *              is the game name and the value is another pair representing the number of online players and the
     *              maximum number of players.
     * @param message The message associated with the event.
     */
    public GetMyOfflineGamesEvent(Feedback feedback, List<Pair<String, Pair<Integer, Integer>>> games, String message) {
        super(id, feedback, message);
        this.games = new ArrayList<>(games);
    }

    /**
     * Retrieves the data associated with this event, which is a list of offline games for the user.
     * Each game is represented as a pair, where the key is the game name and the value is another
     * pair representing the number of online players and the maximum number of players.
     *
     * @return A copy of the list of offline games.
     */
    @Override
    public List<Pair<String, Pair<Integer, Integer>>> getData() {
        return new ArrayList<>(games);
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
