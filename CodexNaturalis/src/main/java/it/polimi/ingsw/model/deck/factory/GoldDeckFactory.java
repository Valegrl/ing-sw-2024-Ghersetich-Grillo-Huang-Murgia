package it.polimi.ingsw.model.deck.factory;

import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.deck.PlayingDeck;

import java.util.Collections;
import java.util.List;

public class GoldDeckFactory extends DeckFactory {

    private static final String JSON_FILE_NAME = "CodexNaturalis/src/main/resources/GoldDeck.json";

    @Override
    public PlayingDeck<GoldCard> createDeck() {
        List<GoldCard> cards = readCardsFromJson(JSON_FILE_NAME, new TypeToken<List<GoldCard>>() {
        }.getType());
        Collections.shuffle(cards);

        GoldCard[] visibleCards = new GoldCard[2];
        visibleCards[0] = cards.removeFirst();
        visibleCards[1] = cards.removeFirst();

        return new PlayingDeck<GoldCard>(cards, visibleCards);
    }
}