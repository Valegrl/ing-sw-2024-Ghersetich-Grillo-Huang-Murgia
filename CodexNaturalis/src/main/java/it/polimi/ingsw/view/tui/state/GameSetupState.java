package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

public class GameSetupState extends ViewState {

    private String setupMessage;

    public GameSetupState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.printMessage("Game setup phase.");

        // TODO show decks and common objCards

        view.printMessage("Choose an option:");

//        int choice = readChoiceFromInput(Arrays.asList(
//                "Choose setup"
//                , "Open chat"
//                , "Quit game"));
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                // Choose setup
                break;
            case 2:
                // Open chat
                break;
            case 3:
                // Quit game
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.CHOOSE_CARDS_SETUP:
                setupMessage = message;
                view.printMessage(message);
                chooseSetup();
                break;
            case EventID.UPDATE_GAME_PLAYERS:
                showResponseMessage(message, 1000);
                break;
        }
    }

    private void chooseSetup() {
        // TODO
    }

    @Override
    public boolean inGame() {
        return true;
    }
}
