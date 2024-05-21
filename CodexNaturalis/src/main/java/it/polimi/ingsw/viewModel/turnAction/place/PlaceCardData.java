package it.polimi.ingsw.viewModel.turnAction.place;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The PlaceCardData class represents the data associated with the event of a player placing a card.
 * It includes the game status, a flag indicating if the last circle was detected, and the scoreboard of the game.
 * This class is abstract and should be extended.
 */
public abstract class PlaceCardData implements Serializable {

    /**
     * The status of the game.
     */
    protected final GameStatus gameStatus;

    /**
     * Flag indicating if the last circle was detected.
     */
    protected final boolean detectedLastCircle;

    /**
     * The scoreboard of the game.
     */
    protected final Map<String, Integer> scoreboard;

    /**
     * Constructs a new PlaceCardData with the given game model.
     * It initializes the game status, detected last circle flag, and the scoreboard of the game.
     *
     * @param model The game model.
     */
    public PlaceCardData(Game model) {
        this.gameStatus = model.getGameStatus();
        this.detectedLastCircle = model.isDetectedLC();
        this.scoreboard = model.getScoreboard().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> entry.getKey().getUsername(),
                        Map.Entry::getValue
                ));
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
     * @return The scoreboard of the game.
     */
    public Map<String, Integer> getScoreboard() {
        return scoreboard;
    }
}
