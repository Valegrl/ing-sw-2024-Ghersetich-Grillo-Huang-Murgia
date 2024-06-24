package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.DeleteAccountEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LogoutEvent;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

/**
 * Represents the state of the view when the user is in the profile settings menu.
 */
public class ProfileSettingsState extends ViewState {
    /**
     * Constructor for the ProfileSettingsState.
     * @param view The TUI instance that this state belongs to.
     */
    public ProfileSettingsState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList(
                "Logout"
                , "Delete account"
                , "Back to menu"));

        handleInput(choice);
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                logout();
                break;
            case 2:
                view.printMessage("Are you sure you want to delete your account permanently?:");
                int choice = readChoiceFromInput(Arrays.asList("Yes", "No"));
                if (choice == 1) deleteAccount();
                else {
                    showResponseMessage("Your account was NOT deleted.", 1000);
                    run();
                }
                break;
            case 3:
                transition(new MenuState(view));
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        notifyResponse();
        switch (eventID) {
            case "LOGOUT":
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 1500);
                    transition(new LoginState(view));
                } else {
                    showResponseMessage("Logout failed: " + message, 2000);
                    run();
                }
                break;
            case "DELETE_ACCOUNT":
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage("Account deleted successfully!", 1500);
                    transition(new LoginState(view));
                } else {
                    showResponseMessage("Account deletion failed: " + message, 2000);
                    deleteAccount();
                }
                break;
        }
    }

    /**
     * Logs the user out of his account.
     */
    private void logout() {
        Event event = new LogoutEvent();
        controller.newViewEvent(event);

        waitForResponse();
    }

    /**
     * Deletes the user's account.
     */
    private void deleteAccount() {
        Event event = new DeleteAccountEvent();
        controller.newViewEvent(event);

        waitForResponse();
    }

    @Override
    public boolean inMenu() {
        return true;
    }
}
