package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.view.tui.state.MenuState;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.utils.AnsiCodes.*;

/**
 * State pattern class representing the state of the view.
 */
public abstract class ViewState {
    /**
     * The view this state belongs to.
     */
    protected View view;

    /**
     * The controller of the view.
     */
    protected final ViewController controller;

    /**
     * The lock used to synchronize the view.
     */
    protected final Object viewLock = new Object();

    /**
     * Constructor for the view state.
     * @param view The view this state belongs to.
     */
    public ViewState(View view) {
        this.view = view;
        this.controller = view.getController();
    }

    /**
     * Transitions to the next state.
     * @param nextState The next state to transition to.
     */
    public void transition(ViewState nextState) {
        view.setState(nextState);
        nextState.run();
    }

    /**
     * Runs the state.
     */
    abstract public void run();

    /**
     * Handles the input from the user.
     * @param input The input from the user.
     * @return true if the input has been handled, false otherwise.
     */
    abstract public boolean handleInput(int input);

    /**
     * Handles the response from the server.
     * @param feedback The feedback from the server.
     * @param message The message from the server.
     * @param eventID The ID of the event.
     */
    abstract public void handleResponse(Feedback feedback, String message, String eventID);

    /**
     * Returns whether the state is in the menu.
     * @return true if the state is in the menu, false otherwise.
     */
    public boolean inMenu() {return false;}

    /**
     * Returns whether the state is in the lobby.
     * @return true if the state is in the lobby, false otherwise.
     */
    public boolean inLobby() {return false;}

    /**
     * Returns whether the state is in the game.
     * @return true if the state is in the game, false otherwise.
     */
    public boolean inGame() {return false;}

    /**
     * Returns whether the state is in the chat.
     * @return true if the state is in the chat, false otherwise.
     */
    public boolean inChat() {return false;}

    /**
     * Returns the state of the view.
     * @return The state of the view.
     */
    public ViewState getState() {return this;}

    /**
     * Prints the possible choices of input and reads an integer representing the user's choice.
     * @param options The list of possible choices.
     * @return The integer representing the user's choice.
     */
    protected int readChoiceFromInput(List<String> options) {
        int choice = -1;

        for (int i = 0; i < options.size(); i++) {
            view.printMessage((i + 1) + " - " + options.get(i));
        }

        choice = readIntFromInput(1, options.size());

        return choice;
    }

    /**
     * Reads an integer from the input.
     * @param lowerBound The lower bound of the integer.
     * @param upperBound The upper bound of the integer.
     * @return The integer read from the input.
     */
    protected int readIntFromInput(int lowerBound, int upperBound) {
        int input = -1;
        String inputString;
        while (true) {
            inputString = view.getIntFromInput();
            if (inputString == null) {
                return 0;
            } else if (inputString.equals("$stop")) {
                input = 0;
            } else if (!inputString.isEmpty()) {
                try {
                    input = Integer.parseInt(inputString);
                } catch (NumberFormatException e) {
                    view.printMessage("Invalid input. Try again.");
                    continue;
                }
            }

            if ((input >= lowerBound && input <= upperBound) || input == -1 || input == 0) {
                break;
            } else {
                view.printMessage("Invalid input. Try again.");
            }
        }
        return input;
    }

    /**
     * Function to wait for a response from the server, synchronizing on the {@code viewLock}.
     */
    protected void waitForResponse() {
        synchronized (viewLock) {
            try {
                viewLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Function to go back to the start of the current state.
     */
    public void backToStateStart() {
        view.getState().run();
    }

    /**
     * Prompt the user to confirm if they want to quit the game, if yes, generates an event to communicate it to the server.
     */
    protected void quitGame() {
        view.printMessage("Are you sure you want to abandon the current game?:");
        int choice = readChoiceFromInput(Arrays.asList("Yes", "No"));
        if (choice == 1) {
            Event event = new QuitGameEvent();
            controller.newViewEvent(event);
            waitForResponse();
        } else {
            showResponseMessage("You are still in the game.", 200);
            run();
        }
    }

    /**
     * Handles the response from the server when abandoning the game.
     * @param feedback The feedback from the server.
     * @param message The message from the server.
     */
    public void handleQuitGame(Feedback feedback, String message) {
        if (feedback == Feedback.SUCCESS) {
            showResponseMessage(message, 1500);
            transition(new MenuState(view));
        } else {
            showResponseMessage(message, 1000);
            run();
        }
    }

    /**
     * @return true if the user wants to go back, false if the read has been interrupted
     */
    protected boolean waitInputToGoBack() {
        view.printMessage("Press and enter any key to go back: ");
        String in = view.getInput();
        return !in.equals("$stop");
    }

    /**
     * Clears the console.
     */
    protected void clearConsole() {
        try {
            if( System.getProperty("os.name").contains("Windows") )
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                view.printMessage("\033[H\033[2J");
        } catch (Exception ignored) {}
    }

    /**
     * Clears the current line.
     */
    protected void clearLine() {
        view.print(MOVE_CURSOR_START + CLEAR_LINE);
    }

    /**
     * Prints a message in bold.
     * @param text The text to print in bold.
     * @return The text in bold.
     */
    protected String boldText(String text) {
        return BOLD + text + RESET;
    }

    /**
     * Prints a message and sleeps for a given amount of time.
     * @param message The message to print.
     * @param sleepTime The time to sleep, in ms.
     */
    public void showResponseMessage(String message, int sleepTime) {
        view.printMessage(message);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ignored) {}
    }

    /**
     * Notifies the view that a response has been received.
     */
    public void notifyResponse() {
        synchronized (viewLock) {
            viewLock.notifyAll();
        }
    }
}
