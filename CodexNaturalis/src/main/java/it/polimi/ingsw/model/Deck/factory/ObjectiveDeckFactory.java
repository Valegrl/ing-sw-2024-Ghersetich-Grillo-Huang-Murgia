package it.polimi.ingsw.model.Deck.factory;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.Card.ObjectiveCard;
import it.polimi.ingsw.model.Deck.Deck;

import java.util.Collections;
import java.util.List;

public class ObjectiveDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/target/classes/ObjectiveDeck.json";

    @Override
    public Deck<ObjectiveCard> createDeck() {
        List<ObjectiveCard> cards = readCardsFromJson(JSON_FILE_NAME, new TypeToken<List<ObjectiveCard>>() {}.getType());
        Collections.shuffle(cards);
        return new Deck<ObjectiveCard>(cards);
    }
}
