package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class GetMyOfflineGamesEvent extends FeedbackEvent {

    private final static String id = "GET_MY_OFFLINE_GAMES";

    public GetMyOfflineGamesEvent() {
        super(id);
    }

    public GetMyOfflineGamesEvent(Feedback feedback) {
        super(id, feedback);
    }
}
