package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.PlaceCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.AvailablePositionsEvent;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;

import java.util.Arrays;

public class PlaceCardState extends GameState {

    private String availablePositions;

    public PlaceCardState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.stopInputRead(false);
        setCurrentPlayAreaUsername(controller.getUsername());
        view.printMessage("It's your turn to " + boldText("place") + " a card!\n" +
                "Next turns: " + controller.playersListToString() + "\n");

        view.printMessage(controller.selfPlayAreaToString());

        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Place a card"
                , "See a specific placed card"
                , "See visible decks"
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
                placeCard();
                break;
            case 2:
                clearConsole();
                seeDetailedCard();
                break;
            case 3:
                showVisibleDecks();
                break;
            case 4:
                showObjectiveCards();
                break;
            case 5:
                showOtherPlayerPlayArea();
                break;
            case 6:
                transition(new ChatState(view, this));
                break;
            case 7:
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
            case PLACE_CARD:
                view.printMessage(message);
                placeCard();
                break;
            case AVAILABLE_POSITIONS:
                availablePositions = message;
                notifyResponse();
                break;
            case SELF_PLACE_CARD:
                transition(new DrawCardState(view));
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

    private void placeCard(){
        view.printMessage("Choose the card you want to place from your hand: ");
        int chosenCardIndex = readChoiceFromInput(controller.getSelfHandCardIds());
        if (chosenCardIndex == -1) {
            run();
            return;
        }

        ImmPlayableCard chosenCard = controller.getSelfHandCard(chosenCardIndex - 1);
        view.printMessage("Choose the showing face of the card you want to place: ");
        int chosenFace = readChoiceFromInput(Arrays.asList("Front", "Back"));
        if (chosenFace == -1) {
            run();
            return;
        }

        view.printMessage("Specify the coordinate to place card " + Item.itemToColor(chosenCard.getPermanentResource(), chosenCard.getId()) + ":\n");

        controller.newViewEvent(new AvailablePositionsEvent());
        waitForResponse();

        view.printMessage(availablePositions);

        Coordinate coordinate = chooseCoordinate();
        if (coordinate != null) {
            controller.newViewEvent(new PlaceCardEvent(chosenCard.getId(), coordinate, (chosenFace == 2)));
        } else {
            placeCard();
        }
    }

    private Coordinate chooseCoordinate() {
        Coordinate c = null;
        String x, y;
        boolean okPos = true;
        while (c == null || !okPos) {
            if (!okPos) view.printMessage("Coordinate not available. Please choose a different coordinate");
            okPos = true;
            c = null;
            view.print("  X: ");
            x = view.getIntFromInput();
            if (x.startsWith("$")) {
                if (x.equals("$exit")) {
                    break;
                }
                continue;
            }
            view.print("  Y: ");
            y = view.getIntFromInput();
            if (y.startsWith("$")) {
                if (y.equals("$exit")) {
                    break;
                }
                continue;
            }
            try {
                int i = Integer.parseInt(x);
                int j = Integer.parseInt(y);
                c = new Coordinate(i, j);
                okPos = controller.isAvailablePos(c);
            } catch (NumberFormatException e) {
                view.printMessage("Coordinate elements must be integers");
                c = null;
            }
        }
        return c;
    }

    @Override
    public boolean inGame() {
        return true;
    }
}
