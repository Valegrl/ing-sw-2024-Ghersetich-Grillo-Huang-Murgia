package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

public class ProfileSettingsState extends ViewState {

    public ProfileSettingsState(View view) {
        super(view);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean handleInput(int input) {
        return false;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }

    @Override
    public boolean inMenu() {
        return true;
    }
}
