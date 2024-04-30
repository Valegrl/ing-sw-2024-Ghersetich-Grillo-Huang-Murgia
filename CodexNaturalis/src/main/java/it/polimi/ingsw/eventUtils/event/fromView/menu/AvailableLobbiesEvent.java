package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;

import java.util.ArrayList;
import java.util.List;

public class AvailableLobbiesEvent extends FeedbackEvent<List<String>> {

    private final static String id = "AVAILABLE_LOBBIES";

    private final List<String> lobbies;

    public AvailableLobbiesEvent() {
        super(id);
        lobbies = new ArrayList<>();
    }

    public AvailableLobbiesEvent(Feedback feedback, List<String> lb, String message) {
        super(id, feedback, message);
        lobbies = new ArrayList<>(lb);
    }

    @Override
    public List<String> getData() {
        return new ArrayList<>(lobbies);
    }
}
