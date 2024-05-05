package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.utils.Pair;

import java.util.List;

public interface View {

    void run();

    void serverCrashed();

    //TODO add signature for specific-to-view methods that are called from controller EventReceiver

    void printMessage(String message);

    void displayAvailableLobbies(Feedback feedback, String message, List<LobbyState> availableLobbies);

    void notifyCreatedLobby(Feedback feedback, String message, String id, int requiredPlayers);

    void notifyDeleteAccount(Feedback feedback, String message);

    void displayOfflineGames(Feedback feedback, String message, List<LobbyState> offlineGames);

    void displayJoinedLobby(Feedback feedback, String message, String id, List<Pair<String, Boolean>> playersReadyStatus );

    void notifyLogin(Feedback feedback, String message, Account account);

    void notifyLogout(Feedback feedback, String message);

    void notifyReconnectToGame(Feedback feedback, String message);

    void notifyRegisterAccount(Feedback feedback, String message, Account account);

    void notifyKickFromLobby(Feedback feedback, String message, String kickedPlayer);

}
