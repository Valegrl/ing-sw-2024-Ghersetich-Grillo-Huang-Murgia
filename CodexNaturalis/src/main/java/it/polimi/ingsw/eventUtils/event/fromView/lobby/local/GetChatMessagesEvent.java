package it.polimi.ingsw.eventUtils.event.fromView.lobby.local;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

public class GetChatMessagesEvent extends FeedbackEvent {

    private final static String id = EventID.GET_CHAT_MESSAGES.getID();

    /**
     * Constructor. Initializes the event with the specified id.
     */
    public GetChatMessagesEvent() {
        super(id);
    }

    /**
     * Constructor. Initializes the event with the specified id, feedback and message.
     *
     * @param feedback The feedback of the event.
     * @param message The message of the event.
     */
    public GetChatMessagesEvent(Feedback feedback, String message) {
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
