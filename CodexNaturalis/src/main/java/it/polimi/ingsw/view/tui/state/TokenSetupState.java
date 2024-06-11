package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChosenTokenSetupEvent;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenSetupState extends ViewState {

    private String tokensMessage;

    private int numTokens;

    private boolean choseToken = false;

    private boolean choosingToken = false;

    public TokenSetupState(View view) {
        super(view);
    }

    @Override
    public void run() {
        controller.setInSetup(new Pair<>(true, true));
        view.stopInputRead(true);
        view.clearInput();
        clearConsole();

        tokensMessage = controller.availableTokensMessage();
        numTokens = controller.getAvailableTokens().size();

        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Choose token"
                , "Open chat"
                , "Quit game"));

        handleInput(choice);
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                showAvailableTokens();
                break;
            case 2:
                transition(new ChatState(view, this));
                break;
            case 3:
                quitGame();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        tokensMessage = controller.availableTokensMessage();
        numTokens = controller.getAvailableTokens().size();
        switch (EventID.getByID(eventID)) {
            case CHOOSE_TOKEN_SETUP:
                view.clearInput();
                if (!choseToken) clearLine();
                showResponseMessage(message, 500);
                if (choosingToken) {
                    view.stopInputRead(true);
                    showAvailableTokens();
                }
                break;
            case CHOSEN_TOKEN_SETUP:
                if (feedback == Feedback.SUCCESS) {
                    choseToken = true;
                    choosingToken = false;
                    notifyResponse();
                    clearConsole();
                    view.printMessage(message);
                    view.printMessage("Wait for other players to chose their token. . .\n");
                } else {
                    showResponseMessage("Failed to choose token: " + message, 500);
                    clearConsole();
                    showAvailableTokens();
                }
                break;
            case UPDATE_GAME_PLAYERS:
                view.clearInput();
                clearLine();
                view.printMessage(message);
                break;
            case UPDATE_LOCAL_MODEL:
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
                    if (controller.hasTurn()) {
                        controller.setPreviousGameStatus(new Pair<>(GameStatus.RUNNING, new PlaceCardState(view)));
                    } else {
                        controller.setPreviousGameStatus(new Pair<>(GameStatus.RUNNING, new WaitForTurnState(view)));
                    }
                    transition(new WaitingReconnectState(view));
                } else {
                    throw new IllegalStateException("Unexpected game status.");
                }
                break;
            case QUIT_GAME:
                if (feedback == Feedback.SUCCESS) {
                    notifyResponse();
                    showResponseMessage(message, 1500);
                    transition(new MenuState(view));
                } else {
                    showResponseMessage("Failed to quit game: " + message, 2000);
                    run();
                }
                break;
            default:
                break;
        }
    }

    private void showAvailableTokens() {
        choosingToken = true;
        clearConsole();
        view.printMessage(tokensMessage);
        int choice = readIntFromInput(1, numTokens);
        if (choice == -1) {
            choosingToken = false;
            run();
            return;
        } else if (choice == 0) {
            return;
        }
        Token chosenToken = extractTokenFromChoice(choice);
        if (chosenToken == null) {
            view.printMessage("Invalid token. Please try again.");
            showAvailableTokens();
            return;
        }
        controller.newViewEvent(new ChosenTokenSetupEvent(chosenToken));
    }

    private Token extractTokenFromChoice(int choice) {
        String regex = choice + "- \\033\\[38;2;\\d+;\\d+;\\d+m(.*?)\\u001B\\[0m";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tokensMessage);

        if (matcher.find()) {
            return Token.fromString(matcher.group(1));
        } else {
            return null;
        }
    }

    @Override
    public boolean inGame() {
        return true;
    }
}
