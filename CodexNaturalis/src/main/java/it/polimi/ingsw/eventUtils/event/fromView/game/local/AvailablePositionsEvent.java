package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class AvailablePositionsEvent extends FeedbackEvent {

    private final static String id = "AVAILABLE_POSITIONS";

    public AvailablePositionsEvent() {
        super(id);
    }

    public AvailablePositionsEvent(Feedback feedback) {
        super(id, feedback);
    }
}
