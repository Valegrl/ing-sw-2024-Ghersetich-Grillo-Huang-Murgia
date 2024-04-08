package it.polimi.ingsw.model.deck.factory;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.deck.PlayingDeck;

import java.util.Collections;
import java.util.List;

public class ResourceDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/src/main/resources/ResourceDeck.json";

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
