package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class ChooseSetupEvent extends FeedbackEvent {

    private final static String id = "CHOOSE_SETUP";

    public ChooseSetupEvent() {
        super(id);
    }

    public ChooseSetupEvent(Feedback feedback) {
        super(id, feedback);
    }
}
