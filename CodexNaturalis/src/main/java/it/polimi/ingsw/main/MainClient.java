package it.polimi.ingsw.main;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

/*start client*/
public class MainClient {

    private static View view;

    public MainClient(View view) {
        MainClient.view = view;
    }

    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname","127.0.0.1"); // TODO config?
        MainClient.view = (( args.length>0 ) && args[0].equals("-cli")) ? new TUI() : new GUI();
        view.run();
    }

    public static void restartTUI() {
        view = new TUI();
        view.run();
    }
}
