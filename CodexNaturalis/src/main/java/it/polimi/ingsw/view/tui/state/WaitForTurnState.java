package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.utils.AnsiCodes;
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
        view.printMessage(controller.opponentPlayAreaToString(choice - 1));
        waitInputToGoBack();
        run();
    }

    private void showObjectiveCards() {
        clearConsole();
        // view.printMessage(controller.selfPlayer.objectiveCardsToString());
        waitInputToGoBack();
        run();
    }

    @Override
    public boolean inGame() {
        return true;
    }
}
