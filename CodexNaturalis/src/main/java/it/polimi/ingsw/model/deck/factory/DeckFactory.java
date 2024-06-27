package it.polimi.ingsw.model.deck.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.evaluator.Evaluator;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class is responsible for creating decks of cards for the game.
 * It uses Gson to read JSON files that contain the card data, and then creates a deck from that data.
 * The class supports different types of cards, each with its own JSON file.
 */
public class DeckFactory {

    /**
     * A map that associates each card class with the name of its JSON file.
     */
    Map<Class<?>, String> classToFile = new HashMap<>() {{
        put(StartCard.class, "it/polimi/ingsw/json/StartDeck.json");
        put(ObjectiveCard.class, "it/polimi/ingsw/json/ObjectiveDeck.json");
        put(GoldCard.class, "it/polimi/ingsw/json/GoldDeck.json");
        put(ResourceCard.class, "it/polimi/ingsw/json/ResourceDeck.json");
    }};

    /**
     * Creates a deck of cards of the specified type.
     *
     * @param cls The class of the cards to create a deck of.
     * @return A deck of cards of the specified type.
     * @throws IllegalArgumentException If the specified class is not supported or the resource for the class is not found.
     */
    public <T extends Card> Deck<T> createDeck(Class<T> cls) {
        InputStream resource;

        String JSON_FILE_NAME = classToFile.get(cls);
        if (JSON_FILE_NAME == null) {
            throw new IllegalArgumentException("Class " + cls + " not supported");
        }

        resource = getClass().getClassLoader().getResourceAsStream(JSON_FILE_NAME);
        if (resource == null) {
            throw new IllegalArgumentException("Resource for " + cls + " not found");
        }

        List<T> cards = readCardsFromJson(resource, TypeToken.getParameterized(List.class, cls).getType());
        assert cards != null;
        Collections.shuffle(cards);

        return new Deck<>(cards);
    }

    /**
     * Reads a list of cards from a JSON resource.
     *
     * @param resource The JSON resource to read from.
     * @param cardListType The type of the list of cards to read.
     * @return A list of cards read from the JSON resource.
     */
    private <T extends Card> List<T> readCardsFromJson(InputStream resource, Type cardListType) {
        try (Reader reader = new InputStreamReader(resource)) {
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
