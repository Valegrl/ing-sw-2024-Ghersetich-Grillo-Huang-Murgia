package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.eventUtils.event.Event;

public abstract class FeedbackEvent<T> extends Event/*<T>*/ {
    private Feedback feedback = null;

    private String message = null;

    public FeedbackEvent(String id) {
        super(id);
    }

    public FeedbackEvent(String id, Feedback feedback, String message) {
        super(id);
        this.feedback = feedback;
        this.message = message;
    }

    public Feedback getFeedback() { return feedback; }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return null;
    }
}
