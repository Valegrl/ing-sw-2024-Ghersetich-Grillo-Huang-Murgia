package it.polimi.ingsw.model.exceptions;


/**
 * Exception thrown when a player's hand is illegal in the starting phase.
 * I.E. This happens if in the starting phase of a game the player's hand doesn't have 1 gold card and 2 resource cards.
 */
public class IllegalFirstHandException extends RuntimeException {

    /**
     * Constructs a new IllegalHandException with a fixed detail message.
     */
    public IllegalFirstHandException(){
        super("Your starting hand is illegal");
    }
}
