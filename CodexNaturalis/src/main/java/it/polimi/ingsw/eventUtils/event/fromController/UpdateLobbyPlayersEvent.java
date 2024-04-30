package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.UIEventReceiver;

public class UpdateLobbyPlayersEvent extends Event {

    private final static String id = "UPDATE_LOBBY_PLAYERS";

    public UpdateLobbyPlayersEvent() {
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
