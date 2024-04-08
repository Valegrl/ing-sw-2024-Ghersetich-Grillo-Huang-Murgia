package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.evaluator.Evaluator;

/**
 * A class to represent a specific type of card, the Resource card.
 */
public class ResourceCard extends PlayableCard {

    /**
     * Constructs a new ResourceCard.
     *
     * @param id                A unique String associated with each card.
     * @param evaluator         The card's specific {@link Evaluator evaluator}.
     * @param points            The number of points associated with each card.
     * @param permanentResource The fixed resource of a Playable card, valid through the entire game.
     * @param corners           The items contained on each corner of the card.
     */
    public ResourceCard(String id, Evaluator evaluator, int points, Item permanentResource, Item[] corners) {
        super(id, evaluator, points, permanentResource, corners, CardType.RESOURCE);
    }
}
