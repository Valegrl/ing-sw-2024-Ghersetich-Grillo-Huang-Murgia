package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;

public class LogoutEvent extends FeedbackEvent<Object> {

    private final static String id = "LOGOUT";

    public LogoutEvent() {
        super(id);
    }

    public LogoutEvent(Feedback feedback, String message) {
        super(id, feedback, message);
    }
}
