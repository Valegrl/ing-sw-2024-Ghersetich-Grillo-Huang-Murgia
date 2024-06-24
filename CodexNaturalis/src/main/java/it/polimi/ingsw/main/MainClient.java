package it.polimi.ingsw.main;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;
import org.fusesource.jansi.AnsiConsole;

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
        if(args.length < 1) {
            System.out.println("Usage: java -jar JARNAME.jar -<own_ip> [-cli]");
            System.exit(1);
        }
        System.setProperty("java.rmi.server.hostname", args[0].substring(1));
        AnsiConsole.systemInstall();
        MainClient.view = (args.length > 1 && args[1].equals("-cli")) ? new TUI() : new GUI();
        view.run();
    }
}
