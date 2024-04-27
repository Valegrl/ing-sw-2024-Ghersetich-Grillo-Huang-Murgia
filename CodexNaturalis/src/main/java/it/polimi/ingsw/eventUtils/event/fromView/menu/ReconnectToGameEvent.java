package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class ReconnectToGameEvent extends FeedbackEvent {

    private final static String id = "RECONNECT_TO_GAME";

    public ReconnectToGameEvent() {
        super(id);
    }

    public ReconnectToGameEvent(Feedback feedback) {
        super(id, feedback);
    }
}
