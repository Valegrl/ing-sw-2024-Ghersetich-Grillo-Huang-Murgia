package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetChatMessagesEvent;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

public class ChatState extends ViewState {

    private final ViewState previousState;

    private boolean inChat = false;

    public ChatState(View view, ViewState previousState) {
        super(view);
        this.previousState = previousState;
    }

    @Override
    public void run() {
        clearConsole();
        view.printMessage("\u001B[1mGame chat\u001B[0m\n(type $exit to leave chat, '$pm username msg' to send a private message):");

        inChat = true;
        controller.newViewEvent(new GetChatMessagesEvent());

        waitForResponse();

        String message = view.getInput();
        while (!message.equals("$exit") && inChat) {
            if (message.startsWith("$")) {
                if (message.startsWith("$pm")) {
                    sendPrivateMessage(message);
                }
            } else {
                controller.newViewEvent(new ChatGMEvent(new ChatMessage(message)));
                view.print("\r\033[2K");
                view.print("\u001B[A\u001B[2K\r");
            }
            message = view.getInput();
        }
        if (inChat) {
            inChat = false;
            view.clearInput();
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
            case EventID.GET_CHAT_MESSAGES:
                view.print(message);
                break;
            case EventID.CHAT_GM, EventID.CHAT_PM:
                if (feedback.equals(Feedback.SUCCESS)) {
                    view.clearInput();
                    printMessage(message);
                } else {
                    printMessage("Message not sent: " + message);
                }
                break;
            case EventID.UPDATE_LOBBY_PLAYERS:
                printMessage("\u001B[1mLobby info: \u001B[0m" + message);
                break;
            case EventID.KICKED_PLAYER_FROM_LOBBY:
                inChat = false;
                view.stopInputRead(true);
                showResponseMessage("\r\033[2K" + message, 2000);
                transition(new MenuState(view));
                return;
            case EventID.UPDATE_GAME_PLAYERS:
                inChat = false;
                view.stopInputRead(true);
                printMessage(""); // remove input box
                transition(new GameSetupState(view));
                return;
            default:
                break;
        }
        inputMessageBox();
        notifyResponse();
    }

    // TODO add events for joined Lobby or left lobby / game

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
        view.printMessage("\r\033[2K" + message);
    }

    public boolean inChat() {
        return inChat;
    }
}
