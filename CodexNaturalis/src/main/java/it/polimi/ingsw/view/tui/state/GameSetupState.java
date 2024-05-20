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
        view.printMessage("Game setup phase.\n");
        view.print("Waiting for the game to assign you a setup. . .");
        // FIXME when server starts game, I receive a non serializable error on PlayArea. Does the model send
        //       the PlayArea and not the ViewPlayArea?
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                // Choose setup
                break;
            case 2:
                // See your assigned setup
                break;
            case 3:
                // See common objective cards
                break;
            case 4:
                // See visible decks
                break;
            case 5:
                // Open chat
                break;
            case 6:
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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                view.print("\r\033[2K");
                setupMessage = message;
                showSetupChoices();
                break;
            case EventID.UPDATE_GAME_PLAYERS:
                showResponseMessage(message, 1000);
                break;
        }
    }

    private void showSetupChoices() {
        view.printMessage(setupMessage);
        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Choose setup"
                , "See your assigned setup"
                , "See common objective cards"
                , "See visible decks"
                , "Open chat"
                , "Quit game"));
        handleInput(choice);
    }

    @Override
    public boolean inGame() {
        return true;
    }
}
