package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a user attempts to log in.
 */
public class LoginEvent extends FeedbackEvent {

    /**
     * The unique identifier for a LoginEvent.
     */
    private final static String id = EventID.LOGIN.getID();

    /**
     * The account of the user attempting to log in.
     */
    private final Account account;


    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and initializes the account
     * with the provided username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public LoginEvent(String username, String password) {
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
    public LoginEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        account = new Account();
    }

    /**
     * @return The account of the user attempting to log in.
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
