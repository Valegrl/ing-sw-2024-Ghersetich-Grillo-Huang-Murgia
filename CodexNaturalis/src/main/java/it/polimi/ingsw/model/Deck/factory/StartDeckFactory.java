package it.polimi.ingsw.model.Deck.factory;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.Card.StartCard;
import it.polimi.ingsw.model.Deck.Deck;

import java.util.Collections;
import java.util.List;

public class StartDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/target/classes/StartDeck.json";

    @Override
    public Deck<StartCard> createDeck() {
        List<StartCard> cards = readCardsFromJson(JSON_FILE_NAME, new TypeToken<List<StartCard>>() {}.getType());
        Collections.shuffle(cards);
        return new Deck<StartCard>(cards);
    }
}
