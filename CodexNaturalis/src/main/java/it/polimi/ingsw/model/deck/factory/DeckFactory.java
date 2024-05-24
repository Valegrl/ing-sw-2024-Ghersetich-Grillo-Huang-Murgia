package it.polimi.ingsw.model.deck.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.evaluator.Evaluator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class DeckFactory {

    Map<Class<?>, String> classToFile = new HashMap<>() {{
        put(StartCard.class, "it/polimi/ingsw/json/StartDeck.json");
        put(ObjectiveCard.class, "it/polimi/ingsw/json/ObjectiveDeck.json");
        put(GoldCard.class, "it/polimi/ingsw/json/GoldDeck.json");
        put(ResourceCard.class, "it/polimi/ingsw/json/ResourceDeck.json");
    }};

    public <T extends Card> Deck<T> createDeck(Class<T> cls) {
        URL resource;
        URI jsonURI;

        String JSON_FILE_NAME = classToFile.get(cls);
        if (JSON_FILE_NAME == null) {
            throw new IllegalArgumentException("Class " + cls + " not supported");
        }

        resource = getClass().getClassLoader().getResource(JSON_FILE_NAME);
        if (resource == null) {
            throw new IllegalArgumentException("Resource for " + cls + " not found");
        }

        try {
            jsonURI = resource.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<T> cards = readCardsFromJson(jsonURI, TypeToken.getParameterized(List.class, cls).getType());
        assert cards != null;
        Collections.shuffle(cards);

        return new Deck<>(cards);
    }

    private <T extends Card> List<T> readCardsFromJson(URI jsonURI, Type cardListType) {
        File file = new File(jsonURI);
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Evaluator.class, new EvaluatorTypeAdapter())
                    .create();
            return gson.fromJson(reader, cardListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
