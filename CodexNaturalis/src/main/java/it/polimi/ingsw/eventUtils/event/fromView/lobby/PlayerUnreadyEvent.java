package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class PlayerUnreadyEvent extends FeedbackEvent {

    private final static String id = "PLAYER_UNREADY";

    public PlayerUnreadyEvent() {
        super(id);
    }

    public PlayerUnreadyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
    }
}
