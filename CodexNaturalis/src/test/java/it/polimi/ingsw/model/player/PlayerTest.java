package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private List<PlayableCard> hand;
    private StartCard startCard;
/*
    @BeforeEach
    void setUp() {
        player = new Player("testPlayer");
        hand = new ArrayList<>();
        startCard = new StartCard();
    }

    @Test
    void getUsernameShouldReturnUsername() {
        assertEquals("testPlayer", player.getUsername());
    }

    @Test
    void getTokenShouldReturnNullBeforeSetting() {
        assertNull(player.getToken());
    }

    @Test
    void isOnlineShouldReturnTrueInitially() {
        assertTrue(player.isOnline());
    }

    @Test
    void getPlayAreaShouldReturnNullBeforeInitialization() {
        assertNull(player.getPlayArea());
    }

    @Test
    void getSecretObjectiveShouldReturnNullBeforeSetting() {
        assertNull(player.getSecretObjective());
    }

    @Test
    void setTokenShouldSetToken() {
        player.setToken(Token.RED);
        assertEquals(Token.RED, player.getToken());
    }

    @Test
    void setSecretObjectiveShouldSetSecretObjective() {
        ObjectiveCard objectiveCard = new BasicEvaluator();
        player.setSecretObjective(objectiveCard);
        assertEquals(objectiveCard, player.getSecretObjective());
    }

    @Test
    void setOnlineShouldSetOnlineStatus() {
        player.setOnline(false);
        assertFalse(player.isOnline());
    }

    @Test
    void initPlayAreaShouldInitializePlayArea() {
        hand.add(new GoldCard());
        hand.add(new ResourceCard());
        hand.add(new ResourceCard());
        player.initPlayArea(hand, startCard);
        assertNotNull(player.getPlayArea());
    }

    @Test
    void initPlayAreaShouldThrowExceptionForInvalidHand() {
        hand.add(new GoldCard());
        hand.add(new GoldCard());
        hand.add(new ResourceCard());
        assertThrows(IllegalFirstHandException.class, () -> player.initPlayArea(hand, startCard));
    }

    @Test
    void toStringShouldReturnUsername() {
        assertEquals("testPlayer", player.toString());
    }
    */
}
