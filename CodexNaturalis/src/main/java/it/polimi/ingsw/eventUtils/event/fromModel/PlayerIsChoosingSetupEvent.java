package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.UIEventReceiver;

public class PlayerIsChoosingSetupEvent extends Event {

    private final static String id = "PLAYER_IS_CHOOSING_SETUP";

    public PlayerIsChoosingSetupEvent() {
        super(id);
    }

    @Override
    public void receiveEvent(UIEventReceiver uiEventReceiver) {
        uiEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
