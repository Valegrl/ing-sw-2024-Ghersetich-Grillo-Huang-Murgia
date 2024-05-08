package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
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
                availableLobbies();
                break;
            case 3:
                reconnect();
                break;
            case 4:
                profileSettings();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
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
                    showResponseMessage(message, 800);
                } else {
                    showResponseMessage("Failed to get available lobbies from server: " + message, 2000);
                    run();
                }
                break;
            case EventID.JOIN_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 2000);
                    transition(new LobbyState(view));
                } else {
                    showResponseMessage("Failed to join lobby: " + message, 2000);
                    availableLobbies();
                }
                break;
            case EventID.GET_MY_OFFLINE_GAMES:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 800);
                } else {
                    showResponseMessage("Failed to get offline games: " + message, 2000);
                    run();
                }
                break;
            case EventID.RECONNECT_TO_GAME:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 2000);
                    // TODO Reconnect to game status
                } else {
                    showResponseMessage("Failed to reconnect to game: " + message, 2000);
                    reconnect();
                }
                break;
        }
        notifyResponse();
    }

    private void createLobby() {
        String lobbyName;
        while (true) {
            view.printMessage("Please provide the lobby name:");
            lobbyName = view.getInput().trim();
            if (lobbyName.startsWith("$")) {
                if (lobbyName.equals("$exit")) run();
                return;
            }
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

    private void availableLobbies() {
        Event event = new AvailableLobbiesEvent();
        controller.newViewEvent(event);
        waitForResponse();

        if (!controller.getLobbies().isEmpty())
            chooseLobby();
        else run();
    }

    private void chooseLobby() {
        view.printMessage("Choose a lobby ('-1' to exit):");
        int choice = readIntFromInput(1, controller.getLobbies().size());
        if (choice == -1) {
            this.run();
            return;
        }
        Event event = new JoinLobbyEvent(controller.getLobbies().get(choice - 1).getId());
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void reconnect() {
        Event event = new GetMyOfflineGamesEvent();
        controller.newViewEvent(event);
        waitForResponse();

        if(!controller.getOfflineGames().isEmpty())
            chooseReconnect();
        else
            run();
    }

    private void chooseReconnect() {
        view.printMessage("Choose the game to reconnect to ('-1' to exit):");
        int choice = readIntFromInput(1, controller.getOfflineGames().size());
        if (choice == -1) {
            run();
            return;
        }
        Event event = new ReconnectToGameEvent(controller.getOfflineGames().get(choice - 1).getId());
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void profileSettings() {
        transition(new ProfileSettingsState(view));
    }

    @Override
    public boolean inMenu() {
        return true;
    }

}
