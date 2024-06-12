package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetChatMessagesEvent;
import static it.polimi.ingsw.utils.AnsiCodes.*;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

public class ChatState extends ViewState {

    private ViewState previousState;

    private boolean inChat = false;

    public ChatState(View view, ViewState previousState) {
        super(view);
        this.previousState = previousState;
    }

    @Override
    public void run() {
        clearConsole();
        view.printMessage(boldText("Game chat") + "\n(type $exit to leave chat, '$pm username msg' to send a private message):");

        inChat = true;
        controller.newViewEvent(new GetChatMessagesEvent());

        waitForResponse();

        String message = view.getInput();
        while (!message.equals("$exit") && inChat) {
            if (message.startsWith("$")) {
                if (message.startsWith("$pm")) {
                    sendPrivateMessage(message);
                } else {
                    printMessage("Invalid command.");
                    inputMessageBox();
                }
            } else {
                controller.newViewEvent(new ChatGMEvent(new ChatMessage(message)));
                clearLine();
                view.print(CURSOR_UP_ONE);
                clearLine();
            }
            message = view.getInput();
        }
        if (inChat) {
            inChat = false;
            view.clearInput();
            view.stopInputRead(true);
            transition(previousState);
        }
    }

    @Override
    public boolean handleInput(int input) {
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case GET_CHAT_MESSAGES:
                view.print(message);
                notifyResponse();
                break;
            case CHAT_GM, CHAT_PM:
                if (feedback.equals(Feedback.SUCCESS)) {
                    view.clearInput();
                    printMessage(message);
                } else {
                    printMessage("Message not sent: " + message);
                }
                break;
            case UPDATE_LOBBY_PLAYERS:
                printMessage(boldText("Lobby info: ") + message);
                view.clearInput();
                break;
            case KICKED_PLAYER_FROM_LOBBY:
                inChat = false;
                view.stopInputRead(true);
                view.clearInput();
                clearLine();
                showResponseMessage(message, 2000);
                transition(new MenuState(view));
                return;
            case UPDATE_GAME_PLAYERS:
                view.clearInput();
                if (previousState.inLobby()) {
                    inChat = false;
                    view.stopInputRead(true);
                    clearLine(); // remove input box
                    transition(new GameSetupState(view));
                } else if (previousState.inGame()) { // Game started
                    clearLine();
                    printMessage(boldText("Game info: ") + message);
                    view.clearInput();
                } else {
                    throw new IllegalStateException("Unexpected previous state.");
                }
                break;
            case CHOOSE_TOKEN_SETUP:
                if (previousState.inGame()) {
                    if (controller.isInTokenSetup()) {
                        view.clearInput();
                        printMessage(boldText("Game info: ") + message);
                    } else {
                        inChat = false;
                        view.stopInputRead(true);
                        clearLine(); // remove input box
                        previousState.handleResponse(feedback, message, eventID); // make GameSetupState handle the tokenSetup transition
                    }
                }
                break;
            case UPDATE_LOCAL_MODEL:
                if (previousState.inGame()) {
                    inChat = false;
                    view.stopInputRead(true);
                    clearLine(); // remove input box
                    previousState.handleResponse(feedback, message, eventID); // make TokenSetupState handle the game transition
                }
                return;
            case OTHER_PLACE_CARD, OTHER_DRAW_CARD, SELF_TURN_TIMER_EXPIRED:
                view.clearInput();
                printMessage(boldText("Game info: ") + message);
                break;
            case NEW_TURN:
                view.clearInput();
                printMessage(boldText("Game info: ") + message);
                if (controller.hasTurn()) {
                    previousState = new PlaceCardState(view);
                } else {
                    previousState = new WaitForTurnState(view);
                }
                break;
            case NEW_GAME_STATUS, ENDED_GAME:
                inChat = false;
                clearLine(); // remove input box
                view.stopInputRead(true);
                previousState.handleResponse(feedback, message, eventID); // make GameState handle the new game status
                return;
            default:
                break;
        }
        inputMessageBox();
    }

    private void sendPrivateMessage(String message) {
        String[] split = message.split(" ");
        if (split.length < 3) {
            printMessage("Invalid command. Use '$pm username message'.");
            inputMessageBox();
        } else {
            if (split[1].equals(view.getUsername())) {
                printMessage("You can't send a private message to yourself.");
                inputMessageBox();
            } else if (controller.getPlayersStatus().containsKey(split[1])) {
                String recipient = split[1];
                String msg = String.join(" ", Arrays.copyOfRange(split, 2, split.length));
                controller.newViewEvent(new ChatPMEvent(new PrivateChatMessage(recipient, msg)));
            } else {
                printMessage("Player not in match.");
                inputMessageBox();
            }
        }
    }

    private void inputMessageBox() {
        view.print("[" + view.getUsername() + "]: "); // wait for a message to send
    }

    private void printMessage(String message) {
        clearLine();
        view.printMessage(message);
    }

    public boolean inChat() {
        return inChat;
    }

    public boolean inGame() {
        return previousState.inGame();
    }

    public boolean inLobby() {
        return previousState.inLobby();
    }
}
