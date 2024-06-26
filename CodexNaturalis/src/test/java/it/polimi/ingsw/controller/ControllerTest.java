package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ControllerTest {
    private Controller controller;
    private VirtualView virtualView1;
    private VirtualView virtualView2;
    private VirtualView virtualView3;
    private GameListener gameListener1;
    private GameListener gameListener2;
    private GameListener gameListener3;
    private final String username1 = "username1";
    private final String username2 = "username2";
    private final String username3 = "username3";
    private Account account1;
    private Account account2;
    private Account account3;

    @BeforeEach
    void setUp() {
        JsonConfig.loadConfig();
        controller = Controller.getInstance();
        gameListener1 = (event)-> System.out.println("1: "+ event.getID());
        gameListener2 = (event)-> System.out.println("2: "+ event.getID());
        gameListener3 = (event)-> System.out.println("3: "+ event.getID());
        virtualView1 = new VirtualView(gameListener1);
        virtualView2 = new VirtualView(gameListener2);
        virtualView3 = new VirtualView(gameListener3);
        account1 = new Account(username1, "password1");
        account2 = new Account(username2, "password2");
        account3 = new Account(username3, "password3");

        controller.register(account1);
        controller.register(account2);
        controller.register(account3);
    }

    @Test
    void testLoginSuccess() {
        LoginEvent result = controller.login(virtualView1, account1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals(username1, result.getUsername());
        assertEquals("Login successful!", result.getMessage());

        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testLoginFailureInvalidFormat() {
        LoginEvent result = controller.login(virtualView1, new Account("user name", " "));

        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("The username or password is not valid.", result.getMessage());
    }

    @Test
    void testLoginFailureAlreadyLoggedInSameAccount() {
        LoginEvent prev = controller.login(virtualView1, account1);
        System.out.println(prev.getMessage());
        assertEquals(prev.getFeedback(), Feedback.SUCCESS);

        LoginEvent result = controller.login(virtualView1, account1);

        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals(username1, result.getUsername());
        assertEquals("You are already logged in to this account.", result.getMessage());

        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testLoginFailureAlreadyLoggedInDifferentAccount() {
        controller.login(virtualView1, account1);

        LoginEvent result = controller.login(virtualView1, account2);

        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals(username2, result.getUsername());
        assertEquals("You are already logged in to another account.", result.getMessage());

        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testLoginFailureAccountConnectedToAnotherInstance() {
        controller.login(virtualView1, account1);

        LoginEvent result = controller.login(virtualView2, account1);

        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals(username1, result.getUsername());
        assertEquals("This account is already connected to another instance.", result.getMessage());

        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testLogoutWithoutLogin() {
        VirtualView virtualView = Mockito.mock(VirtualView.class);

        LogoutEvent result = controller.logout(virtualView);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("You have already logged out.", result.getMessage());
    }

    @Test
    void testLogoutWhenGameNotStarted() throws NoSuchFieldException, IllegalAccessException {
        controller.login(virtualView1, account1);

        GameController gameController = Mockito.mock(GameController.class);
        when(gameController.getPlayers()).thenReturn(Collections.singletonList(account1.getUsername()));
        when(gameController.isGameStarted()).thenReturn(false);

        Field gameControllersField = Controller.class.getDeclaredField("gameControllers");
        gameControllersField.setAccessible(true);
        List<GameController> gameControllers = (List<GameController>) gameControllersField.get(controller);
        gameControllers.add(gameController);

        LogoutEvent result = controller.logout(virtualView1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Logout successful!", result.getMessage());
        gameControllers.clear();
    }

    @Test
    void testLogoutWhenGameStarted() throws NoSuchFieldException, IllegalAccessException {
        controller.login(virtualView1, account1);

        GameController gameController = Mockito.mock(GameController.class);
        when(gameController.getPlayers()).thenReturn(Collections.singletonList(account1.getUsername()));
        when(gameController.isGameStarted()).thenReturn(true);

        Field gameControllersField = Controller.class.getDeclaredField("gameControllers");
        gameControllersField.setAccessible(true);
        List<GameController> gameControllers = (List<GameController>) gameControllersField.get(controller);
        gameControllers.add(gameController);

        LogoutEvent result = controller.logout(virtualView1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Logout successful!", result.getMessage());
        gameControllers.clear();
    }

    @Test
    void testLogoutWhenPlayerNotInAnyGame() {
        // Login first
        controller.login(virtualView1, account1);

        LogoutEvent result = controller.logout(virtualView1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Logout successful!", result.getMessage());
    }

    @Test
    void testRegisterFailureInvalidFormat() {
        RegisterEvent result1 = controller.register(new Account("user name", "password"));
        assertEquals(Feedback.FAILURE, result1.getFeedback());
        assertEquals("Both the username and password must not contain spaces and must not be empty.", result1.getMessage());

        RegisterEvent result2 = controller.register(new Account("", ""));
        assertEquals(Feedback.FAILURE, result2.getFeedback());
        assertEquals("Both the username and password must not contain spaces and must not be empty.", result2.getMessage());
    }

    @Test
    void testDeleteAccount() {
        Account newAccount = new Account("testUser", "testPassword");
        controller.register(newAccount);

        controller.login(virtualView1, newAccount);

        DeleteAccountEvent result = controller.deleteAccount(virtualView1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Account deleted successfully!", result.getMessage());
    }

    @Test
    void testDeleteAccountWithoutLogin() {
        DeleteAccountEvent result = controller.deleteAccount(virtualView1);

        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You must log in first.", result.getMessage());
    }

    @Test
    void testCreateLobbyWithoutLogin() {
        Pair<CreateLobbyEvent, GameController> result = controller.createLobby(virtualView1, gameListener1, "testLobby", 3);

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("You must log in first.", result.key().getMessage());
    }

    @Test
    void testCreateLobbyInvalidLobbyID() {
        controller.login(virtualView1, account1);

        Pair<CreateLobbyEvent, GameController> result = controller.createLobby(virtualView1, gameListener1, "", 3);

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The provided lobby ID is not allowed.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testCreateLobbyInvalidPlayerNumber() {
        controller.login(virtualView1, account1);

        Pair<CreateLobbyEvent, GameController> result = controller.createLobby(virtualView1, gameListener1, "testLobby", 5);

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The number of players should be between 2 and 4.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testCreateLobbyLobbyIDInUse() {
        controller.login(virtualView1, account1);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 3);

        Pair<CreateLobbyEvent, GameController> result = controller.createLobby(virtualView1, gameListener1, "testLobby", 3);

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The lobby ID you entered is already in use.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testCreateLobbyAccountOnlineInLobby() {
        controller.login(virtualView1, account1);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 3);

        Pair<CreateLobbyEvent, GameController> result = controller.createLobby(virtualView1, gameListener1, "testLobby2", 3);

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("Your account is already online in a lobby.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testCreateLobbySuccess() {
        controller.login(virtualView1, account1);

        Pair<CreateLobbyEvent, GameController> result = controller.createLobby(virtualView1, gameListener1, "testLobby", 3);

        assertEquals(Feedback.SUCCESS, result.key().getFeedback());
        assertEquals("New testLobby lobby has been created!", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testJoinLobbyWithoutLogin() {
        Pair<JoinLobbyEvent, GameController> result = controller.joinLobby(virtualView1, gameListener1, "testLobby");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("You must log in first.", result.key().getMessage());
    }

    @Test
    void testJoinLobbyInvalidLobbyID() {
        controller.login(virtualView1, account1);

        Pair<JoinLobbyEvent, GameController> result = controller.joinLobby(virtualView1, gameListener1, "");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The provided lobby ID is not valid.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testJoinLobbyLobbyFull() {
        controller.login(virtualView1, account1);
        controller.login(virtualView2, account2);
        controller.login(virtualView3, account3);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 2);

        Pair<JoinLobbyEvent, GameController> prev = controller.joinLobby(virtualView2, gameListener2, "testLobby");
        assertEquals(Feedback.SUCCESS, prev.key().getFeedback());
        Pair<JoinLobbyEvent, GameController> result = controller.joinLobby(virtualView3, gameListener3, "testLobby");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The lobby is already full.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView2);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView3);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testJoinLobbyLobbyStarted() {
        controller.login(virtualView1, account1);
        controller.login(virtualView2, account2);
        controller.login(virtualView3, account3);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 2);

        Pair<JoinLobbyEvent, GameController> prev = controller.joinLobby(virtualView2, gameListener2, "testLobby");
        assertEquals(Feedback.SUCCESS, prev.key().getFeedback());
        prev.value().readyToStart(virtualView1);
        prev.value().readyToStart(virtualView2);
        prev.value().startCardsSetup();
        Pair<JoinLobbyEvent, GameController> result = controller.joinLobby(virtualView3, gameListener3, "testLobby");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The lobby has already started a game.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView2);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView3);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testJoinLobbyLobbyDoesNotExist() {
        controller.login(virtualView1, account1);

        Pair<JoinLobbyEvent, GameController> result = controller.joinLobby(virtualView1, gameListener1, "nonExistentLobby");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The lobby does not exist.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testJoinLobbyAccountOnlineInGame() {
        controller.login(virtualView1, account1);
        controller.login(virtualView2, account2);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 2);
        Pair<JoinLobbyEvent, GameController> prev = controller.joinLobby(virtualView2, gameListener2, "testLobby");
        assertEquals(Feedback.SUCCESS, prev.key().getFeedback());
        prev.value().readyToStart(virtualView1);
        prev.value().readyToStart(virtualView2);
        prev.value().startCardsSetup();

        Pair<JoinLobbyEvent, GameController> result = controller.joinLobby(virtualView2, gameListener2, "anotherLobby");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("Your account is already online in a game.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView2);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testJoinLobbyAccountOnlineInLobby() {
        controller.login(virtualView1, account1);
        controller.login(virtualView2, account2);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 2);
        Pair<JoinLobbyEvent, GameController> prev = controller.joinLobby(virtualView2, gameListener2, "testLobby");
        assertEquals(Feedback.SUCCESS, prev.key().getFeedback());

        Pair<JoinLobbyEvent, GameController> result = controller.joinLobby(virtualView2, gameListener2, "anotherLobby");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("Your account is already online in a lobby.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView2);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testReconnectToGameWithoutLogin() {
        Pair<ReconnectToGameEvent, GameController> result = controller.reconnectToGame(virtualView1, gameListener1, "testGame");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("You must log in first.", result.key().getMessage());
    }

    @Test
    void testReconnectToGameInvalidGameID() {
        controller.login(virtualView1, account1);

        Pair<ReconnectToGameEvent, GameController> result = controller.reconnectToGame(virtualView1, gameListener1, "");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The provided game ID is not allowed.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testReconnectToGameGameDoesNotExist() {
        controller.login(virtualView1, account1);

        Pair<ReconnectToGameEvent, GameController> result = controller.reconnectToGame(virtualView1, gameListener1, "nonExistentGame");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("The game does not exist, or you do not have the permission to enter it.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testReconnectToGameAccountOnlineInGame() {
        controller.login(virtualView1, account1);
        controller.login(virtualView2, account2);

        controller.createLobby(virtualView1, gameListener1, "testGame", 2);
        Pair<JoinLobbyEvent, GameController> prev = controller.joinLobby(virtualView2, gameListener2, "testGame");
        assertEquals(Feedback.SUCCESS, prev.key().getFeedback());
        prev.value().readyToStart(virtualView1);
        prev.value().readyToStart(virtualView2);
        prev.value().startCardsSetup();

        Pair<ReconnectToGameEvent, GameController> result = controller.reconnectToGame(virtualView2, gameListener2, "testGame");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("Your account is already online in a game.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView2);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testReconnectToGameSuccess() {
        controller.login(virtualView1, account1);
        controller.login(virtualView2, account2);

        controller.createLobby(virtualView1, gameListener1, "testGame", 2);
        Pair<JoinLobbyEvent, GameController> prev = controller.joinLobby(virtualView2, gameListener2, "testGame");
        assertEquals(Feedback.SUCCESS, prev.key().getFeedback());
        prev.value().readyToStart(virtualView1);
        prev.value().readyToStart(virtualView2);
        prev.value().startCardsSetup();

        controller.clientDisconnected(virtualView2);
        controller.login(virtualView2, account2);

        Pair<ReconnectToGameEvent, GameController> result = controller.reconnectToGame(virtualView2, gameListener2, "testGame");

        assertEquals(Feedback.SUCCESS, result.key().getFeedback());
        assertEquals("ok", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView2);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testReconnectToGameAccountOnlineInLobby() {
        controller.login(virtualView1, account1);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 2);

        Pair<ReconnectToGameEvent, GameController> result = controller.reconnectToGame(virtualView1, gameListener1, "testGame");

        assertEquals(Feedback.FAILURE, result.key().getFeedback());
        assertEquals("Your account is already online in a lobby.", result.key().getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testGetLobbiesAvailableWithoutLogin() {
        AvailableLobbiesEvent result = controller.getLobbiesAvailable(virtualView1);

        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You must log in first.", result.getMessage());
    }

    @Test
    void testGetLobbiesAvailableNoLobbies() {
        controller.login(virtualView1, account1);

        AvailableLobbiesEvent result = controller.getLobbiesAvailable(virtualView1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("There are no available lobbies.", result.getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testGetLobbiesAvailableWithLobbies() {
        controller.login(virtualView1, account1);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 2);

        AvailableLobbiesEvent result = controller.getLobbiesAvailable(virtualView1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Choose one of the available lobbies:", result.getMessage());
        assertEquals(1, result.getLobbies().size());
        assertEquals("testLobby", result.getLobbies().getFirst().getId());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testGetMyOfflineGamesAvailableWithoutLogin() {
        GetMyOfflineGamesEvent result = controller.getMyOfflineGamesAvailable(virtualView1);

        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You must log in first.", result.getMessage());
    }

    @Test
    void testGetMyOfflineGamesAvailableNoGames() {
        controller.login(virtualView1, account1);

        GetMyOfflineGamesEvent result = controller.getMyOfflineGamesAvailable(virtualView1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("You do not have any available games.", result.getMessage());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testGetMyOfflineGamesAvailableWithGames() {
        controller.login(virtualView1, account1);
        controller.login(virtualView2, account2);

        controller.createLobby(virtualView1, gameListener1, "testGame", 2);
        Pair<JoinLobbyEvent, GameController> prev = controller.joinLobby(virtualView2, gameListener2, "testGame");
        assertEquals(Feedback.SUCCESS, prev.key().getFeedback());

        prev.value().readyToStart(virtualView1);
        prev.value().readyToStart(virtualView2);
        prev.value().startCardsSetup();

        controller.clientDisconnected(virtualView1);
        controller.login(virtualView1, account1);

        GetMyOfflineGamesEvent result = controller.getMyOfflineGamesAvailable(virtualView1);

        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Choose one of the available games:", result.getMessage());
        assertEquals(1, result.getGames().size());
        assertEquals("testGame", result.getGames().getFirst().getId());
        LogoutEvent logout = controller.logout(virtualView1);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
        logout = controller.logout(virtualView2);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }

    @Test
    void testClientDisconnectedWithoutLogin() {
        controller.clientDisconnected(virtualView1);

        assertEquals(0, controller.getVirtualViewAccounts().size());
        assertEquals(0, controller.getGameControllers().size());
    }

    @Test
    void testClientDisconnectedInLobby() {
        controller.login(virtualView1, account1);

        controller.createLobby(virtualView1, gameListener1, "testLobby", 2);

        controller.clientDisconnected(virtualView1);

        assertEquals(0, controller.getVirtualViewAccounts().size());
    }

    @Test
    void testClientDisconnectedInGame() {
        controller.login(virtualView1, account1);
        controller.login(virtualView2, account2);

        controller.createLobby(virtualView1, gameListener1, "testGame", 2);
        Pair<JoinLobbyEvent, GameController> prev = controller.joinLobby(virtualView2, gameListener2, "testGame");
        assertEquals(Feedback.SUCCESS, prev.key().getFeedback());

        prev.value().readyToStart(virtualView1);
        prev.value().readyToStart(virtualView2);
        prev.value().startCardsSetup();

        controller.clientDisconnected(virtualView1);

        assertEquals(1, controller.getVirtualViewAccounts().size());
        assertEquals(1, controller.getGameControllers().getFirst().nOnlinePlayers());
        LogoutEvent logout = controller.logout(virtualView2);
        assertEquals(Feedback.SUCCESS, logout.getFeedback());
    }
}