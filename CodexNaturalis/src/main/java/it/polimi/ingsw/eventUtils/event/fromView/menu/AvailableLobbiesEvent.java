package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class AvailableLobbiesEvent extends FeedbackEvent {

    private final static String id = "AVAILABLE_LOBBIES";

    public AvailableLobbiesEvent() {
        super(id);
    }

    public AvailableLobbiesEvent(Feedback feedback) {
        super(id, feedback);
    }
}
