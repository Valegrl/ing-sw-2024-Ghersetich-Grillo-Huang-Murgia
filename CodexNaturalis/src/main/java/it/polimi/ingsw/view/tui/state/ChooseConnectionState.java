package it.polimi.ingsw.view.tui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.RegisterEvent;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.tui.TUI;

import java.rmi.RemoteException;
import java.util.Arrays;

public class ChooseConnectionState extends ViewState {
    public ChooseConnectionState(TUI view) {
        super(view);
    }

    @Override
    public void run() {
        clearConsole();
        view.stopInputRead(false);
        while(!chooseConnection());
        showResponseMessage("Connection with server established", 1000);
        this.transition(new LoginState(view));
    }

    @Override
    public boolean handleInput(int input) {
        String ip;
        view.printMessage("Please provide the IP address or the URL of the server:");
        ip = "127.0.0.1"; // FIXME for debug purposes forcing 127.0.0.1
        // ip = view.getInput();
        switch (input) {
            case 1:
                view.printMessage("Connecting with socket...");
                try {
                    ClientManager.getInstance().initSocket(ip, 1098);
                } catch (RemoteException e) {
                    view.printMessage("Cannot connect with socket. Make sure the IP provided is valid and try again later...");
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
