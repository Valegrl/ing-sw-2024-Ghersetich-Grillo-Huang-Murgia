package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;

import java.util.Arrays;

public class WaitForTurnState extends GameState {
    public WaitForTurnState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.stopInputRead(false);
        setCurrentPlayAreaUsername(controller.getUsername());
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
            case OTHER_PLACE_CARD, UPDATE_GAME_PLAYERS:
                showInfoMessage(message);
                break;
            case OTHER_DRAW_CARD:
                clearConsole();
                view.stopInputRead(true);
                view.printMessage(message);
                view.clearInput();
                view.stopInputRead(false);
                break;
            case NEW_TURN:
                clearConsole();
                view.stopInputRead(true);
                showResponseMessage(message, 1000);
                view.clearInput();
                if (controller.hasTurn())
                    transition(new PlaceCardState(view));
                else
                    run();
                break;
            case QUIT_GAME:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 1500);
                    transition(new MenuState(view));
                } else {
                    view.printMessage(message);
                    run();
                }
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
