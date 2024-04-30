package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class KickFromLobbyEvent extends FeedbackEvent {

    private final static String id = "KICK_FROM_LOBBY";

    public KickFromLobbyEvent() {
        super(id);
    }

    public KickFromLobbyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
    }
}
