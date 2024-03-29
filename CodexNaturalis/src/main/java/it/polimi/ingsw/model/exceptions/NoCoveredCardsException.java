package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the player tries to place a card, but the card's coordinate isn't diagonal to any card in the
 * {@link it.polimi.ingsw.model.Player.PlayArea}.
 */
public class NoCoveredCardsException extends Exception{
    /**
     * Constructs a new NoCoveredCardsException with a fixed detail message.
     */
    public NoCoveredCardsException(){
        super ("The card you placed didn't cover any other cards");
    }
}
