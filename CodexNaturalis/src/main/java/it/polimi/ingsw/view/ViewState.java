package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.controller.ViewController;

import java.util.InputMismatchException;

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

    protected int readChoiceFromInput(String opt1, String opt2){
        String inputRead;
        int choice = -1;

        while(true) {
            view.printMessage("1 - " + opt1);
            view.printMessage("2 - " + opt2);
            try {
                inputRead = view.getInput();
            } catch ( InputMismatchException ex){
                view.printMessage("Invalid selection!");
                continue;
            }

            try {
                choice = Integer.parseInt(inputRead);
            } catch (NumberFormatException e) {
                view.printMessage("Invalid selection!");
                continue;
            }
            if(choice != 1 && choice != 2)
                view.printMessage("Invalid selection!");
            else
                break;
        }
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

    public void setResponseReceived(boolean responseReceived) {
        this.responseReceived = responseReceived;
    }

    public Object getViewLock() {
        return viewLock;
    }
}
