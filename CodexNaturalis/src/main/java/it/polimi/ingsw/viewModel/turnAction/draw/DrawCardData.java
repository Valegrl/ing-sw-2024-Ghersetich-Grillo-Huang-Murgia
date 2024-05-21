package it.polimi.ingsw.viewModel.turnAction.draw;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The DrawCardData class represents the data associated with the event of drawing a card.
 * It includes the game status, a flag indicating if the last circle was detected, the top card of the resource deck,
 * the top card of the gold deck, and the visible resource and gold cards. This class is abstract and should be extended.
 */
public abstract class DrawCardData implements Serializable {

    /**
     * The status of the game.
     */
    protected final GameStatus gameStatus;

    /**
     * Flag indicating if the last circle was detected.
     */
    protected final boolean detectedLastCircle;

    /**
     * The top card of the resource deck.
     */
    private final Item topResourceDeck;

    /**
     * The top card of the gold deck.
     */
    private final Item topGoldDeck;

    /**
     * The visible resource cards.
     */
    private final ImmPlayableCard[] visibleResourceCards;

    /**
     * The visible gold cards.
     */
    private final ImmPlayableCard[] visibleGoldCards;

    /**
     * Constructs a new DrawCardData with the given game model.
     * It initializes the game status, detected last circle flag, top cards of the decks, and visible cards.
     *
     * @param model The game model.
     */
    public DrawCardData(Game model) {
        this.gameStatus = model.getGameStatus();
        this.detectedLastCircle = model.isDetectedLC();
        ResourceCard otherRC = model.seeResourceTopCard();
        this.topResourceDeck = otherRC == null ? null : otherRC.getPermanentResource();
        GoldCard otherGC = model.seeGoldTopCard();
        this.topGoldDeck = otherGC == null ? null : otherGC.getPermanentResource();
        this.visibleResourceCards = Arrays.stream(model.getVisibleResourceCards())
                .map(card -> card == null ? null : new ImmPlayableCard(card))
                .toArray(ImmPlayableCard[]::new);
        this.visibleGoldCards = Arrays.stream(model.getVisibleGoldCards())
                .map(card -> card == null ? null : new ImmPlayableCard(card))
                .toArray(ImmPlayableCard[]::new);
    }

    /**
     * @return The game status.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * @return True if the last circle was detected, false otherwise.
     */
    public boolean isDetectedLastCircle() {
        return detectedLastCircle;
    }

    /**
     * @return The top card of the resource deck.
     */
    public Item getTopResourceDeck() {
        return topResourceDeck;
    }

    /**
     * @return The top card of the gold deck.
     */
    public Item getTopGoldDeck() {
        return topGoldDeck;
    }

    /**
     * @return The visible resource cards.
     */
    public ImmPlayableCard[] getVisibleResourceCards() {
        return visibleResourceCards;
    }

    /**
     * @return The visible gold cards.
     */
    public ImmPlayableCard[] getVisibleGoldCards() {
        return visibleGoldCards;
    }
}
