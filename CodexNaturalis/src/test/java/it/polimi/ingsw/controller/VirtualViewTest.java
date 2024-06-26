package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.InvalidEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.event.internal.PingEvent;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VirtualViewTest {
    private Controller controller;
    private GameListener gameListener;
    private GameController gameController;
    private VirtualView virtualView;


    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        JsonConfig.loadConfig();
        controller = Mockito.mock(Controller.class);
        gameListener = Mockito.mock(GameListener.class);
        gameController = Mockito.mock(GameController.class);
        virtualView = new VirtualView(gameListener);

        // Use reflection to set the controller in virtualView
        Field field = VirtualView.class.getDeclaredField("controller");
        field.setAccessible(true);
        field.set(virtualView, controller);

        // Use reflection to set the gameController in virtualView
        Field field1 = VirtualView.class.getDeclaredField("gameController");
        field1.setAccessible(true);
        field1.set(virtualView, gameController);
    }

    @Test
    void testHandleAndEventProcessing() throws InterruptedException {
        Event mockEvent = Mockito.mock(Event.class);
        virtualView.handle(mockEvent);

        Thread.sleep(100);

        verify(mockEvent, times(1)).receiveEvent(virtualView);
    }

    @Test
    void testEvaluateEvent() {
        PingEvent mockEvent = Mockito.mock(PingEvent.class);
        virtualView.evaluateEvent(mockEvent);

        verify(gameListener, times(1)).update(any(InvalidEvent.class));
    }

    @Test
    void testEvaluateEventChosenCardsSetupEvent() {
        ChosenCardsSetupEvent event = new ChosenCardsSetupEvent("CardID", true);
        ChosenCardsSetupEvent response = new ChosenCardsSetupEvent(Feedback.SUCCESS, "Success");

        when(gameController.chosenCardsSetup(virtualView, "CardID", true)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).startTokenSetup();
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventChosenTokenSetupEvent() {
        ChosenTokenSetupEvent event = new ChosenTokenSetupEvent(Token.BLUE);
        ChosenTokenSetupEvent response = new ChosenTokenSetupEvent(Feedback.SUCCESS, "Success");

        when(gameController.chosenTokenSetup(virtualView, Token.BLUE)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).chosenTokenSetup(virtualView, Token.BLUE);
        verify(gameListener, times(1)).update(response);
        verify(gameController, times(1)).startRunning();
    }

    @Test
    void testEvaluateEventDrawCardEvent() {
        DrawCardEvent event = new DrawCardEvent(CardType.RESOURCE, 4);
        DrawCardEvent response = new DrawCardEvent(Feedback.FAILURE, "error");

        when(gameController.drawCard(virtualView, CardType.RESOURCE, 4)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).drawCard(virtualView, CardType.RESOURCE, 4);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventPlaceCardEvent() {
        PlaceCardEvent event = new PlaceCardEvent("CardID", new Coordinate(1, 1), true);
        PlaceCardEvent response = new PlaceCardEvent(Feedback.FAILURE, "Failure");

        when(gameController.placeCard(virtualView, "CardID", new Coordinate(1, 1), true)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).placeCard(virtualView, "CardID", new Coordinate(1, 1), true);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventQuitGameEvent() {
        QuitGameEvent event = new QuitGameEvent();
        QuitGameEvent response = new QuitGameEvent(Feedback.SUCCESS, "Success");

        when(gameController.quitGame(virtualView)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).quitGame(virtualView);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventKickFromLobbyEvent() {
        KickFromLobbyEvent event = new KickFromLobbyEvent("PlayerToKick");
        KickFromLobbyEvent response = new KickFromLobbyEvent(Feedback.SUCCESS, "Success");

        when(gameController.kickPlayer(virtualView, "PlayerToKick")).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).kickPlayer(virtualView, "PlayerToKick");
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventPlayerReadyEvent() {
        PlayerReadyEvent event = new PlayerReadyEvent();
        PlayerReadyEvent response = new PlayerReadyEvent(Feedback.SUCCESS, "Success");

        when(gameController.readyToStart(virtualView)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).readyToStart(virtualView);
        verify(gameController, times(1)).startCardsSetup();
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventPlayerUnreadyEvent() {
        PlayerUnreadyEvent event = new PlayerUnreadyEvent();
        PlayerUnreadyEvent response = new PlayerUnreadyEvent(Feedback.SUCCESS, "Success");

        when(gameController.unReadyToStart(virtualView)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).unReadyToStart(virtualView);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventQuitLobbyEvent() {
        QuitLobbyEvent event = new QuitLobbyEvent();
        QuitLobbyEvent response = new QuitLobbyEvent(Feedback.SUCCESS, "Success");

        when(gameController.quitLobby(virtualView)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).quitLobby(virtualView);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventAvailableLobbiesEvent() {
        AvailableLobbiesEvent event = new AvailableLobbiesEvent();
        AvailableLobbiesEvent response = new AvailableLobbiesEvent(Feedback.SUCCESS, new ArrayList<>(),"Success");

        when(controller.getLobbiesAvailable(virtualView)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).getLobbiesAvailable(virtualView);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventCreateLobbyEvent() {
        CreateLobbyEvent event = new CreateLobbyEvent("LobbyID", 4);
        CreateLobbyEvent response = new CreateLobbyEvent(Feedback.SUCCESS, "Success");

        when(controller.createLobby(virtualView, gameListener, "LobbyID", 4)).thenReturn(new Pair<>(response, null));

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).createLobby(virtualView, gameListener, "LobbyID", 4);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventDeleteAccountEvent() {
        DeleteAccountEvent event = new DeleteAccountEvent();

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).deleteAccount(virtualView);
    }

    @Test
    void testEvaluateEventGetMyOfflineGamesEvent() {
        GetMyOfflineGamesEvent event = new GetMyOfflineGamesEvent();

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).getMyOfflineGamesAvailable(virtualView);
    }

    @Test
    void testEvaluateEventJoinLobbyEvent() {
        JoinLobbyEvent event = new JoinLobbyEvent("LobbyID");
        JoinLobbyEvent response = new JoinLobbyEvent(Feedback.SUCCESS, "Success");

        when(controller.joinLobby(virtualView, gameListener, "LobbyID")).thenReturn(new Pair<>(response, null));

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).joinLobby(virtualView, gameListener, "LobbyID");
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventLoginEvent() {
        Account account = new Account("username", "password");
        LoginEvent event = new LoginEvent("username", "password");
        LoginEvent response = new LoginEvent(Feedback.SUCCESS, "username", "Success");

        when(controller.login(virtualView, account)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).login(virtualView, account);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventLogoutEvent() {
        LogoutEvent event = new LogoutEvent();

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).logout(virtualView);
    }

    @Test
    void testEvaluateEventReconnectToGameEvent() {
        ReconnectToGameEvent event = new ReconnectToGameEvent("GameID");
        ReconnectToGameEvent response = new ReconnectToGameEvent(Feedback.FAILURE, "GameID", "failure");

        when(controller.reconnectToGame(virtualView, gameListener, "GameID")).thenReturn(new Pair<>(response, null));

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).reconnectToGame(virtualView, gameListener, "GameID");
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventRegisterEvent() {
        Account account = new Account("username", "password");
        RegisterEvent event = new RegisterEvent("username", "password");
        RegisterEvent response = new RegisterEvent(Feedback.SUCCESS, "Success");

        when(controller.register(account)).thenReturn(response);

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).register(account);
        verify(gameListener, times(1)).update(response);
    }

    @Test
    void testEvaluateEventChatGMEvent() {
        ChatMessage chatMessage = new ChatMessage("message");
        ChatGMEvent event = new ChatGMEvent(chatMessage);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).chatGlobalMessage(virtualView, chatMessage);
    }

    @Test
    void testEvaluateEventChatPMEvent() {
        PrivateChatMessage pChatMessage = new PrivateChatMessage("message", "recipient");
        ChatPMEvent event = new ChatPMEvent(pChatMessage);

        virtualView.evaluateEvent(event);

        verify(gameController, times(1)).chatPrivateMessage(virtualView, pChatMessage);
    }

    @Test
    void testEvaluateEventClientDisconnectedEvent() {
        ClientDisconnectedEvent event = new ClientDisconnectedEvent();

        virtualView.evaluateEvent(event);

        verify(controller, times(1)).clientDisconnected(virtualView);
    }
}