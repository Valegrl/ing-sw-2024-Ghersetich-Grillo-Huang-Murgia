package it.polimi.ingsw.utils;

import java.util.List;
import java.util.Objects;

/**
 * A class to represent a coordinate in a two-dimensional space.
 */
public class Coordinate {
    /**
     * The x-coordinate.
     */
    private final int x;

    /**
     * The y-coordinate.
     */
    private final int y;

    /**
     * Constructs a new Coordinate with the given x and y coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the sum of two Coordinates by creating a new Coordinate object.
     *
     * @param c The coordinate to sum.
     * @return The new Coordinate.
     */
    public Coordinate sum(Coordinate c){
        return new Coordinate(this.x + c.x, this.y + c.y);
    }

    /**
     * Retrieves the x-coordinate of this Coordinate.
     *
     * @return the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of this Coordinate.
     *
     * @return the y-coordinate.
     */
    public int getY() {
        return y;
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
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Sorts a list of {@code Coordinate} objects by their y-coordinates in ascending order.
     * If y-coordinates are equal, x-coordinates are compared and sorted in ascending order.
     *
     * @param coordinates the list of {@code Coordinate} objects to be sorted
     * @return the sorted list of {@code Coordinate} objects
     */
    public static List<Coordinate> sortCoordinates(List<Coordinate> coordinates) {
        coordinates.sort((c1, c2) -> {
            int yComparison = Integer.compare(c1.getY(), c2.getY());
            if (yComparison != 0) {
                return yComparison;
            } else {
                return Integer.compare(c1.getX(), c2.getX());
            }
        });
        return coordinates;
    }
}
