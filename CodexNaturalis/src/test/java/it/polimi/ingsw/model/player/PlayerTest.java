package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerTest {
    Player player;
    String username = "testUser";
    Token token;
    PlayArea playArea;
    ObjectiveCard secretObjective;

    @BeforeEach
    void setUp() {
        player = new Player(username);
        token = Token.GREEN;
        playArea = mock(PlayArea.class);
        secretObjective = mock(ObjectiveCard.class);
    }

    @Test
    void getUsername() {
        assertEquals(username, player.getUsername());
    }

    @Test
    void getToken() {
        player.setToken(token);
        assertNotNull(token.getColor());
        assertEquals(token, player.getToken());
    }

    @Test
    void isOnline() {
        assertTrue(player.isOnline());
        player.setOnline(false);
        assertFalse(player.isOnline());
    }

    @Test
    void getSecretObjective() {
        player.setSecretObjective(secretObjective);
        assertEquals(secretObjective, player.getSecretObjective());
    }

    @Test
    void initPlayArea() {
        List<PlayableCard> hand = new ArrayList<>();
        hand.add(new GoldCard("GCtest1", null, 5, Item.PLANT, null, null, null));
        hand.add(new ResourceCard("RCtest1", null, 5, Item.PLANT, null));
        hand.add(new ResourceCard("RCtest2", null, 5, Item.PLANT, null));
        StartCard startCard = mock(StartCard.class);

        player.initPlayArea(hand, startCard);

        assertNotNull(player.getPlayArea());
    }

    @Test
    void initPlayAreaThrowsExceptionSize() {
        List<PlayableCard> hand = new ArrayList<>();
        hand.add(mock(GoldCard.class));
        StartCard startCard = mock(StartCard.class);

        assertThrows(RuntimeException.class, () -> player.initPlayArea(hand, startCard));
    }

    @Test
    void initPlayAreaThrowsExceptionTypes() {
        List<PlayableCard> hand = new ArrayList<>();
        hand.add(mock(GoldCard.class));
        hand.add(mock(GoldCard.class));
        hand.add(mock(ResourceCard.class));
        StartCard startCard = mock(StartCard.class);

        assertThrows(RuntimeException.class, () -> player.initPlayArea(hand, startCard));
    }

    @Test
    void testToString() {
        assertEquals(username, player.toString());
    }
}
