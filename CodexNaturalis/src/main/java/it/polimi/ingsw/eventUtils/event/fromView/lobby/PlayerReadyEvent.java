package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class PlayerReadyEvent extends FeedbackEvent {

    private final static String id = "PLAYER_READY";

    public PlayerReadyEvent() {
        super(id);
    }

    public PlayerReadyEvent(Feedback feedback) {
        super(id, feedback);
    }
}
