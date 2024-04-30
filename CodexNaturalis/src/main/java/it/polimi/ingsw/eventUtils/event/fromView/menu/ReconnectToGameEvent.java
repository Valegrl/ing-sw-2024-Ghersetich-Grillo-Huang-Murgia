package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;

import java.util.List;

public class ReconnectToGameEvent extends FeedbackEvent<String> {

    private final static String id = "RECONNECT_TO_GAME";

    private final String gameID;

    public ReconnectToGameEvent(String gameID) {
        super(id);
        this.gameID = gameID;
    }

    public ReconnectToGameEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.gameID = null;
    }

    @Override
    public String getData() {
        return gameID;
    }
}
