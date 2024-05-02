package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * Represents an event that initiates the registration process.
 * This event is a type of FeedbackEvent, which carries a pair of username and password as data.
 */
public class RegisterEvent extends FeedbackEvent<Pair<String, String>> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = EventID.REGISTER.getID();

    /**
     * A pair of username and password.
     */
    private final Pair<String, String> account;

    /**
     * Constructor for View (client). Initializes the account with the provided username and password.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     */
    public RegisterEvent(String username, String password) {
        super(id);
        account = new Pair<>(username, password);
    }

    /**
     * Constructor for Controller (server). Initializes the account with null values.
     *
     * @param feedback The feedback associated with the event.
     * @param message The message associated with the event.
     */
    public RegisterEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        account = new Pair<>(null, null);
    }

    @Override
    public Pair<String, String> getData() {
        return account;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {
        viewEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
