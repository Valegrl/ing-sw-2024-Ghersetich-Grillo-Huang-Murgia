package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

/**
 * Represents the state of the view when the user is in the main menu.
 */
public class MenuState extends ViewState {
    /**
     * Constructor for the MenuState.
     * @param view The TUI instance that this state belongs to.
     */
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
                    showResponseMessage(message, 1000);
                    transition(new LobbyState(view));
                } else {
                    showResponseMessage("Lobby creation failed: " + message, 2000);
                    createLobby();
                }
                break;
            case EventID.AVAILABLE_LOBBIES:
                if (feedback == Feedback.SUCCESS) {
                    clearConsole();
                    if (!controller.getLobbies().isEmpty())
                        view.printMessage(message);
                } else {
                    showResponseMessage("Failed to get available lobbies from server: " + message, 2000);
                    run();
                }
                break;
            case EventID.JOIN_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 1000);
                    transition(new LobbyState(view));
                } else {
                    showResponseMessage("Failed to join lobby: " + message, 2000);
                    availableLobbies();
                }
                break;
            case EventID.GET_MY_OFFLINE_GAMES:
                if (feedback == Feedback.SUCCESS) {
                    clearConsole();
                    if(!controller.getOfflineGames().isEmpty())
                        view.printMessage(message);
                } else {
                    showResponseMessage("Failed to get offline games: " + message, 2000);
                    run();
                }
                break;
            case EventID.RECONNECT_TO_GAME:
                if (feedback == Feedback.SUCCESS) {
                    view.stopInputRead(true);
                    clearConsole();
                    showResponseMessage(message, 2000);
                    transition(new ReconnectToGameState(view));
                } else {
                    showResponseMessage("Failed to reconnect to game: " + message, 1500);
                    reconnect();
                }
                break;
            case ENDED_GAME:
                clearConsole();
                view.stopInputRead(true);
                showResponseMessage(message, 2000);
                view.clearInput();
                transition(new EndedGameState(view));
                break;
        }
        notifyResponse();
    }

    /**
     * Handles the creation of a new lobby.
     */
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
                if(lobbyName.length() <= 32)
                    break;
                else
                    view.printMessage("Lobby name must be 32 characters or less.");
            } else {
                view.printMessage("Lobby name cannot be empty.");
            }
        }

        int numPlayers;
        while (true) {
            int maxPlayers = JsonConfig.getInstance().getMaxLobbySize();
            view.printMessage("Number of required players (2-"+ maxPlayers+"):");
            try {
                numPlayers = readIntFromInput(2, maxPlayers);
                if (numPlayers >= 2 && numPlayers <= maxPlayers) {
                    break;
                } else if (numPlayers == -1) { // exit
                    createLobby();
                    return;
                } else {
                    view.printMessage("Number of players must be between 2 and " + maxPlayers + ".");
                }
            } catch (NumberFormatException ex) {
                view.printMessage("Invalid input. Please enter a number.");
            }
        }

        Event event = new CreateLobbyEvent(lobbyName, numPlayers);
        controller.newViewEvent(event);

        waitForResponse();
    }

    /**
     * Handles the request for available lobbies.
     */
    private void availableLobbies() {
        Event event = new AvailableLobbiesEvent();
        controller.newViewEvent(event);

        waitForResponse();

        if (!controller.getLobbies().isEmpty())
            chooseLobby();
        else {
            showResponseMessage("No lobbies available.", 1000);
            run();
        }
    }

    /**
     * Handles the choice of a lobby to join.
     */
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

    /**
     * Handles the reconnection to a game.
     */
    private void reconnect() {
        Event event = new GetMyOfflineGamesEvent();
        controller.newViewEvent(event);
        waitForResponse();

        if(!controller.getOfflineGames().isEmpty())
            chooseReconnect();
        else {
            showResponseMessage("There's no games to reconnect to.", 1000);
            run();
        }
    }

    /**
     * Handles the choice of a game to reconnect to.
     */
    private void chooseReconnect() {
        view.printMessage("Choose the game to reconnect to ('-1' to exit):");
        int choice = readIntFromInput(1, controller.getOfflineGames().size());
        if (choice == -1) {
            run();
            return;
        }

        Event event = new ReconnectToGameEvent(controller.getOfflineGames().get(choice - 1).getId());
        controller.newViewEvent(event);
    }

    /**
     * Handles the profile settings.
     */
    private void profileSettings() {
        transition(new ProfileSettingsState(view));
    }

    @Override
    public boolean inMenu() {
        return true;
    }

}
