package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.View;

import java.util.List;

public class GUI implements View {
    public void run(){}

    public void serverCrashed(){}

    @Override
    public void displayAvailableLobbies(List<Pair<String, Pair<Integer, Integer>>> availableLobbies) {}

    @Override
    public void notifyCreatedLobby(Pair<String, Integer> createdLobby){}

    @Override
    public void notifyDeleteAccount(Feedback feedback, String message){}
}
