package it.polimi.ingsw.controller;

/*Singleton pattern*/

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final static Controller controller = new Controller();

    private final List<GameController> gameControllers;
    private final List<LobbyController> lobbyControllers;

    private Controller() {
        gameControllers = new ArrayList<>();
        lobbyControllers = new ArrayList<>();
    }

    public static Controller getInstance(){
        return controller;
    }
}
