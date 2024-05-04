package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an event that is used to set up the tokens for the game.
 */
public class ChooseTokenSetupEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CHOOSE_TOKEN_SETUP.getID();

    /**
     * A list of Token objects representing the available colors for the game.
     */
    private final List<Token> availableColors;

    /**
     * Constructs a new ChooseTokenSetupEvent with the given list of available colors.
     *
     * @param colors the list of Token objects representing the available colors for the game
     */
    public ChooseTokenSetupEvent(List<Token> colors) {
        super(id);
        this.availableColors = new ArrayList<>(colors);
    }

    /**
     * @return the list of Token objects representing the available colors for the game
     */
    public List<Token> getAvailableColors() {
        return availableColors;
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
