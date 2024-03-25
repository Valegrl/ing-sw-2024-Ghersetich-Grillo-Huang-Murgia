package it.polimi.ingsw.model.Card;

/**
 * A class to represent all possible cards in the game.
 */
public abstract class Card {
	/**
	 * A unique identifier (an integer) that distinguishes each and every card in the game.
	 */
	private final int id;

	/**
	 * Constructs a new Card with the given identifier.
	 * @param id A unique integer associated with each card.
	 */
	public Card(int id) {
		this.id = id;
	}

	/**
	 * Retrieves the identifier of a card.
	 * @return the card id.
	 */
	public int getId() {
		return id;
	}

}
