package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.controller.ViewController;

import java.util.List;

public abstract class ViewState {
    protected View view;

    protected final ViewController controller;

    protected final Object viewLock = new Object();

    public ViewState(View view) {
        this.view = view;
        this.controller = view.getController();
    }

    public void transition(ViewState nextState) {
        view.setState(nextState);
        nextState.run();
    }

    abstract public void run();

    abstract public boolean handleInput(int input);

    abstract public void handleResponse(Feedback feedback, String message, String eventID);

    public boolean inMenu() {return false;};

    public boolean inLobby() {return false;};

    public boolean inGame() {return false;};

    protected int readChoiceFromInput(List<String> options) {
        int choice = -1;

        for (int i = 0; i < options.size(); i++) {
            view.printMessage((i + 1) + " - " + options.get(i));
        }

        choice = readIntFromInput(1, options.size());

        return choice;
    }

    protected int readIntFromInput(int lowerBound, int upperBound) {
        int input = -1;
        String inputString;
        while (true) {
            inputString = view.getInput();
            if (inputString.equals("$stop")) {
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

    protected void waitForResponse() {
        synchronized (viewLock) {
            try {
                viewLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void backToStateStart() {
        view.getState().run();
    }

    public void clearConsole() {
        try {
            if( System.getProperty("os.name").contains("Windows") )
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                view.printMessage("\033[H\033[2J");
        } catch (Exception ignored) {}
    }

    public void showResponseMessage(String message, int sleepTime) {
        view.printMessage(message);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ignored) {}
    }

    public void notifyResponse() {
        synchronized (viewLock) {
            viewLock.notifyAll();
        }
    }
}
