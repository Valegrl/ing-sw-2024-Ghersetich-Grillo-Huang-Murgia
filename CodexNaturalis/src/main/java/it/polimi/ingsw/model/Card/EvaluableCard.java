package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

/**
 * A class to represent an Evaluable card, i.e. an Objective or Gold or Resource card.
 */
public abstract class EvaluableCard extends Card {
	/**
	 * The card's specific {@link Evaluator evaluator}.
	 */
	private final Evaluator evaluator;

	/**
	 * The amount of points associated with each and every card.
	 */
	private int points;

	/**
	 * Constructs a new Evaluable card.
	 * @param id A unique integer associated with each card.
	 * @param evaluator The card's specific {@link Evaluator evaluator}.
	 * @param points The amount of points associated with each card.
	 */
	public EvaluableCard(int id, Evaluator evaluator, int points) {
		super(id);
		this.evaluator = evaluator;
		this.points = points;
	}

	/**
	 * Retrieves the evaluator belonging to the specific type of card.
	 * @return The card's specific {@link Evaluator evaluator}.
	 */
	public Evaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * Retrieves the points associated with an Evaluable card.
	 * @return The amount of points associated with each and every Evaluable card.
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Sets the amount of point of an Evaluable card.
	 * @param points The amount of points associated with each Evaluable card.
	 */
	public void setPoints(int points) {
		this.points = points;
	}
}

