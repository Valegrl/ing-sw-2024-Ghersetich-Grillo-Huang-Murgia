package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.tui.TUI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Class that represents the state where the user chooses the connection type with the server.
 */
public class ChooseConnectionState extends ViewState {
    /**
     * Constructor for the ChooseConnectionState.
     * @param view The TUI instance that this state belongs to.
     */
    public ChooseConnectionState(TUI view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        while(!chooseConnection());
        showResponseMessage("Connection with server established", 1000);
        this.transition(new LoginState(view));
    }

    @Override
    public boolean handleInput(int input) {
        String ip = "127.0.0.1";
        boolean validIp = false;

        view.printMessage("Please provide the IP address or the URL of the server:");
        while (!validIp) {
            ip = view.getInput();
            if (ClientManager.validateAddress(ip)) {
                validIp = true;
            } else {
                view.printMessage("Invalid IP address. Please provide a valid one:");
            }
        }

        switch (input) {
            case 1:
                view.printMessage("Connecting with socket...");
                try {
                    ClientManager.getInstance().initSocket(ip, 1098);
                } catch (RemoteException e) {
                    view.printMessage("Cannot connect with socket. Make sure the IP provided is valid and try again later...");
                    return false;
                } catch (IOException e) {
                    showResponseMessage("Cannot connect with socket. Make sure the IP provided is valid and try again later...", 1000);
                    return false;
                }
                break;
            case 2:
                view.printMessage("Connecting with RMI...");
                try {
                    ClientManager.getInstance().initRMI(ip);
                } catch (RemoteException e) {
                    view.printMessage("Cannot connect with RMI. Make sure the IP provided is valid and try again later...");
                    return false;
                }
                break;
        }
        return true;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        // Not used
    }

    /**
     * Method that asks the user to choose the connection type.
     * @return true if the connection is established, false otherwise.
     */
    private boolean chooseConnection(){
        view.printMessage("Choose the connection type:");
        int choice = readChoiceFromInput(Arrays.asList("SOCKET", "RMI"));

        return handleInput(choice);
    }

    @Override
    public boolean inMenu() {
        return true;
    }
}
