package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class DrawCardEvent extends FeedbackEvent {

    private final static String id = "DRAW_CARD";

    public DrawCardEvent() {
        super(id);
    }

    public DrawCardEvent(Feedback feedback) {
        super(id, feedback);
    }
}
