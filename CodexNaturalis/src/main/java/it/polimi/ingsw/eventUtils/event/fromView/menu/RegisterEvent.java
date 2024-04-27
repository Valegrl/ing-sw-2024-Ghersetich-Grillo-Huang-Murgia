package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class RegisterEvent extends FeedbackEvent {

    private final static String id = "REGISTER";

    public RegisterEvent() {
        super(id);
    }

    public RegisterEvent(Feedback feedback) {
        super(id, feedback);
    }
}
