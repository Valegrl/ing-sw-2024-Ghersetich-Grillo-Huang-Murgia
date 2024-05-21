package it.polimi.ingsw.viewModel.turnAction.draw;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.viewModel.immutableCard.BackPlayableCard;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The OtherDrawCardData class represents the data associated with the event of another player drawing a card.
 */
public class OtherDrawCardData extends DrawCardData{

    /**
     * The username of the opponent.
     */
    private final String opponent;

    /**
     * The hand of the opponent.
     */
    private final List<BackPlayableCard> hand;

    /**
     * Constructs a new OtherDrawCardData with the given game model and username.
     *
     * @param model The game model.
     * @param username The username of the other player.
     */
    public OtherDrawCardData(Game model, String username) {
        super(model);
        this.opponent = username;
        this.hand = model.getPlayerFromUsername(username).getPlayArea()
                .getHand().stream()
                .map(BackPlayableCard::new)
                .collect(Collectors.toList());
    }

    /**
     * @return The username of the opponent.
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * @return The hand of the opponent.
     */
    public List<BackPlayableCard> getHand() {
        return hand;
    }
}
