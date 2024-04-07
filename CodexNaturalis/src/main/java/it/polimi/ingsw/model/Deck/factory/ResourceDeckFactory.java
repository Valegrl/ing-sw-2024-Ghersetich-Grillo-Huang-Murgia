package it.polimi.ingsw.model.Deck.factory;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.Card.ResourceCard;
import it.polimi.ingsw.model.Deck.PlayingDeck;

import java.util.Collections;
import java.util.List;

public class ResourceDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/target/classes/ResourceDeck.json";

    @Override
    public PlayingDeck<ResourceCard> createDeck() {
        List<ResourceCard> cards = readCardsFromJson(JSON_FILE_NAME, new TypeToken<List<ResourceCard>>() {}.getType());
        Collections.shuffle(cards);

        ResourceCard[] visibleCards = new ResourceCard[2];
        visibleCards[0] = cards.removeFirst();
        visibleCards[1] = cards.removeFirst();

        return new PlayingDeck<ResourceCard>(cards, visibleCards);
    }
}
