package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChosenCardsSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChosenTokenSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.DrawCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.PlaceCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    private GameController gameController;
    private VirtualView vv1;
    private VirtualView vv2;
    private VirtualView vv3;
    private GameListener gl1;
    private GameListener gl2;
    private GameListener gl3;
    private final String username1 = "username1";
    private final String username2 = "username2";
    private final String username3 = "username3";
    private Account account1;
    private Account account2;
    private Account account3;

    @BeforeEach
    public void setup() {
        JsonConfig.loadConfig();
        gl1 = (event)-> System.out.println("1: "+ event.getID());
        gl2 = (event)-> System.out.println("2: "+ event.getID());
        gl3 = (event)-> System.out.println("3: "+ event.getID());
        vv1 = new VirtualView(gl1);
        vv2 = new VirtualView(gl2);
        vv3 = new VirtualView(gl3);
        account1 = new Account(username1, "password1");
        account2 = new Account(username2, "password2");
        account3 = new Account(username3, "password3");
        gameController = new GameController(vv1, account1, gl1, "game1", 2);
    }

    @Test
    public void testKickPlayer() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        KickFromLobbyEvent result = gameController.kickPlayer(vv1, username2);
        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Kick successful!", result.getMessage());
        assertEquals(1, gameController.nOnlinePlayers());

        result = gameController.kickPlayer(vv1, username1);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You cannot kick yourself.", result.getMessage());

        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        result = gameController.kickPlayer(vv2, username1);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You are not the host of the lobby.", result.getMessage());
        assertEquals(2, gameController.nOnlinePlayers());

        result = gameController.kickPlayer(vv1, username3);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("Player " + username3 + " is not in the lobby.", result.getMessage());

        gameController.quitLobby(vv1);
        assertEquals(1, gameController.nOnlinePlayers());
        gameController.addLobbyPlayer(vv1, account1, gl1);
        assertEquals(2, gameController.nOnlinePlayers());

        result = gameController.kickPlayer(vv2, username1);
        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Kick successful!", result.getMessage());
        gameController.quitLobby(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testKickPlayerDuringGame() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        gameController.readyToStart(vv1);
        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        KickFromLobbyEvent result = gameController.kickPlayer(vv1, username2);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You cannot kick other players during the game.", result.getMessage());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testReadyToStart() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        PlayerReadyEvent result = gameController.readyToStart(vv2);
        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("You are now ready!", result.getMessage());

        result = gameController.readyToStart(vv2);
        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("You were already ready.", result.getMessage());

        result = gameController.readyToStart(vv3);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You are not in the lobby.", result.getMessage());

        gameController.readyToStart(vv1);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        result = gameController.readyToStart(vv2);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("The ready status applies only to the lobby phase.", result.getMessage());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testUnReadyToStart() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        PlayerReadyEvent prev = gameController.readyToStart(vv2);
        assertEquals(Feedback.SUCCESS, prev.getFeedback());
        assertEquals("You are now ready!", prev.getMessage());

        PlayerUnreadyEvent result = gameController.unReadyToStart(vv2);
        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("You are now unready!", result.getMessage());

        result = gameController.unReadyToStart(vv2);
        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("You were already unready.", result.getMessage());

        result = gameController.unReadyToStart(vv3);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You are not in the lobby.", result.getMessage());

        gameController.readyToStart(vv1);
        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        result = gameController.unReadyToStart(vv2);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("The unready status applies only to the lobby phase.", result.getMessage());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testQuitLobby() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        QuitLobbyEvent result = gameController.quitLobby(vv2);
        assertEquals(Feedback.SUCCESS, result.getFeedback());
        assertEquals("Successfully left the lobby.", result.getMessage());
        assertEquals(1, gameController.nOnlinePlayers());

        result = gameController.quitLobby(vv2);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You are not in the lobby.", result.getMessage());
        assertEquals(1, gameController.nOnlinePlayers());

        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        gameController.readyToStart(vv1);
        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        result = gameController.quitLobby(vv2);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You can only quit a lobby during the session.", result.getMessage());
        assertEquals(2, gameController.nOnlinePlayers());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testStartCardsSetup() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        gameController.readyToStart(vv1);
        gameController.startCardsSetup();
        assertFalse(gameController.isGameStarted());

        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testChosenCardsSetup() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        ChosenCardsSetupEvent prev = gameController.chosenCardsSetup(vv2, "ObjectiveCardID", true);
        assertEquals(Feedback.FAILURE, prev.getFeedback());
        assertEquals("You can only choose during the cards-setup phase.", prev.getMessage());

        gameController.readyToStart(vv1);
        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        boolean chosen = false;
        int i = 1;
        ChosenCardsSetupEvent result = null;
        String objectiveCardID = "";


        while (!chosen && i <= 16) {
            objectiveCardID = String.format("OC%02d", i);
            result = gameController.chosenCardsSetup(vv2, objectiveCardID, true);
            if (result.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                i++;
        }
        assert i <= 16;
        assertEquals("Your cards setup has been chosen!", result.getMessage());

        result = gameController.chosenCardsSetup(vv2, objectiveCardID, true);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("Choice has already been made.", result.getMessage());

        result = gameController.chosenCardsSetup(vv3, "ObjectiveCardID", true);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You are not in the game.", result.getMessage());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testStartTokenSetup() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        gameController.readyToStart(vv1);
        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        boolean chosen = false;
        int i = 1;
        ChosenCardsSetupEvent result;
        String objectiveCardID;

        while (!chosen && i <= 16) {
            objectiveCardID = String.format("OC%02d", i);
            result = gameController.chosenCardsSetup(vv1, objectiveCardID, true);
            if (result.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                i++;
        }
        assert i <= 16;

        chosen = false;
        i = 1;
        while (!chosen && i <= 16) {
            objectiveCardID = String.format("OC%02d", i);
            result = gameController.chosenCardsSetup(vv2, objectiveCardID, true);
            if (result.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                i++;
        }
        assert i <= 16;

        gameController.startTokenSetup();
        ChosenCardsSetupEvent prev = gameController.chosenCardsSetup(vv2, "ObjectiveCardID", true);
        assertEquals(Feedback.FAILURE, prev.getFeedback());
        assertEquals("You can only choose during the cards-setup phase.", prev.getMessage());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testChosenTokenSetup() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        gameController.readyToStart(vv1);
        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        boolean chosen = false;
        int i = 1;
        ChosenCardsSetupEvent prev;
        String objectiveCardID;

        while (!chosen && i <= 16) {
            objectiveCardID = String.format("OC%02d", i);
            prev = gameController.chosenCardsSetup(vv1, objectiveCardID, true);
            if (prev.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                i++;
        }
        assert i <= 16;

        chosen = false;
        i = 1;
        while (!chosen && i <= 16) {
            objectiveCardID = String.format("OC%02d", i);
            prev = gameController.chosenCardsSetup(vv2, objectiveCardID, true);
            if (prev.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                i++;
        }
        assert i <= 16;

        gameController.startTokenSetup();
        gameController.disconnectFromGame(vv2);

        ChosenTokenSetupEvent result = gameController.chosenTokenSetup(vv1, Token.RED);
        assertEquals(Feedback.SUCCESS, result.getFeedback());
        gameController.startRunning();

        gameController.quitGame(vv1);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testChosenTokenSetupConditions() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        gameController.readyToStart(vv1);
        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        boolean chosen = false;
        int i = 1;
        ChosenCardsSetupEvent prev;
        String objectiveCardID;

        while (!chosen && i <= 16) {
            objectiveCardID = String.format("OC%02d", i);
            prev = gameController.chosenCardsSetup(vv1, objectiveCardID, true);
            if (prev.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                i++;
        }
        assert i <= 16;

        chosen = false;
        i = 1;
        while (!chosen && i <= 16) {
            objectiveCardID = String.format("OC%02d", i);
            prev = gameController.chosenCardsSetup(vv2, objectiveCardID, true);
            if (prev.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                i++;
        }
        assert i <= 16;

        gameController.startTokenSetup();
        ChosenTokenSetupEvent result = gameController.chosenTokenSetup(vv2, null);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("Token not available.", result.getMessage());

        gameController.disconnectFromGame(vv2);
        gameController.reconnectPlayer(vv2, account2, gl2);

        result = gameController.chosenTokenSetup(vv2, Token.RED);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You cannot choose after a disconnection, wait for the timer to run out.", result.getMessage());

        result = gameController.chosenTokenSetup(vv1, Token.RED);
        assertEquals(Feedback.SUCCESS, result.getFeedback());

        gameController.startRunning();

        result = gameController.chosenTokenSetup(vv1, Token.RED);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You can only choose during the token-setup phase.", result.getMessage());

        result = gameController.chosenTokenSetup(vv3, Token.RED);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals("You are not in the game.", result.getMessage());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testPlaceCard() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        gameController.readyToStart(vv1);
        gameController.readyToStart(vv2);
        gameController.startCardsSetup();
        assertTrue(gameController.isGameStarted());

        boolean chosen = false;
        int j = 1;
        ChosenCardsSetupEvent resultCard;
        String objectiveCardID;

        while (!chosen && j <= 16) {
            objectiveCardID = String.format("OC%02d", j);
            resultCard = gameController.chosenCardsSetup(vv1, objectiveCardID, true);
            if (resultCard.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                j++;
        }
        assert j <= 16;

        chosen = false;
        j = 1;
        while (!chosen && j <= 16) {
            objectiveCardID = String.format("OC%02d", j);
            resultCard = gameController.chosenCardsSetup(vv2, objectiveCardID, true);
            if (resultCard.getFeedback() == Feedback.SUCCESS)
                chosen = true;
            else
                j++;
        }
        assert j <= 16;

        gameController.startTokenSetup();
        ChosenTokenSetupEvent resultToken = gameController.chosenTokenSetup(vv1, Token.RED);
        assertEquals(Feedback.SUCCESS, resultToken.getFeedback());
        resultToken = gameController.chosenTokenSetup(vv2, Token.BLUE);
        assertEquals(Feedback.SUCCESS, resultToken.getFeedback());

        gameController.startRunning();

        boolean placed = false;
        int i = 1;
        PlaceCardEvent prev1;
        PlaceCardEvent prev2;
        String playableCardID = "";
        VirtualView vvTurn = null;

        while (!placed && i <= 40) {
            playableCardID = String.format("RC%02d", i);
            prev1 = gameController.placeCard(vv1, playableCardID, new Coordinate(1, 1), false);
            prev2 = gameController.placeCard(vv2, playableCardID, new Coordinate(1, 1), false);
            if (prev1.getFeedback() == Feedback.SUCCESS) {
                placed = true;
                vvTurn = vv1;
            } else if (prev2.getFeedback() == Feedback.SUCCESS) {
                placed = true;
                vvTurn = vv2;
            } else
                i++;
        }
        assert i <= 40;

        PlaceCardEvent result = gameController.placeCard(vvTurn, playableCardID, new Coordinate(1, 1), false);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        result = gameController.placeCard(vv3, playableCardID, new Coordinate(1, 1), false);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        gameController.quitGame(vv1);
        gameController.quitGame(vv2);
        result = gameController.placeCard(vvTurn, playableCardID, new Coordinate(1, 1), false);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testDrawCard() {
        gameController.quitLobby(vv1);
        assertEquals(0, gameController.nOnlinePlayers());
        GameController gameControllerN3 = new GameController(vv1, account1, gl1, "game2", 3);
        gameControllerN3.addLobbyPlayer(vv2, account2, gl2);
        gameControllerN3.addLobbyPlayer(vv3, account3, gl3);
        assertEquals(3, gameControllerN3.nOnlinePlayers());

        List<VirtualView> vvList = new ArrayList<>(Arrays.asList(vv1, vv2, vv3));

        for (VirtualView vv : vvList)
            gameControllerN3.readyToStart(vv);
        gameControllerN3.startCardsSetup();
        assertTrue(gameControllerN3.isGameStarted());

        boolean chosen;
        int j;
        ChosenCardsSetupEvent resultCard;
        String objectiveCardID;

        for (VirtualView vv : vvList) {
            chosen = false;
            j = 1;
            while (!chosen && j <= 16) {
                objectiveCardID = String.format("OC%02d", j);
                resultCard = gameControllerN3.chosenCardsSetup(vv, objectiveCardID, true);
                if (resultCard.getFeedback() == Feedback.SUCCESS)
                    chosen = true;
                else
                    j++;
            }
            assert j <= 16;
        }

        gameControllerN3.startTokenSetup();

        ChosenTokenSetupEvent resultToken = gameControllerN3.chosenTokenSetup(vv1, Token.RED);
        assertEquals(Feedback.SUCCESS, resultToken.getFeedback());
        resultToken = gameControllerN3.chosenTokenSetup(vv2, Token.BLUE);
        assertEquals(Feedback.SUCCESS, resultToken.getFeedback());
        resultToken = gameControllerN3.chosenTokenSetup(vv3, Token.GREEN);
        assertEquals(Feedback.SUCCESS, resultToken.getFeedback());

        gameControllerN3.startRunning();

        DrawCardEvent result;
        for (VirtualView vv : vvList) {
            result = gameControllerN3.drawCard(vv, CardType.RESOURCE, 0);
            assertEquals(Feedback.FAILURE, result.getFeedback());
        }

        boolean placed = false;
        int i = 1;
        PlaceCardEvent prev1;
        PlaceCardEvent prev2;
        PlaceCardEvent prev3;
        String playableCardID;
        VirtualView vvTurn = null;

        while (!placed && i <= 40) {
            playableCardID = String.format("RC%02d", i);
            prev1 = gameControllerN3.placeCard(vv1, playableCardID, new Coordinate(1, 1), false);
            prev2 = gameControllerN3.placeCard(vv2, playableCardID, new Coordinate(1, 1), false);
            prev3 = gameControllerN3.placeCard(vv3, playableCardID, new Coordinate(1, 1), false);
            if (prev1.getFeedback() == Feedback.SUCCESS) {
                placed = true;
                vvTurn = vv1;
            } else if (prev2.getFeedback() == Feedback.SUCCESS) {
                placed = true;
                vvTurn = vv2;
            } else if (prev3.getFeedback() == Feedback.SUCCESS) {
                placed = true;
                vvTurn = vv3;
            } else
                i++;
        }
        assert i <= 40;

        result = gameControllerN3.drawCard(vvTurn, null, 0);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        result = gameControllerN3.drawCard(new VirtualView(System.out::println), CardType.RESOURCE, 0);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        result = gameControllerN3.drawCard(vvTurn, CardType.RESOURCE, 0);
        assertEquals(Feedback.SUCCESS, result.getFeedback());

        result = gameControllerN3.drawCard(vvTurn, CardType.RESOURCE, 0);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        placed = false;
        i = 1;
        VirtualView vvNext = null;
        Account accNext = null;
        GameListener glNext = null;

        while (!placed && i <= 40) {
            playableCardID = String.format("RC%02d", i);
            prev1 = gameControllerN3.placeCard(vv1, playableCardID, new Coordinate(1, 1), false);
            prev2 = gameControllerN3.placeCard(vv2, playableCardID, new Coordinate(1, 1), false);
            prev3 = gameControllerN3.placeCard(vv3, playableCardID, new Coordinate(1, 1), false);
            if (prev1.getFeedback() == Feedback.SUCCESS) {
                placed = true;
                vvNext = vv1;
                accNext = account1;
                glNext = gl1;
            } else if (prev2.getFeedback() == Feedback.SUCCESS) {
                placed = true;
                vvNext = vv2;
                accNext = account2;
                glNext = gl2;
            } else if (prev3.getFeedback() == Feedback.SUCCESS) {
                placed = true;
                vvNext = vv3;
                accNext = account3;
                glNext = gl3;
            } else
                i++;
        }
        assert i <= 40;
        
        VirtualView vvNext2;
        if (!vvTurn.equals(vv3) && !vvNext.equals(vv3))
            vvNext2 = vv3;
        else if (!vvTurn.equals(vv2) && !vvNext.equals(vv2))
            vvNext2 = vv2;
        else
            vvNext2 = vv1;

        gameControllerN3.disconnectFromGame(vvNext);

        result = gameControllerN3.drawCard(vvTurn, CardType.RESOURCE, 0);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        assertEquals(2, gameControllerN3.nOnlinePlayers());
        gameControllerN3.disconnectFromGame(vvNext2);
        assertEquals(1, gameControllerN3.nOnlinePlayers());

        gameControllerN3.reconnectPlayer(vvNext, accNext, glNext);
        assertEquals(2, gameControllerN3.nOnlinePlayers());

        gameControllerN3.quitGame(vvTurn);
        assertEquals(1, gameControllerN3.nOnlinePlayers());
        gameControllerN3.quitGame(vvNext);
        assertEquals(0, gameControllerN3.nOnlinePlayers());
        result = gameControllerN3.drawCard(vvTurn, CardType.RESOURCE, 0);
        assertEquals(Feedback.FAILURE, result.getFeedback());
        assertEquals(0, gameControllerN3.nOnlinePlayers());
    }

    @Test
    public void testChatGlobalMessage() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        ChatMessage chatMessage = new ChatMessage("Hello, world!");
        ChatGMEvent result = gameController.chatGlobalMessage(vv1, chatMessage);
        assertEquals(Feedback.SUCCESS, result.getFeedback());

        result = gameController.chatGlobalMessage(vv3, chatMessage);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        gameController.quitLobby(vv1);
        gameController.quitLobby(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }

    @Test
    public void testChatPrivateMessage() {
        gameController.addLobbyPlayer(vv2, account2, gl2);
        assertEquals(2, gameController.nOnlinePlayers());

        PrivateChatMessage pChatMessage = new PrivateChatMessage(username2, "Hello, world!");
        ChatPMEvent result = gameController.chatPrivateMessage(vv1, pChatMessage);
        assertEquals(Feedback.SUCCESS, result.getFeedback());

        pChatMessage = new PrivateChatMessage("Hello, world!", "nonExistingUsername");
        result = gameController.chatPrivateMessage(vv1, pChatMessage);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        result = gameController.chatPrivateMessage(vv3, pChatMessage);
        assertEquals(Feedback.FAILURE, result.getFeedback());

        gameController.quitLobby(vv1);
        gameController.quitLobby(vv2);
        assertEquals(0, gameController.nOnlinePlayers());
    }
}