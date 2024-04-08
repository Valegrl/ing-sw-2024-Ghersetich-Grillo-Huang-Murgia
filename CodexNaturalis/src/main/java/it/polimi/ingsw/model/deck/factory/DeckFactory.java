package it.polimi.ingsw.model.deck.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.evaluator.Evaluator;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public abstract class DeckFactory {

    public abstract Deck<? extends Card> createDeck();

    protected <T extends Card> List<T> readCardsFromJson(String jsonFileName, Type cardListType) {
        try (FileReader reader = new FileReader(jsonFileName)) {
            Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Evaluator.class, new EvaluatorDeserializer())
                            .create();
            return gson.fromJson(reader, cardListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
