package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChosenTokenSetupEvent;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenSetupState extends ViewState {

    private boolean inChat = false;

    private String tokensMessage;

    private int numTokens;

    public TokenSetupState(View view, String tokensMessage, int numTokens) {
        super(view);
        this.tokensMessage = tokensMessage;
        this.numTokens = numTokens;
    }

    @Override
    public void run() {
        view.stopInputRead(false);
        clearConsole();

        showAvailableTokens();
    }

    @Override
    public boolean handleInput(int input) {
        return false;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }

    private void showAvailableTokens() {
        view.printMessage(tokensMessage);
        int choice = readIntFromInput(1, numTokens);
        Token chosenToken = extractTokenFromChoice(choice);
        if (chosenToken == null) {
            view.printMessage("Invalid token. Please try again.");
            showAvailableTokens();
            return;
        }
        controller.newViewEvent(new ChosenTokenSetupEvent(chosenToken));
    }

    private Token extractTokenFromChoice(int choice) {
        Pattern pattern = Pattern.compile(choice + "- \\s*([a-z]+)");
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
