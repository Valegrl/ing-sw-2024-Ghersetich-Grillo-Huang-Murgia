package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.controller.ViewController;

import java.util.List;

public abstract class ViewState {
    protected View view;

    protected final ViewController controller;

    private boolean responseReceived = false;

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
        int input;

        while (true) {
            input = Integer.parseInt(view.getInput());

            if (input >= lowerBound && input <= upperBound) {
                break;
            } else {
                view.printMessage("Invalid input. Try again.");
            }
        }
        return input;
    }

    protected void waitForResponse() {
        synchronized (viewLock) {
            while (!responseReceived) {
                try {
                    viewLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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

    protected void notifyResponse() {
        synchronized (viewLock) {
            responseReceived = true;
            viewLock.notifyAll();
        }
    }

    public Object getViewLock() {
        return viewLock;
    }
}
