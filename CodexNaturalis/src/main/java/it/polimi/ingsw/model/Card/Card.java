package it.polimi.ingsw.model.Card;

/**
 * A class to represent all possible cards in the game.
 */
public abstract class Card {
	/**
	 * A String-format unique identifier that distinguishes each and every card in the game.
	 */
	private final String id;

	/**
	 * Constructs a new Card with the given identifier.
	 *
	 * @param id A unique String associated with each card.
	 */
	public Card(String id) {
		this.id = id;
	}

	/**
	 * Retrieves the identifier of a card.
	 * @return {@link Card#id}.
	 */
	public String getId() {
		return id;
	}
}

