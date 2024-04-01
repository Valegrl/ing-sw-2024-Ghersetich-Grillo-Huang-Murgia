package it.polimi.ingsw.model.Deck;

import it.polimi.ingsw.model.Card.Card;
import it.polimi.ingsw.model.Card.PlayableCard;

public abstract interface Deck {

    public abstract void newVisibleCard();

    public abstract PlayableCard drawVisible(int chosenCard);

    public abstract PlayableCard drawTop();

}
