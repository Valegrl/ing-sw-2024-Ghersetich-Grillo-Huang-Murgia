package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LoginEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.RegisterEvent;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

public class LoginState extends ViewState {

    public LoginState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput("Login", "Register");

        handleInput(choice);
    }

    @Override
    public boolean handleInput(int input) {
        switch (input) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        synchronized (viewLock) {
            setResponseReceived(true);
            viewLock.notify();
        }
        if (EventID.LOGIN.getID().equals(eventID)) {
            if (feedback == Feedback.SUCCESS) {
                showResponseMessage("Login for user '" + view.getUsername() + "' successful", 2000);
                transition(new MenuState(view));
            } else {
                showResponseMessage("Login failed: " + message, 2000);
                login();
            }
        } else if (EventID.REGISTER.getID().equals(eventID)) {
            if (feedback == Feedback.SUCCESS) {
                showResponseMessage("Registered user successfully! Now log in to your account.", 2000);
            } else {
                showResponseMessage("Registration failed: " + message, 2000);
            }
            run();
        }
    }

    private void login(){
        view.printMessage("Please provide your username:");
        String user = view.getInput();
        view.printMessage("Please provide your password:");
        String psw = view.getInput();

        Event event = new LoginEvent(user, psw);
        controller.newViewEvent(event);

        waitForResponse();
    }

    private void register(){
        view.printMessage("Please choose your username:");
        String user = view.getInput();
        view.printMessage("Please choose your password:");
        String psw = view.getInput();

        Event event = new RegisterEvent(user, psw);
        controller.newViewEvent(event);

        waitForResponse();
    }
}
