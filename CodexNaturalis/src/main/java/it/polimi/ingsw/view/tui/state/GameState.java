package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.utils.AnsiCodes;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.viewModel.ViewModel;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayer;
import it.polimi.ingsw.viewModel.viewPlayer.ViewPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class that represents the state of the game.
 */
public abstract class GameState extends ViewState {

    /**
     * The username of the player whose play area is currently being shown.
     */
    private String currentPlayAreaUsername = view.getUsername();

    /**
     * Class constructor.
     * @param view The TUI instance that this state belongs to.
     */
    public GameState(View view) {
        super(view);
    }

    /**
     * Shows the visible decks and waits for the user's input to go back.
     */
    protected void showVisibleDecks() {
        clearConsole();
        view.printMessage(controller.decksToString());
        if(waitInputToGoBack())
            run();
    }

    /**
     * Prompts the user to choose a player to see its {@link it.polimi.ingsw.viewModel.viewPlayer.ViewPlayArea ViewPlayArea}
     * and waits for the user's input to go back.
     */
    protected void showOtherPlayerPlayArea() {
        clearConsole();
        List<String> players = new ArrayList<>(controller.getInMatchPlayerUsernames());
        players.remove(controller.getUsername());
        List<String> coloredPlayers = players.stream()
                .map(username -> controller.getPlayerToken(username).getColorCode() + username + AnsiCodes.RESET)
                        .toList();
        view.printMessage("Choose a player: ");
        int choice = readChoiceFromInput(coloredPlayers);

        if (choice == -1) {
            run();
            return;
        }
        if (choice == 0) return;

        clearConsole();
        currentPlayAreaUsername = players.get(choice - 1);
        view.printMessage(controller.opponentPlayAreaToString(currentPlayAreaUsername));

        seeDetailedCard();
    }

    /**
     * Shows the objective cards and waits for the user's input to go back.
     */
    protected void showObjectiveCards() {
        clearConsole();
        view.printMessage(controller.objectiveCardsToString());
        if(waitInputToGoBack())
            run();
    }

    /**
     * Method to show an info message during the game.
     * @param message The message to show.
     */
    protected void showInfoMessage(String message) {
        clearConsole();
        view.stopInputRead(true);
        showResponseMessage(message, 1000);
        view.clearInput();
        run();
    }

    /**
     * Prompts the user to choose a card to see its details and waits for the user's input to go back.
     */
    protected void seeDetailedCard() {
        view.printMessage("Write the id of the card you want to see: (type '$exit' to go back)");
        String id = view.getInput();
        if (id.startsWith("$")) {
            if (id.equals("$exit")) {
                run();
                return;
            } else if (id.equals("$stop")) {
                return;
            }
            seeDetailedCard();
            return;
        }

        if (!id.matches("[RGOS]C\\d\\d")) {
            showResponseMessage("Invalid card id.", 500);
            seeDetailedCard();
            return;
        }

        ViewController controller = view.getController();
        ViewModel vm = controller.getModel();

        ImmPlayableCard card;

        if (Objects.equals(currentPlayAreaUsername, controller.getUsername())) {
            SelfViewPlayer player = vm.getSelfPlayer();
            card = player.getPlayArea().getPlayedCards().values().stream()
                    .filter(playableCard -> playableCard.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        } else {
            ViewPlayer player = vm.getOpponent(currentPlayAreaUsername);
            card = player.getPlayArea().getPlayedCards().values().stream()
                    .filter(playableCard -> playableCard.getId().equals(id))
                    .findFirst()
                    .orElse(null);

        }
        if (card == null) {
            showResponseMessage("Card not found in the current PlayArea.", 1000);
            seeDetailedCard();
        } else {
            view.printMessage(card.printCard(0));
            seeDetailedCard();
        }
    }

    /**
     * Method to handle the {@link it.polimi.ingsw.eventUtils.event.fromView.game.local.NewGameStatusEvent NewGameStatusEvent}.
     * @param message The message for this event.
     */
    protected void handleNewGameStatus(String message) {
        clearConsole();
        view.stopInputRead(true);
        view.clearInput();
        if (controller.getGameStatus().equals(GameStatus.WAITING)) {
            showResponseMessage(message, 1500);
            transition(new WaitingReconnectState(view));
        } else {
            view.printMessage(message);
        }
    }

    /**
     * Method to handle the {@link it.polimi.ingsw.eventUtils.event.fromModel.EndedGameEvent EndedGameEvent}.
     * @param message The message for this event.
     */
    protected void handleGameEndedEvent(String message) {
        clearConsole();
        view.stopInputRead(true);
        showResponseMessage(message, 2000);
        view.clearInput();
        transition(new EndedGameState(view));
    }

    /**
     * Sets the username of the player whose play area is currently being shown.
     * @param username The username to set.
     */
    protected void setCurrentPlayAreaUsername(String username) {
        currentPlayAreaUsername = username;
    }
}
