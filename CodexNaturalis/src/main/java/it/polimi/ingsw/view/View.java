package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.Event;

public interface View {

    void run();

    void serverCrashed();

    // TODO add signature for specific-to-view methods that are called from controller EventReceiver
}
