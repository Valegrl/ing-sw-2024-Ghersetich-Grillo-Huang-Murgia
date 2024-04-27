package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class QuitGameEvent extends FeedbackEvent {

    private final static String id = "QUIT_GAME";

    public QuitGameEvent() {
        super(id);
    }

    public QuitGameEvent(Feedback feedback) {
        super(id, feedback);
    }
}
