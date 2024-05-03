package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.Pair;

import java.util.List;

public interface View {

    void run();

    void serverCrashed();

    //TODO add signature for specific-to-view methods that are called from controller EventReceiver

    void displayAvailableLobbies(List<Pair<String, Pair<Integer, Integer>>> availableLobbies);

    void notifyCreatedLobby(Pair<String, Integer> createdLobby);

    void notifyDeleteAccount(Feedback feedback, String message);
}
