package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LoginEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.RegisterEvent;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;

public class LoginState extends ViewState {

    public LoginState(View view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.printMessage("Choose an option:");
        int choice = readChoiceFromInput(Arrays.asList("Login", "Register"));

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
        notifyResponse();
        switch (EventID.getByID(eventID)) {
            case LOGIN:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage("Login for user '" + view.getUsername() + "' successful", 2000);
                    transition(new MenuState(view));
                } else {
                    showResponseMessage("Login failed: " + message, 2000);
                    login();
                }
                break;
            case REGISTER:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage("Registered user successfully! Now log in to your account.", 1000);
                } else {
                    showResponseMessage("Registration failed: " + message, 2000);
                }
                run();
                break;
        }
    }


    private void login(){
        view.printMessage("Please provide your username:");
        String user = view.getInput();
        if (user.startsWith("$")) {
            if (user.equals("$exit")) backToStateStart();
            return;
        }
        view.printMessage("Please provide your password:");
        String psw = view.getInput();
        if (psw.startsWith("$")) {
            if (psw.equals("$exit")) backToStateStart();
            return;
        }

        Event event = new LoginEvent(user, psw);
        controller.newViewEvent(event);

        waitForResponse();
    }

    private void register(){
        view.printMessage("Please choose your username:");
        String user = view.getInput();
        if (user.startsWith("$")) {
            if (user.equals("$exit")) backToStateStart();
            return;
        }
        view.printMessage("Please choose your password:");
        String psw = view.getInput();
        if (psw.startsWith("$")) {
            if (psw.equals("$exit")) backToStateStart();
            return;
        }

        Event event = new RegisterEvent(user, psw);
        controller.newViewEvent(event);

        waitForResponse();
    }

    @Override
    public boolean inMenu() {
        return true;
    }
}
