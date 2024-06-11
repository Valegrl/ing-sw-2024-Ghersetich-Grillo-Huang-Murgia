package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import it.polimi.ingsw.viewModel.ViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an event that is triggered when a user attempts to reconnect to a game.
 */
public class ReconnectToGameEvent extends FeedbackEvent {

    /**
     * The unique identifier for a ReconnectToGameEvent.
     */
    private final static String id = EventID.RECONNECT_TO_GAME.getID();

    /**
     * The ID of the game to reconnect to.
     */
    private final String gameID;

    /**
     * The ViewModel associated with the current game state.
     */
    private final ViewModel viewModel;

    /**
     * The current status of the game.
     */
    private final GameStatus gameStatus;

    /**
     * The map of online players in the game.
     */
    private final Map<String, Boolean> players;

    /**
     * This is true if the game status is {@link GameStatus#SETUP} and the token setup has not yet started.
     */
    private final boolean autoSetup;

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and initializes the game ID.
     *
     * @param gameID The ID of the game to reconnect to.
     */
    public ReconnectToGameEvent(String gameID) {
        super(id);
        this.gameID = gameID;
        this.gameStatus = null;
        this.viewModel = null;
        this.players = null;
        this.autoSetup = false;
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     *
     * @param feedback The feedback for the event.
     * @param gameID The ID of the game to reconnect to.
     * @param message The message for the event.
     */
    public ReconnectToGameEvent(Feedback feedback, String gameID, String message) {
        super(id, feedback, message);
        this.gameID = gameID;
        this.gameStatus = null;
        this.viewModel = null;
        this.players = null;
        this.autoSetup = false;
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     *
     * @param feedback The feedback for the event.
     * @param gameID The ID of the game to reconnect to.
     * @param status The current status of the game.
     * @param autoSetup This is true if the game status is {@link GameStatus#SETUP} and the token setup has not yet started.
     * @param model The ViewModel associated with the current game state.
     * @param pl The map of online players in the game.
     * @param message The message for the event.
     */
    public ReconnectToGameEvent(Feedback feedback, String gameID, GameStatus status, boolean autoSetup, ViewModel model, Map<String, Boolean> pl, String message) {
        super(id, feedback, message);
        this.gameID = gameID;
        this.gameStatus = status;
        this.viewModel = model;
        this.players = new HashMap<>(pl);
        this.autoSetup = autoSetup;
    }

    /**
     * @return The ID of the game to reconnect to.
     */
    public String getGameID() {
        return gameID;
    }

    /**
     * @return A copy of the map of online players in the game.
     */
    public Map<String, Boolean> getPlayers() {
        return players;
    }

    /**
     * @return The ViewModel of the current game state.
     */
    public ViewModel getViewModel() {
        return viewModel;
    }

    /**
     * @return The current status of the game.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * @return true if the game status is {@link GameStatus#SETUP} and the token setup has not yet started, false otherwise.
     */
    public boolean isAutoSetup() {
        return autoSetup;
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
