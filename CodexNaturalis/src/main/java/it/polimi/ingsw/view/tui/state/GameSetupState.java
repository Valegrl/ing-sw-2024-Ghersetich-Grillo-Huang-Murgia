package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

/**
 * Class that represents the state of the game setup phase.
 */
public class GameSetupState extends ViewState {
    /**
     * Boolean that represents if it is the first time the state is shown.
     */
    private boolean notFirstStart = false;

    /**
     * String that represents the message of the setup.
     */
    private String setupMessage;

    /**
     * String that represents the hand message.
     */
    private String handMessage;

    /**
     * String that represents the common objectives message.
     */
    private String commObjectivesMessage;

    /**
     * String that represents the decks message.
     */
    private String decksMessage;

    /**
     * String that represents the opponents' hands message.
     */
    private String opponentsHandsMessage;

    /**
     * Class constructor.
     * @param view The TUI instance that this state belongs to.
     */
    public GameSetupState(View view) {
        super(view);
    }

    @Override
    public void run() {
        if (notFirstStart) {
            showSetupChoices();
        } else {
            controller.setInSetup(new Pair<>(true, false));
            notFirstStart = true;
            view.stopInputRead(true);
            view.clearInput();
            clearConsole();
            view.printMessage("Game setup phase.\n");
            view.print("Waiting for the game to assign you a setup. . .");
        }
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                // Choose setup
                chooseSetup();
                break;
            case 2:
                // See your assigned setup
                clearConsole();
                showResponseMessage(handMessage, 500);
                if(waitInputToGoBack())
                    showSetupChoices();
                break;
            case 3:
                // See common objectives
                clearConsole();
                showResponseMessage(commObjectivesMessage, 500);
                if(waitInputToGoBack())
                    showSetupChoices();
                break;
            case 4:
                // See visible decks
                clearConsole();
                showResponseMessage(decksMessage, 500);
                if(waitInputToGoBack())
                    showSetupChoices();
                break;
            case 5:
                // See opponents' hands
                clearConsole();
                showResponseMessage(opponentsHandsMessage, 500);
                if(waitInputToGoBack())
                    showSetupChoices();
                break;
            case 6:
                notFirstStart = true;
                transition(new ChatState(view, this));
                break;
            case 7:
                // Quit game
                quitGame();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case CHOOSE_CARDS_SETUP:
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                clearLine();
                String[] m = message.split("%");
                setupMessage = m[0];
                handMessage = m[1];
                commObjectivesMessage = m[2];
                decksMessage = m[3];
                opponentsHandsMessage = m[4];
                showSetupChoices();
                break;
            case UPDATE_GAME_PLAYERS:
                view.printMessage(message);
                view.clearInput();
                break;
            case CHOSEN_CARDS_SETUP:
                notifyResponse();
                clearConsole();
                view.printMessage(message);
                view.printMessage("Wait for other players to chose their setup. . .\n");
                break;
            case CHOOSE_TOKEN_SETUP:
                clearConsole();
                transition(new TokenSetupState(view));
                break;
            case QUIT_GAME:
                if (feedback == Feedback.SUCCESS) {
                    notifyResponse();
                    showResponseMessage(message, 1500);
                    transition(new MenuState(view));
                } else {
                    showResponseMessage("Failed to quit game: " + message, 2000);
                    showSetupChoices();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Method that shows the choices available during the setup phase.
     */
    private void showSetupChoices() {
        clearConsole();
        view.printMessage(setupMessage);
        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Choose setup"
                , "See your assigned hand"
                , "See common objective cards"
                , "See visible decks"
                , "See opponents' hands"
                , "Open chat"
                , "Quit game"));
        handleInput(choice);
    }

    /**
     * Method that allows the player to choose the setup.
     */
    private void chooseSetup() {
        view.printMessage("Choose the showing face of the start card: ");
        int chosenFace = readChoiceFromInput(Arrays.asList("Front", "Back"));
        if (chosenFace == -1) {
            run();
            return;
        }
        if (chosenFace == 0) return;
        view.printMessage("Choose the secret objective card: ");
        int chosenObjective = readChoiceFromInput(Arrays.asList("Card 1", "Card 2"));
        if (chosenObjective == -1) {
            run();
            return;
        }
        if (chosenObjective == 0) return;
        controller.chosenSetup(chosenObjective, (chosenFace == 2)); // 1 is not flipped (false), 2 is flipped (true)

        waitForResponse();
    }

    @Override
    public boolean inGame() {
        return true;
    }
}
