package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * The NewTurnEvent class represents an event that occurs when a new turn starts in the game.
 */
public class NewTurnEvent extends Event {

    /**
     * The id of the event.
     */
    private final static String id = EventID.NEW_TURN.getID();

    /**
     * The index of the turn.
     */
    private final int turnIndex;

    /**
     * The current status of the game.
     */
    private final GameStatus gameStatus;

    /**
     * Constructs a new NewTurnEvent with the given turn index and game status.
     *
     * @param index The index of the turn.
     * @param gameStatus The current status of the game.
     */
    public NewTurnEvent(int index, GameStatus gameStatus) {
        super(id);
        this.turnIndex = index;
        this.gameStatus = gameStatus;
    }

    /**
     * @return The index of the turn.
     */
    public int getTurnIndex() {
        return turnIndex;
    }

    /**
     * @return The current status of the game.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
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
