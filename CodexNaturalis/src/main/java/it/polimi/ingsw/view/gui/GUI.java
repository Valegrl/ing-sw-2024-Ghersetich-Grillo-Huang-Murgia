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

    public void serverCrashed(){}

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
    public void setUsername(String username) {

    }

    @Override
    public void handleResponse(String eventID, Feedback feedback, String message) {

    }

    @Override
    public void displayAvailableLobbies(Feedback feedback, String message, List<LobbyState> availableLobbies) {}

    @Override
    public void notifyCreatedLobby(Feedback feedback, String message, String id, int requiredPlayers){}

    @Override
    public void notifyDeleteAccount(Feedback feedback, String message){}

    @Override
    public void displayOfflineGames(Feedback feedback, String message, List<LobbyState> offlineGames){}

    @Override
    public void displayJoinedLobby(Feedback feedback, String message, String id, List<Pair<String, Boolean>> playersReadyStatus){}

    @Override
    public void notifyLogout(Feedback feedback, String message){}

    @Override
    public void notifyReconnectToGame(Feedback feedback, String message){}

    @Override
    public void notifyKickFromLobby(Feedback feedback, String message, String kickedPlayer){}

}
