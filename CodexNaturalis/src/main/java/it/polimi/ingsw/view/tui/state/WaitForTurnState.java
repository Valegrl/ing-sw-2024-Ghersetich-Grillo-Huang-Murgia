package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;
import java.util.List;

public class WaitForTurnState extends ViewState {
    public WaitForTurnState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.printMessage("Player turns: " + controller.playersListToString());
        view.printMessage("Waiting for your turn. . .  \n");

        view.printMessage(controller.selfPlayAreaToString());

        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Open chat"
                , "See visible decks"
                , "See objective cards"
                , "See another player's play area"
                , "Quit game"));
        handleInput(choice);
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                transition(new ChatState(view, this));
                break;
            case 2:
                showVisibleDecks();
                break;
            case 3:
                showObjectiveCards();
                break;
            case 4:
                showOtherPlayerPlayArea();
                break;
            case 5:
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
            case OTHER_PLACE_CARD, OTHER_DRAW_CARD:
                showInfoMessage(message);
                break;
            case UPDATE_GAME_PLAYERS:

                break;
            case QUIT_GAME:
                if (feedback == Feedback.SUCCESS) {

                } else {
                    view.printMessage(message);
                    run();
                }
                break;
            default:
                break;
        }
    }

    private void showVisibleDecks() {
        clearConsole();
        view.printMessage(controller.decksToString());
        waitInputToGoBack();
        run();
    }

    private void showOtherPlayerPlayArea() {
        clearConsole();
        List<String> players = controller.getModel().getPlayerUsernames();
        players.remove(controller.getUsername());
        view.printMessage("Choose a player:");
        int choice = readChoiceFromInput(players);
        clearConsole();
        view.printMessage(controller.opponentPlayAreaToString(players.get(choice - 1)));
        waitInputToGoBack();
        run();
    }

    private void showObjectiveCards() {
        clearConsole();
        view.printMessage(controller.objectiveCardsToString());
        waitInputToGoBack();
        run();
    }

    private void showInfoMessage(String message) {
        clearConsole();
        view.stopInputRead(true);
        showResponseMessage(message, 1000);
        view.clearInput();
        view.stopInputRead(false);
        run();
    }

    @Override
    public boolean inGame() {
        return true;
    }
}
