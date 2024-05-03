package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.eventUtils.event.Event;

/**
 * Abstract class representing an event with feedback.
 * This class extends the Event class and provides additional fields for feedback and message.
 */
public abstract class FeedbackEvent extends Event {
    /**
     * The feedback associated with the event.
     */
    private Feedback feedback = null;

    /**
     * The message associated with the event.
     */
    private String message = null;

    /**
     * Constructor for View (client). Initializes the event with the specified id.
     *
     * @param id The id of the event.
     */
    public FeedbackEvent(String id) {
        super(id);
    }

    /**
     * Constructor for Controller (server). Initializes the event with the specified id, feedback, and message.
     *
     * @param id The id of the event.
     * @param feedback The feedback associated with the event.
     * @param message The message associated with the event.
     */
    public FeedbackEvent(String id, Feedback feedback, String message) {
        super(id);
        this.feedback = feedback;
        this.message = message;
    }

    /**
     * @return The feedback associated with the event.
     */
    public Feedback getFeedback() { return feedback; }

    /**
     * @return The message associated with the event.
     */
    public String getMessage() {
        return message;
    }
}
