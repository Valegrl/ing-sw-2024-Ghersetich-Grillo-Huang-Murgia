package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

import java.util.ArrayList;
import java.util.List;

public class GetMyOfflineGamesEvent extends FeedbackEvent<List<String>> {

    private final static String id = "GET_MY_OFFLINE_GAMES";

    private final List<String> games;

    public GetMyOfflineGamesEvent() {
        super(id);
        games = new ArrayList<>();
    }

    public GetMyOfflineGamesEvent(Feedback feedback, List<String> games, String message) {
        super(id, feedback, message);
        this.games = new ArrayList<>(games);
    }

    @Override
    public List<String> getData() {
        return games;
    }
}
