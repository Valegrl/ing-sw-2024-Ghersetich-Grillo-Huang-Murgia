package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.eventUtils.event.Event;

public abstract class FeedbackEvent extends Event {
    private Feedback feedback = null;

    public Feedback getResponse() { return feedback; }

    public FeedbackEvent(String id) {
        super(id);
    }

    public FeedbackEvent(String id, Feedback feedback) {
        super(id);
        this.feedback = feedback;
    }
}
