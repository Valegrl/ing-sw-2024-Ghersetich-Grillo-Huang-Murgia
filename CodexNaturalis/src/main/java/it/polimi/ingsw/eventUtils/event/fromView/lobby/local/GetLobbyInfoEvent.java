package it.polimi.ingsw.eventUtils.event.fromView.lobby.local;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

public class GetLobbyInfoEvent extends FeedbackEvent {

    private final static String id = EventID.GET_MY_OFFLINE_GAMES.getID();

    public GetLobbyInfoEvent() {
        super(id);
    }

    public GetLobbyInfoEvent(Feedback feedback, String message) {
        super(id, feedback, message);
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {
        viewEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
