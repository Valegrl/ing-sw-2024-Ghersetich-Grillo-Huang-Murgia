package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class CreateLobbyEvent extends FeedbackEvent {

    private final static String id = "CREATE_LOBBY";

    public CreateLobbyEvent() {
        super(id);
    }

    public CreateLobbyEvent(Feedback feedback) {
        super(id, feedback);
    }
}
