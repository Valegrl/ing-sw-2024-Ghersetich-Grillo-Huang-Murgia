package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.utils.Pair;

import java.util.List;

public interface View {

    void run();

    void serverCrashed();

    //TODO add signature for specific-to-view methods that are called from controller EventReceiver

    void displayAvailableLobbies(List<LobbyState> availableLobbies);

    void notifyCreatedLobby(String id, int requiredPlayers);

    void notifyDeleteAccount(Feedback feedback, String message);

    void displayOfflineGames(List<LobbyState> offlineGames);

    void displayJoinedLobby(String id, List<Pair<String, Boolean>> playersReadyStatus );
}
