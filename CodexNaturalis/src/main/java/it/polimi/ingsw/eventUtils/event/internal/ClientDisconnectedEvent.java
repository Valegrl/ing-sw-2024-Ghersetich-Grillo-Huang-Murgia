package it.polimi.ingsw.eventUtils.event.internal;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.UIEventReceiver;

public class ClientDisconnectedEvent extends Event {

    private final static String id = "CLIENT_DISCONNECTED";

    public ClientDisconnectedEvent() {
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
