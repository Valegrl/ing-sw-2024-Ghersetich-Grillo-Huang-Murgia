package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;

public class RegisterEvent extends FeedbackEvent<Pair<String, String>> {

    private final static String id = "REGISTER";

    private final Pair<String, String> account;

    public RegisterEvent(String username, String password) {
        super(id);
        account = new Pair<>(username, password);
    }

    public RegisterEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        account = new Pair<>(null, null);
    }

    @Override
    public Pair<String, String> getData() {
        return account;
    }
}
