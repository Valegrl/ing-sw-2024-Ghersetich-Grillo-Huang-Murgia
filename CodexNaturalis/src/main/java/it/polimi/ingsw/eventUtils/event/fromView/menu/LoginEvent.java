package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class LoginEvent extends FeedbackEvent {

    private final static String id = "LOGIN";

    public LoginEvent() {
        super(id);
    }

    public LoginEvent(Feedback feedback) {
        super(id, feedback);
    }
}
