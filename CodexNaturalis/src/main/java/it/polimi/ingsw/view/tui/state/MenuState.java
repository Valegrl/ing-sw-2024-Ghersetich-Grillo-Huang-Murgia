package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.CreateLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LoginEvent;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

public class MenuState extends ViewState {
    public MenuState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                  "Create lobby"
                , "See available lobbies"
                , "Reconnect to a game"
                , "Profile settings"));

        handleInput(choice);
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                createLobby();
                break;
            case 2:
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        notifyResponse();
        switch (EventID.getByID(eventID)) {
            case EventID.CREATE_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 2000);
                    transition(new LobbyState(view));
                } else {
                    showResponseMessage("Lobby creation failed: " + message, 2000);
                    createLobby();
                }
                break;
            case EventID.AVAILABLE_LOBBIES:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage("Registered user successfully! Now log in to your account.", 2000);
                } else {
                    showResponseMessage("Registration failed: " + message, 2000);
                }
                run();
                break;
        }
    }


    private void createLobby() {
        String lobbyName;
        while (true) {
            view.printMessage("Please provide the lobby name:");
            lobbyName = view.getInput().trim();
            if (!lobbyName.isEmpty()) {
                break;
            } else {
                view.printMessage("Lobby name cannot be empty.");
            }
        }

        int numPlayers;
        while (true) {
            view.printMessage("Number of required players (2-4):");
            try {
                numPlayers = Integer.parseInt(view.getInput());
                if (numPlayers >= 2 && numPlayers <= 4) {
                    break;
                } else {
                    view.printMessage("Number of players must be between 2 and 4.");
                }
            } catch (NumberFormatException ex) {
                view.printMessage("Invalid input. Please enter a number.");
            }
        }

        Event event = new CreateLobbyEvent(lobbyName, numPlayers);
        controller.newViewEvent(event);

        waitForResponse();
    }

    @Override
    public boolean inMenu() {
        return true;
    }

}
