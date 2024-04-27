package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class QuitLobbyEvent extends FeedbackEvent {

    private final static String id = "QUIT_LOBBY";

    public QuitLobbyEvent() {
        super(id);
    }

    public QuitLobbyEvent(Feedback feedback) {
        super(id, feedback);
    }
}
