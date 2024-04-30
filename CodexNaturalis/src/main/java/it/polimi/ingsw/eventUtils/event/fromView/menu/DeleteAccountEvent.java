package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;

public class DeleteAccountEvent extends FeedbackEvent<Pair<String, String>> {

    private final static String id = "DELETE_ACCOUNT";

    public DeleteAccountEvent() {
        super(id);
    }

    public DeleteAccountEvent(Feedback feedback, String message) {
        super(id, feedback, message);
    }
}
