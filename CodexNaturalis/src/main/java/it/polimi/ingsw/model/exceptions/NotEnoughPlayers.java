package it.polimi.ingsw.model.exceptions;

public class NotEnoughPlayers extends Exception {
    public NotEnoughPlayers() {
        super("Not enough players to start the game");
    }
}
