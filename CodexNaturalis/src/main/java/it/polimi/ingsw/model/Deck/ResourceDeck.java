package it.polimi.ingsw.model.Deck;

import java.util.List;

import it.polimi.ingsw.model.Card.PlayableCard;
import it.polimi.ingsw.model.Card.ResourceCard;
import it.polimi.ingsw.model.Card.Card;

public class ResourceDeck implements Deck {

    private List<ResourceCard> deck;

    private ResourceCard[] visibleCards;


    /**
     * @see it.polimi.ingsw.model.Deck.Deck#newVisibleCard()
     */
    public void newVisibleCard() {

    }


    /**
     * @see it.polimi.ingsw.model.Deck.Deck#drawVisible(int)
     */
    public ResourceCard drawVisible(int chosenCard) {
        return null;
    }


    /**
     * @see it.polimi.ingsw.model.Deck.Deck#drawTop()
     */
    public ResourceCard drawTop() {
        return null;
    }

}
