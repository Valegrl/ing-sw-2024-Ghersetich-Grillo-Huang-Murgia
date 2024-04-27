package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class seeOpponentPlayAreaEvent extends FeedbackEvent {

    private final static String id = "SEE_OPPONENT_PLAY_AREA";

    public seeOpponentPlayAreaEvent() {
        super(id);
    }

    public seeOpponentPlayAreaEvent(Feedback feedback) {
        super(id, feedback);
    }
}
