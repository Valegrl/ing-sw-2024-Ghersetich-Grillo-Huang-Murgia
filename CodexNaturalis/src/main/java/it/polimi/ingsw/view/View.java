package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.controller.ViewController;

public interface View {

    void run();

    void setFXMLController(FXMLController controller);

    boolean inGame();

    boolean inLobby();

    boolean inMenu();

    boolean inChat();

    void printMessage(String message);

    void print(String message);

    String getInput();

    String getIntFromInput();

    ViewController getController();

    String getUsername();

    ViewState getState();

    void setState(ViewState state);

    void setUsername(String username);

    void stopInputRead(boolean stopInputRead);

    void handleResponse(String eventID, Feedback feedback, String message);

    void serverDisconnected();

    void clearInput();
}
