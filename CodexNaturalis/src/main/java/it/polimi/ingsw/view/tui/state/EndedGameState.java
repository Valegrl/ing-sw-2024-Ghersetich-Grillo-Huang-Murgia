package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.AnsiCodes;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.viewModel.EndedGameData;

import java.util.Arrays;
import java.util.List;

public class EndedGameState extends GameState {

    private boolean firstRun = true;

    public EndedGameState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();

        if (firstRun)
            showResponseMessage("Game ended!\n\nHere are the results:\n", 2000);
        else
            view.printMessage("Game ended!\n\nHere are the results:\n");

        view.printMessage(controller.getEndedGameData().resultsToString());

        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "See a specific play area"
                , "See visible decks"
                , "Back to main menu"));
        handleInput(choice);
    }

    @Override
    public boolean handleInput(int input) {
        firstRun = false;
        switch (input) {
            case 1:
                showSpecificPlayArea();
                break;
            case 2:
                view.printMessage(controller.getEndedGameData().decksToString());
                if(waitInputToGoBack())
                    run();
                break;
            case 3:
                view.stopInputRead(true);
                controller.gameEnded();
                transition(new MenuState(view));
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }

    @Override
    public boolean inGame() {
        return true;
    }

    private void showSpecificPlayArea() {
        view.printMessage("Choose a play area to see:");
        List<String> usernames = controller.getEndedGameData().getPlayAreas().keySet().stream().toList();
        List<String> coloredUsernames = usernames.stream()
                .map(username -> controller.getPlayerToken(username).getColorCode() + username + AnsiCodes.RESET)
                .toList();
        int choice = readChoiceFromInput(coloredUsernames);
        if (choice == -1) {
            run();
        } else {
            view.printMessage(controller.getEndedGameData().playAreaToString(usernames.get(choice - 1)));
            if (waitInputToGoBack())
                run();
        }
    }
}
