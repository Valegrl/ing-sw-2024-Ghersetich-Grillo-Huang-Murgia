package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import it.polimi.ingsw.viewModel.EndedGameData;

/**
 * The EndedGameEvent class represents an event that occurs when a game ends.
 * It includes the data of the ended game.
 */
public class EndedGameEvent extends Event {

    /**
     * The id of the event.
     */
    private final static String id = EventID.ENDED_GAME.getID();

    /**
     * The data of the ended game.
     */
    private final EndedGameData endedGameData;

    /**
     * Constructs a new EndedGameEvent with the given ended game data.
     *
     * @param egd The data of the ended game.
     */
    public EndedGameEvent(EndedGameData egd) {
        super(id);
        this.endedGameData = egd;
    }

    /**
     * @return The data of the ended game.
     */
    public EndedGameData getEndedGameData() {
        return endedGameData;
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

