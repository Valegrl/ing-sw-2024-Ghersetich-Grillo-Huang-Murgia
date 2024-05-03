package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a user attempts to register.
 */
public class RegisterEvent extends FeedbackEvent {

    /**
     * The unique identifier for a RegisterEvent.
     */
    private final static String id = EventID.REGISTER.getID();

    /**
     * The account of the user attempting to register.
     */
    private final Account account;

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and initializes the account with the provided username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public RegisterEvent(String username, String password) {
        super(id);
        account = new Account(username, password);
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     * It also initializes the account with an empty account.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public RegisterEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        account = new Account();
    }

    /**
     * @return The account of the user attempting to register.
     */
    public Account getAccount() {
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
