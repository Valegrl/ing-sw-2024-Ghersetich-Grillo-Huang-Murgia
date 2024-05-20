package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

public class GameSetupState extends ViewState {

    private String setupMessage;

    private String handMessage;

    public GameSetupState(View view) {
        super(view);
    }

    @Override
    public void run() {
        view.stopInputRead(true);
        view.clearInput();
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
                clearConsole();
                showResponseMessage(handMessage, 1000);
                view.printMessage("Press and enter any key to go back: ");
                view.getInput();
                showSetupChoices();
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
                view.stopInputRead(false);
                view.print("\r\033[2K");
                String[] m = message.split("%");
                setupMessage = m[0];
                handMessage = m[1];
                showSetupChoices();
                break;
            case EventID.UPDATE_GAME_PLAYERS:
                showResponseMessage(message, 1000);
                break;
        }
    }

    private void showSetupChoices() {
        clearConsole();
        view.printMessage(setupMessage);
        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Choose setup"
                , "See your assigned hand"
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
