package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

public class PlaceCardEvent extends FeedbackEvent {

    private final static String id = "PLACE_CARD";

    public PlaceCardEvent() {
        super(id);
    }

    public PlaceCardEvent(Feedback feedback) {
        super(id, feedback);
    }
}
