package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is used to set up the cards for the game.
 */
public class ChooseCardsSetupEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CHOOSE_CARDS_SETUP.getID();

    /**
     * A string representing the message associated with this event.
     */
    private final String message;

    /**
     * A ViewStartSetup object representing the setup of the game.
     */
    private final ViewStartSetup viewSetup;

    /**
     * Constructs a new ChooseCardsSetupEvent with the given ViewStartSetup object and message.
     *
     * @param view the ViewStartSetup object representing the setup of the game
     * @param m the message associated with this event
     */
    public ChooseCardsSetupEvent(ViewStartSetup view, String m) {
        super(id);
        this.message = m;
        this.viewSetup = view;
    }

    /**
     * @return the message associated with this event
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the ViewStartSetup object associated with this event
     */
    public ViewStartSetup getViewSetup() {
        return viewSetup;
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
