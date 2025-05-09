package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.DrawCardEvent;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * Class that represents the state of the game where the player has to draw a card.
 */
public class DrawCardState extends GameState {
    /**
     * Constructor for the DrawCardState.
     * @param view The TUI instance that this state belongs to.
     */
    public DrawCardState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        setCurrentPlayAreaUsername(controller.getUsername());

        view.printMessage("It's your turn to " + boldText("draw") + " a card!\n" +
                "Next turns: " + controller.playersListToString() + "\n");

        view.printMessage(controller.selfPlayAreaToString());
        view.printMessage(controller.decksToString());

        view.printMessage("It's your turn\n\nChoose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Draw a card"
                , "See a specific placed card"
                , "See objective cards"
                , "See another player's play area"
                , "Open chat"
                , "Quit game"));
        handleInput(choice);
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                drawCard();
                break;
            case 2:
                clearConsole();
                seeDetailedCard();
                break;
            case 3:
                showObjectiveCards();
                break;
            case 4:
                showOtherPlayerPlayArea();
                break;
            case 5:
                transition(new ChatState(view, this));
                break;
            case 6:
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
            case DRAW_CARD:
                showResponseMessage(message, 500);
                drawCard();
                break;
            case SELF_DRAW_CARD:
                clearConsole();
                view.printMessage(message);
                break;
            case UPDATE_GAME_PLAYERS:
                view.clearInput();
                view.printMessage(message);
                break;
            case SELF_TURN_TIMER_EXPIRED:
                clearConsole();
                view.stopInputRead(true);
                showResponseMessage("Your turn-timer has expired. A card will be drawn randomly.", 1000);
                view.clearInput();
                break;
            case NEW_TURN:
                clearConsole();
                view.stopInputRead(true);
                showResponseMessage(message, 1000);
                view.clearInput();
                transition(new WaitForTurnState(view));
                break;
            case NEW_GAME_STATUS:
                handleNewGameStatus(message);
                break;
            case ENDED_GAME:
                handleGameEndedEvent(message);
                break;
            case QUIT_GAME:
                handleQuitGame(feedback, message);
                break;
            default:
                break;
        }
    }

    /**
     * Prompts the user to choose a card to be drawn.
     * The user can choose between the gold deck and the resource deck.
     * The user can choose to draw from the top of the deck or from a visible card.
     */
    private void drawCard() {
        view.printMessage("Choose the " + boldText("deck") + " you want to draw a card from: ");
        int chosenDeckIndex = readChoiceFromInput(Arrays.asList("GOLD", "RESOURCE"));
        CardType chosenDeck;
        if (chosenDeckIndex == -1) {
            run();
            return;
        } else if (chosenDeckIndex == 1) chosenDeck = CardType.GOLD;
        else if (chosenDeckIndex == 0) return;
        else chosenDeck = CardType.RESOURCE;

        view.printMessage("Choose the card you want to draw: ");
        List<String> ids = controller.getDeckVisibleIds(chosenDeck);
        ids.addFirst("Top of deck");
        int chosenCard = readChoiceFromInput(ids);
        if (chosenCard == -1) {
            run();
            return;
        } else if (chosenCard == 1) {
            chosenCard = 2;
        } else if (chosenCard == 0) {
            return;
        } else {
            ids.removeFirst(); // removing top of deck
            chosenCard = controller.getVisibleDeckCardIndex(chosenDeck, ids.get(chosenCard - 2));
        }
        if (chosenCard == -1) {
            view.printMessage("Visible card could not be found. Please try again");
            drawCard();
            return;
        }
        controller.newViewEvent(new DrawCardEvent(chosenDeck, chosenCard));
    }

    @Override
    public boolean inGame() {
        return true;
    }

    @Override
    public String toString() {
        return "DrawCardState";
    }
}
