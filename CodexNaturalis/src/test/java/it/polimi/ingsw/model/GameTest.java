package it.polimi.ingsw.model;

import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.utils.PlayerCardsSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private final String username1 = "player1";
    private final String username2 = "player2";
    private final String externalPlayer = "other";

    @BeforeEach
    void setUp() {
        JsonConfig.loadConfig();
    }

    @Test
    void matchSimulation() {
        Map<String, GameListener> players = new HashMap<>() {{
            put(username1, (event -> System.out.println("notify " + username1 + " with " + event)));
            put(username2, (event -> System.out.println("notify " + username2 + " with " + event)));
        }};
        game = new Game("testGame", players);
        List<PlayerCardsSetup> cardsSetupData = game.gameSetup();
        assertEquals(game.getGameStatus(), GameStatus.SETUP);

        assertFalse(game.setupPlayerCards(externalPlayer, cardsSetupData.getFirst().getObjectiveCards()[1], false));
        game.setupPlayerCards(username1, cardsSetupData.getFirst().getObjectiveCards()[1], false);
        game.setupPlayerCards(username2, cardsSetupData.get(1).getObjectiveCards()[0], true);

        assertFalse(game.setupPlayerToken(externalPlayer, Token.BLUE));
        game.setupPlayerToken(username1, Token.BLUE);
        game.setupPlayerToken(username2, Token.GREEN);

        game.startTurn();
        assertEquals(game.getGameStatus(), GameStatus.RUNNING);

        game.offlinePlayer(username1);
        assertEquals(game.getGameStatus(), GameStatus.WAITING);

        assertThrows(PlayerNotFoundException.class, () -> game.reconnectPlayer(externalPlayer,
                (event -> System.out.println("notify "+ username1 +" with " + event))));
        game.reconnectPlayer(username1, (event -> System.out.println("notify "+ username1 +" with " + event)));
        assertEquals(game.getGameStatus(), GameStatus.RUNNING);

        boolean exceptionThrown = true;
        int i = 1;
        Coordinate pos = new Coordinate(1,1);

        while (exceptionThrown && i <= 40) {
            String playableCardID = String.format("RC%02d", i);

            try {
                game.placeCard(playableCardID, pos, false);
                exceptionThrown = false;
            } catch (Exception e) {
                i++;
            }
        }
        assert i<=40;

        assertDoesNotThrow(() -> game.drawCard(CardType.GOLD, 0));

        game.newTurn(true);
        assertEquals(game.getGameStatus(), GameStatus.RUNNING);

        exceptionThrown = true;
        i = 1;
        pos = new Coordinate(1,1);

        while (exceptionThrown && i <= 40) {
            String playableCardID = String.format("GC%02d", i);

            try {
                game.placeCard(playableCardID, pos, false);
            } catch (Exception e) {
                if ("You do not have a card with the provided ID.".equals(e.getMessage())) {
                    i++;
                } else if ("The selected card does not satisfy its constraint.".equals(e.getMessage())) {
                    exceptionThrown = false;
                }
            }
        }
        assert i<=40;

        exceptionThrown = true;
        i = 1;
        pos = new Coordinate(1,1);

        while (exceptionThrown && i <= 40) {
            String playableCardID = String.format("RC%02d", i);

            try {
                game.placeCard(playableCardID, pos, true);
                exceptionThrown = false;
            } catch (Exception e) {
                i++;
            }
        }
        assert i<=40;

        assertDoesNotThrow(() -> game.drawCard(CardType.RESOURCE, 0));

        game.newTurn(true);
        assertEquals(game.getGameStatus(), GameStatus.RUNNING);

        game.offlinePlayer(username1);
        assertEquals(game.getGameStatus(), GameStatus.WAITING);

        game.endGame();
        assertEquals(game.getGameStatus(), GameStatus.ENDED);

    }
}