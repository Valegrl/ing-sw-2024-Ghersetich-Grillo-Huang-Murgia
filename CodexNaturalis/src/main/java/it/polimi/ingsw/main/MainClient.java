package it.polimi.ingsw.main;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

/**
 * Class used to start the client.
 */
public class MainClient {

    /**
     * The {@link View} used to display the game.
     */
    private static View view;

    /**
     * Creates a new {@link MainClient} with the given {@link View}.
     *
     * @param view The {@link View} used to display the game.
     */
    public MainClient(View view) {
        MainClient.view = view;
    }

    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname","127.0.0.1"); // TODO config?
        MainClient.view = (( args.length>0 ) && args[0].equals("-cli")) ? new TUI() : new GUI();
        view.run();
    }
}
