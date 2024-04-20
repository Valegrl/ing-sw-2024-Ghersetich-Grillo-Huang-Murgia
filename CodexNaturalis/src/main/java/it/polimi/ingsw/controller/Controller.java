package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a Singleton class that manages the game and lobby controllers.
 * It maintains a list of all active game controllers and lobby controllers.
 *
 * <p>Use {@link #getInstance()} to get the instance of this class.</p>
 */
public class Controller {
    private final static Controller controller = new Controller();

    private final List<GameController> gameControllers;
    private final List<LobbyController> lobbyControllers;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the lists of game and lobby controllers.
     */
    private Controller() {
        gameControllers = new ArrayList<>();
        lobbyControllers = new ArrayList<>();
    }

    /**
     * Returns the single instance of the Controller class.
     *
     * @return the instance of the Controller class.
     */
    public static Controller getInstance(){
        return controller;
    }
}
