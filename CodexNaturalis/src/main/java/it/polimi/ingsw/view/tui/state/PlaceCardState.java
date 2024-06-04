package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.PlaceCardEvent;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

public class PlaceCardState extends ViewState {
    public PlaceCardState(View view) {
        super(view);
    }

    @Override
    public void run() {
        // FIXME place card DEBUG
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        controller.newViewEvent(new PlaceCardEvent(controller.getModel().getSelfPlayer().getPlayArea().getHand().getFirst().getId(), new Coordinate(1,1), false));
    }

    @Override
    public boolean handleInput(int input) {
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case PLACE_CARD:
                view.printMessage(message);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean inGame() {
        return true;
    }
}
