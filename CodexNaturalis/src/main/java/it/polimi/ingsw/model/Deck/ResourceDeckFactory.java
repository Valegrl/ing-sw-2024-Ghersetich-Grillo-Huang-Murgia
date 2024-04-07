package it.polimi.ingsw.model.Deck;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.Card.Card;
import it.polimi.ingsw.model.Card.ResourceCard;

import java.util.Collections;
import java.util.List;

public class ResourceDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/target/classes/ResourceDeck.json";

    @Override
    public PlayingDeck<? extends Card> createDeck() {
        List<ResourceCard> cards = readCardsFromJson(JSON_FILE_NAME, new TypeToken<List<ResourceCard>>() {}.getType());
        Collections.shuffle(cards);

        ResourceCard[] visibleCards = new ResourceCard[2];
        visibleCards[0] = cards.removeFirst();
        visibleCards[1] = cards.removeFirst();

        return new PlayingDeck<ResourceCard>(cards, visibleCards);
    }
}
