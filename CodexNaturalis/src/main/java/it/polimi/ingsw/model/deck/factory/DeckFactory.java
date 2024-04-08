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
        put(StartCard.class, "CodexNaturalis/src/main/resources/StartDeck.json");
        put(ObjectiveCard.class, "CodexNaturalis/src/main/resources/ObjectiveDeck.json");
        put(GoldCard.class, "CodexNaturalis/src/main/resources/GoldDeck.json");
        put(ResourceCard.class, "CodexNaturalis/src/main/resources/ResourceDeck.json");
    }};

    public <T extends Card> Deck<T> createDeck(Class<T> cls) {
        String JSON_FILE_NAME = classToFile.get(cls);
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
