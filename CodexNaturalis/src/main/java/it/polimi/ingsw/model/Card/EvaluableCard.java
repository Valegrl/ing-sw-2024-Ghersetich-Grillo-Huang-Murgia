package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

/**
 * A class to represent an Evaluable card, i.e. an Objective or Gold or Resource card.
 */
public abstract class EvaluableCard extends Card {
	/**
	 * The card's specific {@link Evaluator evaluator}.
	 */
	private Evaluator evaluator;

	/**
	 * The amount of points associated with each and every card.
	 */
	private int points;

	/**
	 * Constructs a new Evaluable card with the given identifier, , amount of points.
	 * @param id A unique integer associated with each card.
	 * @param evaluator The card's specific {@link Evaluator evaluator}.
	 * @param points The amount of points associated with each card.
	 */
	public EvaluableCard(int id, Evaluator evaluator, int points) {
		super(id);
		this.evaluator = evaluator;
		this.points = points;
	}

	public Evaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
