package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

/**
 * Represents the state of the view when the user is reconnecting to a game.
 */
public class ReconnectToGameState extends ViewState {
    /**
     * Constructor for the ReconnectToGameState.
     * @param view The TUI instance that this state belongs to.
     */
    public ReconnectToGameState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        String message = "Reconnecting to the game . . .\n";
        if (controller.isInTokenSetup()) {
            message += "Your setup will be chosen randomly. Wait for the other players to choose their setup.";
        } else {
            message += "You will be able to choose your colored token once the other players have chosen their card setup.";
        }
        showResponseMessage(message, 1000);

        if (!controller.isInSetup()) {
            if (controller.getGameStatus().equals(GameStatus.RUNNING)) {
                if (controller.hasTurn()) {
                    transition(new PlaceCardState(view));
                    return;
                } else {
                    transition(new WaitForTurnState(view));
                    return;
                }
            } else if (controller.getGameStatus().equals(GameStatus.WAITING)) {
                if (controller.hasTurn())
                    controller.setPreviousGameStatus(new Pair<>(GameStatus.RUNNING, new PlaceCardState(view)));
                else
                    controller.setPreviousGameStatus(new Pair<>(GameStatus.RUNNING, new WaitForTurnState(view)));
                transition(new WaitingReconnectState(view));
                return;
            } else {
                throw new IllegalStateException("Unexpected game status.");
            }
        }

        view.printMessage("Press and enter any key to leave the game: ");
        String in = view.getInput();
        if (!in.equals("$stop"))
            quitGame();
    }

    @Override
    public boolean handleInput(int input) {
        return false;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case UPDATE_GAME_PLAYERS:
                view.clearInput();
                view.printMessage(message);
                break;
            case CHOOSE_TOKEN_SETUP:
                view.clearInput();
                if (controller.isInTokenSetup()) {
                    view.printMessage(message);
                } else {
                    view.stopInputRead(true);
                    clearConsole();
                    transition(new TokenSetupState(view));
                }
                break;
            case QUIT_GAME:
                if (feedback == Feedback.SUCCESS) {
                    notifyResponse();
                    showResponseMessage(message, 1500);
                    transition(new MenuState(view));
                } else {
                    showResponseMessage("Failed to quit game: " + message, 2000);
                }
                break;
            case UPDATE_LOCAL_MODEL:
                view.clearInput();
                view.stopInputRead(true);
                clearConsole();
                showResponseMessage(message, 1000);

                if (controller.getGameStatus().equals(GameStatus.RUNNING)) {
                    if (controller.hasTurn()) {
                        transition(new PlaceCardState(view));
                    } else {
                        transition(new WaitForTurnState(view));
                    }
                } else if (controller.getGameStatus().equals(GameStatus.WAITING)) {
                    transition(new WaitingReconnectState(view));
                } else {
                    throw new IllegalStateException("Unexpected game status.");
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
