package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

public class LobbyState extends ViewState {
    public LobbyState(View view) {
        super(view);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean handleInput(int input) {
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }
}
