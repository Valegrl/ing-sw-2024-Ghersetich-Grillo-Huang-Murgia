package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.view.tui.state.ChooseConnectionState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that represents the Terminal User Interface of the application.
 */
public class TUI implements View {

    /**
     * The {@link BufferedReader} used to read input from the user.
     */
    private BufferedReader in;

    /**
     * The {@link PrintStream} used to print output to the user.
     */
    private PrintStream out;

    /**
     * The {@link ExecutorService} used to manage the handling of responses from server.
     */
    private ExecutorService executor;

    /**
     * The username of the user who is logged in this instance of TUI.
     */
    private String username;

    /**
     * The {@link ViewController} this TUI is associated with which is used for handling view events.
     */
    private ViewController controller;

    /**
     * The {@link ViewState} this TUI is currently in.
     */
    private ViewState state;

    /**
     * The boolean that represents if the TUI is waiting for input.
     */
    private boolean waitingForInput;

    /**
     * The boolean that represents if the TUI should stop reading input.
     */
    private boolean stopInputRead = false;

    /**
     * The boolean that represents if the server is disconnected.
     */
    private boolean serverDisconnected = false;

    /**
     * Constructor for the TUI class.
     */
    public TUI() {
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintStream(System.out, true);
        this.controller = new ViewController(this);
        this.state = new ChooseConnectionState(this);
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        clearConsole();
        // ChooseConnectionState
        state.run();
    }

    @Override
    public void handleResponse(String eventID, Feedback feedback, String message) {
        executor.execute(() -> state.handleResponse(feedback, message, eventID));
    }

    /**
     * Method to clear the console.
     */
    public void clearConsole() {
        try {
            if( System.getProperty("os.name").contains("Windows") )
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                out.println("\033[H\033[2J");
        } catch (Exception ignored) {}
    }

    @Override
    public void serverDisconnected() {
        if (isWaitingForInput()) {
            serverDisconnected = true;
            stopInputRead(true);
        } else {
            state.notifyResponse();
            state.showResponseMessage("Disconnected from server. Please try connecting again.", 2000);
            this.resetUI();
        }
    }

    @Override
    public void printMessage(String message){
        out.println(message);
    }

    @Override
    public void print(String message) {
        out.print(message);
    }

    @Override
    public String getInput() {
        String input = "-1";
        stopInputRead = false;
        waitingForInput = true;
        try {
            while (!in.ready() && !stopInputRead) {
                Thread.sleep(200);
            }
            if (!stopInputRead)
                while(input.isEmpty() || input.equals("-1"))
                    input = in.readLine();
        } catch (InterruptedException e) {
            waitingForInput = false;
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (stopInputRead) {
            waitingForInput = false;
            if (serverDisconnected) {
                clearConsole();
                state.showResponseMessage("Disconnected from server. Please try connecting again.", 2000);
                this.resetUI();
            }
            return "$stop";
        }
        waitingForInput = false;
        return input;
    }

    @Override
    public String getIntFromInput() {
        waitingForInput = true;
        stopInputRead = false;
        String input = "";
        try {
            while (!in.ready() && !stopInputRead) {
                Thread.sleep(100);
            }
            if (!stopInputRead)
                while(input.isEmpty())
                    input = in.readLine();
        } catch (InterruptedException e) {
            waitingForInput = false;
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (stopInputRead) {
            waitingForInput = false;
            if (serverDisconnected) {
                clearConsole();
                state.showResponseMessage("Disconnected from server. Please try connecting again.", 2000);
                this.resetUI();
            }
            return "$stop";
        }
        waitingForInput = false;
        return input;
    }

    @Override
    public void setFXMLController(FXMLController controller){}

    @Override
    public boolean inGame() {
        return state.inGame();
    }

    @Override
    public boolean inLobby() {
        return state.inLobby();
    }

    @Override
    public boolean inMenu() {
        return state.inMenu();
    }

    @Override
    public boolean inChat() {
        return state.inChat();
    }

    @Override
    public ViewController getController() {
        return controller;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public ViewState getState() {
        return state.getState();
    }

    @Override
    public void setState(ViewState state) {
        this.state = state;
    }

    @Override
    public void stopInputRead(boolean stopInputRead) {
        this.stopInputRead = stopInputRead;
    }

    /**
     * Method to check if the TUI is waiting for input.
     * @return true if the TUI is waiting for input, false otherwise.
     */
    public boolean isWaitingForInput() {
        return waitingForInput;
    }

    @Override
    public void clearInput() {
        try {
            in.mark(100);
            in.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetUI() {
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintStream(System.out, true);
        this.controller = new ViewController(this);
        this.state = new ChooseConnectionState(this);
        this.executor = Executors.newCachedThreadPool();
        run();
    }

    @Override
    public boolean isInputReadStopped() {
        return stopInputRead;
    }
}