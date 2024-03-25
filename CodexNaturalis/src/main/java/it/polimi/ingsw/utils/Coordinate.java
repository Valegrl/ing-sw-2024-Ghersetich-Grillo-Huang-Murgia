package it.polimi.ingsw.utils;

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
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Retrieves the x-coordinate of this Coordinate.
	 * @return the x-coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retrieves the y-coordinate of this Coordinate.
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
}
