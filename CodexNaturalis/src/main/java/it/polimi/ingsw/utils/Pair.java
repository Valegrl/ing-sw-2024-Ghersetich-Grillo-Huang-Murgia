package it.polimi.ingsw.utils;

import java.util.Objects;

/**
 * A class to represent a pair of elements.
 *
 * @param <T> The type of the key.
 * @param <U> The type of the value.
 */
public class Pair<T, U> {
    /**
     * The key element of the Pair.
     */
    private T key;

    /**
     * The value element of the Pair.
     */
    private U value;

    /**
     * Constructs a new Pair object with the given key and value elements.
     *
     * @param key   The key element of the Pair.
     * @param value The value element of the Pair.
     */
    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Retrieves the key element of this Pair.
     *
     * @return The key element.
     */
    public T getKey() {
        return key;
    }

    /**
     * Sets the key element of this Pair.
     *
     * @param key The new key element to set.
     */
    public void setKey(T key) {
        this.key = key;
    }

    /**
     * Retrieves the value element of this Pair.
     *
     * @return The value element.
     */
    public U getValue() {
        return value;
    }

    /**
     * Sets the value element of this Pair.
     *
     * @param value The new value element to set.
     */
    public void setValue(U value) {
        this.value = value;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * This method overrides the default equals implementation inherited from Object class.
     *
     * @param o The reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }
}
