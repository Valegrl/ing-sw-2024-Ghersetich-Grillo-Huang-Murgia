package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;

public class CreateLobbyEvent extends FeedbackEvent<Pair<String, Integer>> {

    private final static String id = "CREATE_LOBBY";

    private final Pair<String, Integer> setting;

    public CreateLobbyEvent(String lobbyID, Integer nPlayers) {
        super(id);
        setting = new Pair<>(lobbyID, nPlayers);
    }

    public CreateLobbyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        setting = new Pair<>(null, null);
    }

    @Override
    public Pair<String, Integer> getData() {
        return setting;
    }

}
