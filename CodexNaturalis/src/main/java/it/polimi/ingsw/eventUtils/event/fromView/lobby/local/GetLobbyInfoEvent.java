package it.polimi.ingsw.eventUtils.event.fromView.lobby.local;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that requests the lobby information.
 */
public class GetLobbyInfoEvent extends FeedbackEvent {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.GET_LOBBY_INFO.getID();

    /**
     * Default constructor. Initializes the event with the unique identifier.
     */
    public GetLobbyInfoEvent() {
        super(id);
    }

    /**
     * Constructor. Initializes the event with the specified id, feedback and message.
     *
     * @param feedback The feedback of the event.
     * @param message The message of the event.
     */
    public GetLobbyInfoEvent(Feedback feedback, String message) {
        super(id, feedback, message);
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
