package it.polimi.ingsw.utils;

import java.io.Serializable;

/**
 * A Record to represent a pair of elements.
 *
 * @param <T>   The type of the key.
 * @param <U>   The type of the value.
 * @param key   The key element of the Pair.
 * @param value The value element of the Pair.
 */
public record Pair<T, U>(T key, U value) implements Serializable {
    /**
     * Constructs a new Pair Record with the given key and value elements.
     *
     * @param key   The key element of the Pair.
     * @param value The value element of the Pair.
     */
    public Pair {
    }

    /**
     * Retrieves the key element of this Pair.
     *
     * @return The key element.
     */
    @Override
    public T key() {
        return key;
    }

    /**
     * Retrieves the value element of this Pair.
     *
     * @return The value element.
     */
    @Override
    public U value() {
        return value;
    }

}
