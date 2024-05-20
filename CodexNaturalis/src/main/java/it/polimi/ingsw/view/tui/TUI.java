package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.main.MainClient;
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

public class TUI implements View {

    private final BufferedReader in;

    private boolean waitingForInput;

    private boolean stopInputRead = false;

    private boolean serverDisconnected = false;

    private final PrintStream out;

    private final ExecutorService executor;

    private String username;

    private final ViewController controller;

    private ViewState state;

    public TUI() {
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintStream(System.out, true);
        this.controller = new ViewController(this);
        this.state = new ChooseConnectionState(this);
        this.executor = Executors.newCachedThreadPool();
    }

    public void run() {
        clearConsole();
        // ChooseConnectionState
        state.run();
    }

    @Override
    public void handleResponse(String eventID, Feedback feedback, String message) {
        executor.execute(() -> state.handleResponse(feedback, message, eventID));
    }

    public void clearConsole() {
        try {
            if( System.getProperty("os.name").contains("Windows") )
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                out.println("\033[H\033[2J");
        } catch (Exception ignored) {}
    }

    public void serverDisconnected() {
        if (isWaitingForInput()) {
            serverDisconnected = true;
            stopInputRead(true);
        } else {
            state.notifyResponse();
            state.showResponseMessage("Disconnected from server. Please try connecting again.", 2000);
            MainClient.restartTUI();
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
        if (stopInputRead && serverDisconnected) {
            clearConsole();
            state.showResponseMessage("Disconnected from server. Please try connecting again.", 2000);
            MainClient.restartTUI();
            return "$stop";
        }
        waitingForInput = false;
        return input;
    }

    public void setFXMLController(FXMLController controller){
    }

    public ViewController getController() {
        return controller;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public ViewState getState() {
        return state;
    }

    @Override
    public void setState(ViewState state) {
        this.state = state;
    }

    public void stopInputRead(boolean stopInputRead) {
        this.stopInputRead = stopInputRead;
    }

    public boolean isWaitingForInput() {
        return waitingForInput;
    }

    public void clearInput() {
        try {
            while(in.ready())
                in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}