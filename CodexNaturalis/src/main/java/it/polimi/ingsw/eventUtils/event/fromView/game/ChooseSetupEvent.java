package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.UIEventReceiver;

public class ChooseSetupEvent extends FeedbackEvent {

    private final static String id = "CHOOSE_SETUP";

    public ChooseSetupEvent() {
        super(id);
    }

    public ChooseSetupEvent(Feedback feedback, String message) {
        super(id, feedback, message);
    }

    @Override
    public void receiveEvent(UIEventReceiver uiEventReceiver) {
        uiEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
