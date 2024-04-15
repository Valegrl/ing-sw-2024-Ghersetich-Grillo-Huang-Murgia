package it.polimi.ingsw.model.deck.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.evaluator.Evaluator;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class DeckFactory {

    Map<Class<?>, String> classToFile = new HashMap<>() {{
        put(StartCard.class, "StartDeck.json");
        put(ObjectiveCard.class, "ObjectiveDeck.json");
        put(GoldCard.class, "GoldDeck.json");
        put(ResourceCard.class, "ResourceDeck.json");
    }};

    public <T extends Card> Deck<T> createDeck(Class<T> cls) {
        String JSON_FILE_NAME = getClass().getClassLoader().getResource(classToFile.get(cls)).getPath();
        if (JSON_FILE_NAME == null) {
            throw new IllegalArgumentException("Class " + cls + " not supported");
        }
        List<T> cards = readCardsFromJson(JSON_FILE_NAME, TypeToken.getParameterized(List.class, cls).getType());
        assert cards != null;
        Collections.shuffle(cards);

        return new Deck<>(cards);
    }

    private <T extends Card> List<T> readCardsFromJson(String jsonFileName, Type cardListType) {
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
