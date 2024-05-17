package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

public class ChatState extends ViewState {

    private final ViewState previousState;

    private boolean inGlobalChat = false;

    public ChatState(View view, ViewState previousState) {
        super(view);
        this.previousState = previousState;
    }

    @Override
    public void run() {
        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Send private message"
                , "Open global chat"
                , "Back"));

        handleInput(choice);
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                // sendPrivateMessage();
                break;
            case 2:
                openGlobalChat();
                break;
            case 3:
                transition(previousState);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.CHAT_GM:
                if (feedback.equals(Feedback.SUCCESS)) {
                    view.printMessage("\r" + " ".repeat(80) + "\r");
                    view.printMessage(message + "\n");
                    inputMessageBox();
                } else {
                    view.printMessage("Message not sent: " + message);
                }
                break;
            default:
                break;
        }
    }

    private void openGlobalChat() {

        // TODO ask ViewController for recent global messages
        inGlobalChat = true;
        // TODO make command to exit global chat
        inputMessageBox();


        // TODO when exiting chat, set inGlobalChat to false
    }

    private void sendPrivateMessage() {
    }

    private void inputMessageBox() {
        view.printMessage("[" + view.getUsername() + "]: "); // wait for a message to send
    }

    public boolean inChat() {
        return inGlobalChat;
    }
}
