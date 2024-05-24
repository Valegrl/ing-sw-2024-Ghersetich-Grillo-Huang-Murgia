package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChosenTokenSetupEvent;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenSetupState extends ViewState {

    private String tokensMessage;

    private int numTokens;

    private boolean choseToken = false;

    public TokenSetupState(View view, String tokensMessage, int numTokens) {
        super(view);
        this.tokensMessage = tokensMessage;
        this.numTokens = numTokens;
    }

    @Override
    public void run() {
        view.stopInputRead(true);
        view.clearInput();
        clearConsole();
        view.stopInputRead(false);

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
                // TODO quit game
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
                view.printMessage(message);
                break;
            case CHOSEN_TOKEN_SETUP:
                if (feedback == Feedback.SUCCESS) {
                    choseToken = true;
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
                // TODO transition to game state
                clearConsole();
                showResponseMessage(message, 1000);
                break;
            default:
                break;
        }
    }

    private void showAvailableTokens() {
        clearConsole();
        view.printMessage(tokensMessage);
        int choice = readIntFromInput(1, numTokens);
        Token chosenToken = extractTokenFromChoice(choice);
        if (chosenToken == null) {
            view.printMessage("Invalid token. Please try again.");
            showAvailableTokens();
            return;
        }
        controller.newViewEvent(new ChosenTokenSetupEvent(chosenToken));
        waitForResponse();
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
