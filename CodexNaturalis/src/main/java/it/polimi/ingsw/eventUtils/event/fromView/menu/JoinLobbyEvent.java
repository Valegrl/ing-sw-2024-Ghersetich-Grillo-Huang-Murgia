package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;

import java.util.List;

public class JoinLobbyEvent extends FeedbackEvent<Pair<String, List<String>>> {

    private final static String id = "JOIN_LOBBY";

    private final Pair<String, List<String>> info;

    public JoinLobbyEvent(String lobbyID) {
        super(id);
        info = new Pair<>(lobbyID, null);
    }

    public JoinLobbyEvent(Feedback feedback, List<String> usernames, String message) {
        super(id, feedback, message);
        info = new Pair<>(null, usernames);
    }

    @Override
    public Pair<String, List<String>> getData() {
        return info;
    }
}
