package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.InsufficientPlayersException;
import it.polimi.ingsw.model.exceptions.NonUniqueUsernameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    @Test
    void addPlayerToLobbySuccessfully() {
        Lobby lobby = new Lobby(1, "player1", 2);
        assertDoesNotThrow(() -> lobby.addPlayer("player2"));
        assertEquals(2, lobby.getJoinedPlayers().size());
    }

    @Test
    void addPlayerToFullLobbyThrowsException() {
        Lobby lobby = new Lobby(1, "player1", 2);
        try {
            lobby.addPlayer("player2");
        } catch (NonUniqueUsernameException | FullLobbyException e) {
            throw new RuntimeException(e);
        }
        assertThrows(FullLobbyException.class, () -> lobby.addPlayer("player3"));
    }

    @Test
    void addPlayerWithDuplicateUsernameThrowsException() {
        Lobby lobby = new Lobby(1, "player1", 2);
        assertThrows(NonUniqueUsernameException.class, () -> lobby.addPlayer("player1"));
    }

    @Test
    void removePlayerFromLobbySuccessfully() {
        Lobby lobby = new Lobby(1, "player1", 2);
        try {
            lobby.addPlayer("player2");
        } catch (NonUniqueUsernameException | FullLobbyException e) {
            throw new RuntimeException(e);
        }
        assertTrue(lobby.removePlayer("player2"));
        assertEquals(1, lobby.getJoinedPlayers().size());
    }

    @Test
    void removeNonExistentPlayerFromLobby() {
        Lobby lobby = new Lobby(1, "player1", 2);
        assertFalse(lobby.removePlayer("player2"));
    }

    @Test
    void startGameWithSufficientPlayers() {
        Lobby lobby = new Lobby(1, "player1", 2);
        try {
            lobby.addPlayer("player2");
        } catch (NonUniqueUsernameException | FullLobbyException e) {
            throw new RuntimeException(e);
        }

        assertDoesNotThrow(lobby::startGame);

        Game game = lobby.startGame();
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    void startGameWithInsufficientPlayersThrowsException() {
        Lobby lobby = new Lobby(1, "player1", 2);
        assertThrows(InsufficientPlayersException.class, lobby::startGame);
    }

    @Test
    void lobbyConstructorThrowsExceptionWhenUsernameIsNull() {
        assertThrows(NullPointerException.class, () -> new Lobby(1, null, 2));
    }

    @Test
    void lobbyConstructorThrowsExceptionWhenRequiredPlayersIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Lobby(1, "player1", 5));
        assertThrows(IllegalArgumentException.class, () -> new Lobby(1, "player1", 1));
    }

    @Test
    void addPlayerThrowsExceptionWhenUsernameIsNull() {
        Lobby lobby = new Lobby(1, "player1", 2);
        assertThrows(NullPointerException.class, () -> lobby.addPlayer(null));
    }

    @Test
    void removePlayerThrowsExceptionWhenUsernameIsNull() {
        Lobby lobby = new Lobby(1, "player1", 2);
        assertThrows(NullPointerException.class, () -> lobby.removePlayer(null));
    }

    @Test
    void getJoinedPlayers() {
        Lobby lobby = new Lobby(1, "player1", 2);
        try {
            lobby.addPlayer("player2");
        } catch (NonUniqueUsernameException | FullLobbyException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, lobby.getJoinedPlayers().size());
    }

    @Test
    void getRequiredPlayers() {
        Lobby lobby = new Lobby(1, "player1", 2);
        assertEquals(2, lobby.getRequiredPlayers());
    }
}