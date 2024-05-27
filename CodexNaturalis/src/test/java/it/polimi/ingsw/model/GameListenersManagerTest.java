package it.polimi.ingsw.model;

import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.eventUtils.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameListenersManagerTest {
    Game gameMock;
    GameListener listenerMock1;
    GameListener listenerMock2;
    Event eventMock;
    Map<String, GameListener> listeners;
    GameListenersManager manager;
    Set<String> onlinePlayers;

    @BeforeEach
    void setUp() {
        gameMock = mock(Game.class);
        listenerMock1 = mock(GameListener.class);
        listenerMock2 = mock(GameListener.class);
        eventMock = mock(Event.class);

        listeners = new HashMap<>(){{
            put("user1", listenerMock1);
            put("user2", listenerMock2);
        }};

        onlinePlayers = new HashSet<>(){{
            add("user1");
            add("user2");
        }};

        manager = new GameListenersManager(gameMock, listeners);
        when(gameMock.getOnlinePlayers()).thenReturn(onlinePlayers);
    }

    @Test
    void testNotifyListener() {
        manager.notifyListener("user1", eventMock);

        verify(listenerMock1, times(1)).update(eventMock);
    }

    @Test
    void testNotifyListenerWithOfflineUser() {
        onlinePlayers.remove("user1");

        manager.notifyListener("user1", eventMock);

        verify(listenerMock1, times(0)).update(eventMock);
    }

    @Test
    void testNotifyListenerWithNoListener() {
        manager.changeListener("user1", null);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.notifyListener("user1", eventMock);
        });
    }

    @Test
    void testNotifyAllExceptOne() {
        manager.notifyAllExceptOne("user1", eventMock);

        verify(listenerMock2, times(1)).update(eventMock);
        verify(listenerMock1, times(0)).update(eventMock);
    }

    @Test
    void testNotifyAllExceptOneWithOfflineUser() {
        onlinePlayers.remove("user2");
        manager.notifyAllExceptOne("user1", eventMock);

        verify(listenerMock1, times(0)).update(eventMock);
        verify(listenerMock2, times(0)).update(eventMock);
    }

    @Test
    void testNotifyAllExceptOneWithNoListener() {
        manager.changeListener("user2", null);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.notifyAllExceptOne("user1", eventMock);
        });
    }

    @Test
    void testNotifyAllListeners() {
        manager.notifyAllListeners(eventMock);

        verify(listenerMock1, times(1)).update(eventMock);
        verify(listenerMock2, times(1)).update(eventMock);
    }

    @Test
    void testNotifyAllListenersWithOfflineUser() {
        onlinePlayers.remove("user2");

        manager.notifyAllListeners(eventMock);

        verify(listenerMock1, times(1)).update(eventMock);
        verify(listenerMock2, times(0)).update(eventMock);
    }

    @Test
    void testNotifyAllListenersWithNoListener() {
        manager.changeListener("user2", null);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.notifyAllListeners(eventMock);
        });
    }
}