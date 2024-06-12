package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetLobbyInfoEvent;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

public class LobbyState extends ViewState {
    public LobbyState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();

        showLobbyInfo();

        if (controller.isLobbyLeader()) {
            lobbyLeaderOptions();
        } else {
            playerOptions();
        }
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                toggleReadyStatus();
                break;
            case 2:
                transition(new ChatState(view, this));
                break;
            case 3:
                quitLobby();
                break;
            case 4:
                if (controller.isLobbyLeader()) {
                    kickPlayer();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.GET_LOBBY_INFO:
                view.printMessage(message);
                break;
            case EventID.PLAYER_READY, EventID.PLAYER_UNREADY:
                if (feedback == Feedback.FAILURE) {
                    showResponseMessage("Ready status change failed: " + message, 2000);
                }
                run();
                break;
            case EventID.QUIT_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 1500);
                    transition(new MenuState(view));
                } else {
                    showResponseMessage("Failed to leave lobby: " + message, 2000);
                    run();
                }
                break;
            case EventID.KICK_FROM_LOBBY:
                if (feedback == Feedback.FAILURE) {
                    showResponseMessage("Failed to kick player: " + message, 2000);
                }
                break;
            case EventID.UPDATE_LOBBY_PLAYERS:
                view.stopInputRead(true);
                clearLine();
                view.clearInput();
                clearConsole();
                showResponseMessage(message, 1000);
                run();
                break;
            case EventID.KICKED_PLAYER_FROM_LOBBY:
                view.clearInput();
                clearLine();
                view.stopInputRead(true);
                showResponseMessage(message, 2000);
                transition(new MenuState(view));
                break;
            case EventID.UPDATE_GAME_PLAYERS:
                view.clearInput();
                clearLine();
                view.stopInputRead(true);
                notifyResponse();
                transition(new GameSetupState(view));
                break;
            default:
                break;
        }
        notifyResponse();
    }

    @Override
    public boolean inLobby() {
        return true;
    }

    private void lobbyLeaderOptions() {
        String readyStatusString = controller.getLobbyLeader().getValue() ? "Unready" : "Ready";

        view.printMessage("Choose an option (lobby leader):");
        int choice = readChoiceFromInput(Arrays.asList(
                readyStatusString
                , "Open chat"
                , "Leave lobby"
                , "Kick player"));

        handleInput(choice);
    }

    private void playerOptions() {
        String readyStatusString = controller.getPlayersStatus().get(view.getUsername()) ? "Unready" : "Ready";

        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                readyStatusString
                , "Open chat"
                , "Leave lobby"));

        handleInput(choice);
    }

    private void toggleReadyStatus() {
        Event event;
        if(controller.getPlayersStatus().get(view.getUsername())) {
            event = new PlayerUnreadyEvent();
        } else {
            event = new PlayerReadyEvent();
        }
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void quitLobby() {
        Event event = new QuitLobbyEvent();
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void kickPlayer() {
        view.printMessage("\nChoose a player to kick:");
        int choice = -1;
        if (controller.getPlayersStatus().size() > 1) {
            choice = readChoiceFromInput(controller.getPlayersStatus().keySet().stream().filter(p -> !p.equals(view.getUsername())).toList());
        } else {
            showResponseMessage("No players to kick.", 1000);
        }
        if (choice == -1) {
            this.run();
            return;
        }

        int i = 1;
        String chosenUser = "";
        for (String username : controller.getPlayersStatus().keySet()) {
            if (!username.equals(view.getUsername())) {
                if (i == choice) {
                    chosenUser = username;
                }
                i++;
            }
        }
        Event event = new KickFromLobbyEvent(chosenUser);
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void showLobbyInfo() {
        Event event = new GetLobbyInfoEvent();
        controller.newViewEvent(event);
        waitForResponse();
    }
}
