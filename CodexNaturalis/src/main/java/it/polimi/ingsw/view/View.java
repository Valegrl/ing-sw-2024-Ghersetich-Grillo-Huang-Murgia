package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.controller.ViewController;

import java.util.List;

public interface View {

    void run();

    void setState(ViewState state);

    //TODO add signature for specific-to-view methods that are called from controller EventReceiver

    void printMessage(String message);

    String getInput();

    ViewController getController();

    String getUsername();

    ViewState getState();

    void setUsername(String username);

    void handleResponse(String eventID, Feedback feedback, String message);

    void serverCrashed();
}
