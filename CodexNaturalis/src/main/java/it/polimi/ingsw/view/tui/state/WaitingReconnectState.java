package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

public class WaitingReconnectState extends GameState {
    public WaitingReconnectState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        String message = """
                You are the only player remaining in the game.
                Waiting for at least one player to reconnect, or for the timer to run out . . .
                
                Type 'q' to leave the game.
                """;

        String input;
        do {
            view.printMessage(message);
            input = view.getInput();
        } while (!input.equals("q") && !input.equals("$stop"));
        if (input.equals("q"))
            handleInput(1);
    }

    @Override
    public boolean handleInput(int input) {
        if (input == 1) {
            quitGame();
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case UPDATE_GAME_PLAYERS:
                view.clearInput();
                view.printMessage(message + "\n");
                break;
            case NEW_GAME_STATUS:
                view.stopInputRead(true);
                view.clearInput();
                showResponseMessage(message, 1000);
                transition(controller.getPreviousViewState());
                break;
            case NEW_TURN:
                view.stopInputRead(true);
                view.clearInput();
                showResponseMessage(message, 1000);
                if (controller.hasTurn())
                    transition(new PlaceCardState(view));
                else
                    transition(new WaitForTurnState(view));
                break;
            case QUIT_GAME:
                handleQuitGame(feedback, message);
                break;
            case ENDED_GAME:
                handleGameEndedEvent(message);
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
