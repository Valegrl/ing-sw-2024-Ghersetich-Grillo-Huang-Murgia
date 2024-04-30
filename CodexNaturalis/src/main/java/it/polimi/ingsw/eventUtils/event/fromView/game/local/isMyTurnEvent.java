package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class isMyTurnEvent extends FeedbackEvent {

    private final static String id = "IS_MY_TURN";

    public isMyTurnEvent() {
        super(id);
    }

    public isMyTurnEvent(Feedback feedback, String message) {
        super(id, feedback, message);
    }
}
