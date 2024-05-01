package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

public class SeeOpponentPlayAreaEvent extends FeedbackEvent {

    private final static String id = "SEE_OPPONENT_PLAY_AREA";

    public SeeOpponentPlayAreaEvent() {
        super(id);
    }

    public SeeOpponentPlayAreaEvent(Feedback feedback, String message) {
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
