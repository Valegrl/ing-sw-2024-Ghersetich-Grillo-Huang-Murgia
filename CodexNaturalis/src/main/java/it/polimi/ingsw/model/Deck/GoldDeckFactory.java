package it.polimi.ingsw.model.Deck;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.Card.Card;
import it.polimi.ingsw.model.Card.GoldCard;

import java.util.Collections;
import java.util.List;

public class GoldDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/target/classes/GoldDeck.json";

    @Override
    public PlayingDeck<? extends Card> createDeck() {
        List<GoldCard> cards = readCardsFromJson(JSON_FILE_NAME, new TypeToken<List<GoldCard>>() {
        }.getType());
        Collections.shuffle(cards);

        GoldCard[] visibleCards = new GoldCard[2];
        visibleCards[0] = cards.removeFirst();
        visibleCards[1] = cards.removeFirst();

        return new PlayingDeck<GoldCard>(cards, visibleCards);
    }
}