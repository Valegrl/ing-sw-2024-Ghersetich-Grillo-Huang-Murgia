package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.View;

import java.util.List;

public class GUI implements View {
    public void run(){}

    public void serverCrashed(){}

    @Override
    public void displayAvailableLobbies(Feedback feedback, String message, List<LobbyState> availableLobbies) {}

    @Override
    public void notifyCreatedLobby(Feedback feedback, String message, String id, int requiredPlayers){}

    @Override
    public void notifyDeleteAccount(Feedback feedback, String message){}

    @Override
    public void displayOfflineGames(Feedback feedback, String message, List<LobbyState> offlineGames){}

    @Override
    public void displayJoinedLobby(String id, List<Pair<String, Boolean>> playersReadyStatus ){}
}
