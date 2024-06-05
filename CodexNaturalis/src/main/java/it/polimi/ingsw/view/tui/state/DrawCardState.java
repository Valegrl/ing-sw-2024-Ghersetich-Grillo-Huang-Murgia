package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.DrawCardEvent;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.view.View;

public class DrawCardState extends GameState {
    public DrawCardState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        setCurrentPlayAreaUsername(controller.getUsername());
        view.printMessage("Draw a card!");
        // FIXME place card DEBUG
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        controller.newViewEvent(new DrawCardEvent(CardType.GOLD, 1));
    }

    @Override
    public boolean handleInput(int input) {
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case DRAW_CARD:
                view.printMessage(message);
                break;
            case UPDATE_GAME_PLAYERS:
                view.printMessage(message);
                break;
            case NEW_TURN:
                clearConsole();
                view.stopInputRead(true);
                showResponseMessage(message, 1000);
                view.clearInput();
                view.stopInputRead(false);
                transition(new WaitForTurnState(view));
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
