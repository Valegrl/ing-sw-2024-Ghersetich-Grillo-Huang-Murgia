package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.utils.Pair;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

public class TUI implements View {
    private final Scanner in;

    private final PrintStream out;

    private String username;

    private ViewController controller;

    public TUI() {
        this.in = new Scanner(System.in);
        this.out = new PrintStream(System.out, true);
        this.controller = new ViewController(this);
    }

    private int readChoiceFromInput(String opt1, String opt2){
        String inputRead;
        int choice = -1;

        while(true) {
            out.println("1 - " + opt1);
            out.println("2 - " + opt2);
            try {
                inputRead = in.nextLine();
            } catch ( InputMismatchException ex){
                out.println("Invalid selection!");
                continue;
            }

            try {
                choice = Integer.parseInt(inputRead);
            } catch (NumberFormatException e) {
                out.println("Invalid selection!");
                continue;
            }
            if(choice != 1 && choice != 2)
                out.println("Invalid selection!");
            else
                break;
        }
        return choice;
    }

    private boolean chooseConnection(){
        out.println("Choose the connection type:");
        int choice = readChoiceFromInput("SOCKET","RMI");

        String ip;
        out.println("Please provide the IP address or the URL of the server:");
        ip = in.nextLine();

        if(choice == 1) {
            out.println("Connecting with socket...");
            try {
                ClientManager.getInstance().initSocket(ip, 1098);
            } catch (RemoteException e){
                out.println("Cannot connect with socket. Make sure the IP provided is valid and try again later...");
                return false;
            }
        } else {
            out.println("Connecting with RMI...");
            try {
                ClientManager.getInstance().initRMI(ip);
            } catch (RemoteException e) {
                out.println("Cannot connect with RMI. Make sure the IP provided is valid and try again later...");
                return false;
            }
        }
        return true;
    }

    public void run() {
        clearConsole();
        while(!chooseConnection());
        out.println("Connection with server established");
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
        run();
    }

    //TODO implement methods, possible changes to the names
    public void displayAvailableLobbies(List<Pair<String, Pair<Integer, Integer>>> availableLobbies) {}

    public void notifyCreatedLobby(Pair<String, Integer> createdLobby){}

    public void notifyDeleteAccount(Feedback feedback, String message){}
}