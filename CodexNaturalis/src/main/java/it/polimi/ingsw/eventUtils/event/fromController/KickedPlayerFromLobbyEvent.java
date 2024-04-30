package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.UIEventReceiver;

public class KickedPlayerFromLobbyEvent extends Event {

    private final static String id = "KICKED_PLAYER_FROM_LOBBY";

    public KickedPlayerFromLobbyEvent() {
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
