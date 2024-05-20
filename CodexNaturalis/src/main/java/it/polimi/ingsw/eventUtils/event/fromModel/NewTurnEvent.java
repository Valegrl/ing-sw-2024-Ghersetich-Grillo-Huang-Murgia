package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

public class NewTurnEvent extends Event {

    private final static String id = EventID.NEW_TURN.getID();

    private final int turnIndex;

    private final GameStatus gameStatus;

    public NewTurnEvent(int index, GameStatus gameStatus) {
        super(id);
        this.turnIndex = index;
        this.gameStatus = gameStatus;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

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
