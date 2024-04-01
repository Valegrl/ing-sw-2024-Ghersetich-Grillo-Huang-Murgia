package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

/**
 * A class to represent a specific type of card, the Resource card.
 */
public class ResourceCard extends PlayableCard {
    /**
     * Constructs a new Lobby with the given first player's username and the number of required players.
     * @param id The first player's username.
     * @param evaluator The first player's username.
     * @param points A unique integer associated with each card.
     * @param permanentResource The first player's username.
     * @param corners The number of required players to start this Lobby's game.
     * @param cardType The first player's username.
     */
    public ResourceCard(int id, Evaluator evaluator, int points, Item permanentResource, Item[] corners, CardType cardType) {
        super(id, evaluator, points, permanentResource, corners, cardType);
    }
}
