package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

public class AvailablePositionsEvent extends FeedbackEvent {

    private final static String id = "AVAILABLE_POSITIONS";

    public AvailablePositionsEvent() {
        super(id);
    }

    public AvailablePositionsEvent(Feedback feedback, String message) {
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
