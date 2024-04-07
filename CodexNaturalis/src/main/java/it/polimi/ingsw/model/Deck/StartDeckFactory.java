package it.polimi.ingsw.model.Deck;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.Card.Card;
import it.polimi.ingsw.model.Card.StartCard;

import java.util.Collections;
import java.util.List;

public class StartDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/target/classes/StartDeck.json";

    @Override
    public Deck<? extends Card> createDeck() {
        List<StartCard> cards = readCardsFromJson(JSON_FILE_NAME, new TypeToken<List<StartCard>>() {}.getType());
        Collections.shuffle(cards);
        return new Deck<StartCard>(cards);
    }
}
