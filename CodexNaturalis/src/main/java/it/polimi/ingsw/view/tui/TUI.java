package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.tui.state.ChooseConnectionState;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TUI implements View {
    private final Scanner in;

    private final PrintStream out;

    private final ExecutorService executor;

    private String username;

    private final ViewController controller;

    private ViewState state;

    public TUI() {
        this.in = new Scanner(System.in);
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

    public void serverCrashed() {
        clearConsole();
        out.println("Server crashed. Please try connecting again.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        state = new ChooseConnectionState(this);
        run();
    }

    //TODO implement methods, possible changes to the names

    @Override
    public void displayAvailableLobbies(Feedback feedback, String message, List<LobbyState> availableLobbies) {
        printMessage(message);

        if(feedback.equals(Feedback.SUCCESS)){
            if(!availableLobbies.isEmpty()){
                for(LobbyState lobby : availableLobbies){
                    String lobbyID = lobby.getId();
                    int onlinePlayers = lobby.getOnlinePlayers();
                    int requiredPlayers = lobby.getRequiredPlayers();
                    out.println(lobbyID + ": [" + onlinePlayers + "/" + requiredPlayers + "].") ;
                }
            }
            else{
                out.println("Please try again later...");
            }
        }
    }

    @Override
    public void notifyDeleteAccount(Feedback feedback, String message){
        printMessage(message);

        //TODO logic with feedback if account is deleted ?
    }

    @Override
    public void displayOfflineGames(Feedback feedback, String message, List<LobbyState> offlineGames){
        printMessage(message);

        if(feedback.equals(Feedback.SUCCESS)){
            if(!offlineGames.isEmpty()){
                for(LobbyState lobby : offlineGames){
                    String lobbyID = lobby.getId();
                    int playersInGame = lobby.getOnlinePlayers();
                    int requiredPlayers = lobby.getRequiredPlayers();
                    out.println(lobbyID + ": [" + playersInGame + "/" + requiredPlayers + "].");
                }
            }
        }
    }

    @Override
    public void displayJoinedLobby(Feedback feedback, String message, String id, List<Pair<String, Boolean>> playersReadyStatus){
        printMessage(message);

        if(feedback.equals(Feedback.SUCCESS)){
            out.println("Current players and status:");
            for(Pair<String, Boolean> player : playersReadyStatus){
                String username = player.key();
                Boolean ready = player.value();
                if(ready) {
                    out.println(username + " is ready.");
                }
                else{
                    out.println(username + " is not ready.");
                }
            }
        }
    }

    @Override
    public void notifyLogout(Feedback feedback, String message){
        printMessage(message);

        //TODO logic with feedback if player log out ?
    }

    @Override
    public void notifyReconnectToGame(Feedback feedback, String message){
        printMessage(message);

        //TODO logic with feedback if player reconnect to game ?
    }

    @Override
    public void notifyKickFromLobby(Feedback feedback, String message, String kickedPlayer){
        printMessage(message);

        if(feedback.equals(Feedback.SUCCESS)){
            out.println(kickedPlayer + " has been kicked from the lobby.");
        }
    }

    @Override
    public void printMessage(String message){
        out.println(message);
    }

    @Override
    public String getInput() {
        return in.nextLine();
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
}