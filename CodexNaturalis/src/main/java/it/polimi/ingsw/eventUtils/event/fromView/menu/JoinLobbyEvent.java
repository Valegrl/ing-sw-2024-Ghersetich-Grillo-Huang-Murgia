package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class JoinLobbyEvent extends FeedbackEvent {

    private final static String id = "JOIN_LOBBY";

    public JoinLobbyEvent() {
        super(id);
    }

    public JoinLobbyEvent(Feedback feedback) {
        super(id, feedback);
    }
}
