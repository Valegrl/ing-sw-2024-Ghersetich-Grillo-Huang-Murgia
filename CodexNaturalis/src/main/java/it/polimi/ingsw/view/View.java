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

    void displayAvailableLobbies(Feedback feedback, String message, List<LobbyState> availableLobbies);

    void notifyDeleteAccount(Feedback feedback, String message);

    void displayOfflineGames(Feedback feedback, String message, List<LobbyState> offlineGames);

    void displayJoinedLobby(Feedback feedback, String message, String id, List<Pair<String, Boolean>> playersReadyStatus );

    void notifyLogout(Feedback feedback, String message);

    void notifyReconnectToGame(Feedback feedback, String message);

    void notifyKickFromLobby(Feedback feedback, String message, String kickedPlayer);

    void serverCrashed();
}
