package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.controller.ViewController;

import java.util.List;

public class GUI implements View {
    public void run(){}

    public void serverDisconnected(){}

    @Override
    public void clearInput() {

    }

    public void setState(ViewState state){}

    @Override
    public void printMessage(String message){}

    @Override
    public String getInput() {
        return "";
    }

    @Override
    public ViewController getController() {
        return null;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public ViewState getState() {
        return null;
    }

    @Override
    public void setUsername(String username) {

    }

    @Override
    public void handleResponse(String eventID, Feedback feedback, String message) {

    }

    @Override
    public void stopInputRead(boolean stopInputRead) {

    }

}
