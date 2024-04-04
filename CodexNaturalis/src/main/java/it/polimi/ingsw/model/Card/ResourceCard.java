package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

/**
 * A class to represent a specific type of card, the Resource card.
 */
public class ResourceCard extends PlayableCard {
    /**
     * Constructs a new Lobby with the given first player's username and the number of required players.
     * @param id A unique String associated with each card.
     * @param evaluator The card's specific {@link Evaluator evaluator}.
     * @param points The amount of points associated with each card.
     * @param permanentResource The fixed resource of a Playable card, valid through the entire game.
     */
    public ResourceCard(String id, Evaluator evaluator, int points, Item permanentResource) {
        super(id, evaluator, points, permanentResource);
    }
}
