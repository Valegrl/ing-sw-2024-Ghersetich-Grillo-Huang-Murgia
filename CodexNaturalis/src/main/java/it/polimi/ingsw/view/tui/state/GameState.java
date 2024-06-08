package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.utils.Pair;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class GameState extends ViewState {

    private String currentPlayAreaUsername = view.getUsername();

    public GameState(View view) {
        super(view);
    }

    protected void showVisibleDecks() {
        clearConsole();
        view.printMessage(controller.decksToString());
        if(waitInputToGoBack())
            run();
    }

    protected void showOtherPlayerPlayArea() {
        clearConsole();
        List<String> players = new ArrayList<>(controller.getPlayerUsernames());
        players.remove(controller.getUsername());
        view.printMessage("Choose a player: ");
        int choice = readChoiceFromInput(players);

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

    protected void showObjectiveCards() {
        clearConsole();
        view.printMessage(controller.objectiveCardsToString());
        if(waitInputToGoBack())
            run();
    }

    protected void showInfoMessage(String message) {
        clearConsole();
        view.stopInputRead(true);
        showResponseMessage(message, 1000);
        view.clearInput();
        run();
    }

    protected List<String> rangeList(int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());
    }

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

        // TODO check card ID validity through a regEx

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

    public void handleNewGameStatus(String message) {
        controller.setPreviousGameStatus(new Pair<>(controller.getGameStatus(), this));
        clearConsole();
        view.stopInputRead(true);
        showResponseMessage(message, 2000);
        view.clearInput();
        if(controller.getGameStatus().equals(GameStatus.WAITING))
            transition(new WaitingReconnectState(view));
        else
            run();
    }

    protected void setCurrentPlayAreaUsername(String username) {
        currentPlayAreaUsername = username;
    }
}
