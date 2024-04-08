package it.polimi.ingsw.model.deck.factory;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.deck.Deck;

import java.util.Collections;
import java.util.List;

public class ObjectiveDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/src/main/resources/ObjectiveDeck.json";

    @Override
    public Deck<ObjectiveCard> createDeck() {
        List<ObjectiveCard> cards = readCardsFromJson(JSON_FILE_NAME, new TypeToken<List<ObjectiveCard>>() {}.getType());
        Collections.shuffle(cards);
        return new Deck<ObjectiveCard>(cards);
    }
}
