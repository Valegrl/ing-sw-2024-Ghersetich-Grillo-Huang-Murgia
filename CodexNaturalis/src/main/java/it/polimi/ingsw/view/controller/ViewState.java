package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.UpdateLocalModelEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.AvailablePositionsEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.IsMyTurnEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.SeeOpponentPlayAreaEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ServerCrashedEvent;

import java.lang.*;

/**
 * The ViewState interface defines the methods that need to be implemented by any class that wants to handle events in the {@link ViewController}.
 * These methods are used to evaluate different types of events that can occur in the system.
 * Classes that implement this interface can handle events such as setup events, invalid events, lobby events, game events, and server crash events.
 * Implementing classes:
 * 1. {@link ViewInMenu}
 * 2. {@link ViewInLobby}
 * 3. {@link ViewInGame}
 */
public interface ViewState {
    /**
     * Handles the event of the cards-setup from Server
     * @param event The ChooseCardsSetupEvent to be handled.
     */
    void evaluateEvent(ChooseCardsSetupEvent event) throws IllegalStateException;

    /**
     * Handles the event of the token-setup from Server.
     * @param event The ChooseTokenSetupEvent to be handled.
     */
    void evaluateEvent(ChooseTokenSetupEvent event) throws IllegalStateException;

    /**
     * Called when an invalid event is sent by the local player.
     * @param event The invalid event sent by the local player.
     */
    void evaluateEvent(InvalidEvent event) throws IllegalStateException;

    /**
     * Handles the event when the local player is kicked from the lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(KickedPlayerFromLobbyEvent event) throws IllegalStateException;

    /**
     * Handles the event of updating the lobby players.
     * @param event The event to be handled.
     */
    void evaluateEvent(UpdateLobbyPlayersEvent event) throws IllegalStateException;

    /**
     * Handles the event of updating the game players.
     * @param event The event to be handled.
     */
    void evaluateEvent(UpdateGamePlayersEvent event) throws IllegalStateException ;

    /**
     * Handles the event of updating the local model.
     * @param event The event to be handled.
     */
    void evaluateEvent(UpdateLocalModelEvent event) throws IllegalStateException;

    /**
     *
     * Handles the event of available positions in the play area.
     * @param event The event to be handled.
     */
    void evaluateEvent(AvailablePositionsEvent event) throws IllegalStateException;

    /**
     * Handles the event of checking if it's the player's turn.
     * @param event The event to be handled.
     */
    void evaluateEvent(IsMyTurnEvent event) throws IllegalStateException;

    /**
     * Handles the event of seeing the opponent's play area.
     * @param event The event to be handled.
     */
    void evaluateEvent(SeeOpponentPlayAreaEvent event) throws IllegalStateException;

    /**
     * Handles the event used for the local player's cards-setup choice.
     * @param event The event to be handled.
     */
    void evaluateEvent(ChosenCardsSetupEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player's token-setup choice.
     * @param event The ChosenTokenSetupEvent to be handled.
     */
    void evaluateEvent(ChosenTokenSetupEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player drawing a card.
     * @param event The event to be handled.
     */
    void evaluateEvent(DrawCardEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player placing a card.
     * @param event The event to be handled.
     */
    void evaluateEvent(PlaceCardEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player quitting the game.
     * @param event The event to be handled.
     */
    void evaluateEvent(QuitGameEvent event) throws IllegalStateException;

    /**
     * Handles the event of kicking a chosen player from the lobby.
     * This event is only applicable if the local player is the host of the lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(KickFromLobbyEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player being ready.
     * @param event The event to be handled.
     */
    void evaluateEvent(PlayerReadyEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player being unready.
     * @param event The event to be handled.
     */
    void evaluateEvent(PlayerUnreadyEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player quitting the lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(QuitLobbyEvent event) throws IllegalStateException;

    /**
     * Handles the event of available lobbies.
     * @param event The event to be handled.
     */
    void evaluateEvent(AvailableLobbiesEvent event) throws IllegalStateException;

    /**
     * Handles the event of creating a lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(CreateLobbyEvent event) throws IllegalStateException;

    /**
     * Handles the event of deleting an account.
     * @param event The event to be handled.
     */
    void evaluateEvent(DeleteAccountEvent event) throws IllegalStateException;

    /**
     * Handles the event of getting the local player's offline games.
     * @param event The event to be handled.
     */
    void evaluateEvent(GetMyOfflineGamesEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player joining a lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(JoinLobbyEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player logging in.
     * @param event The event to be handled.
     */
    void evaluateEvent(LoginEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player logging out.
     * @param event The event to be handled.
     */
    void evaluateEvent(LogoutEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player reconnecting to a game.
     * @param event The event to be handled.
     */
    void evaluateEvent(ReconnectToGameEvent event) throws IllegalStateException;

    /**
     * Handles the event of the local player registering.
     * @param event The event to be handled.
     */
    void evaluateEvent(RegisterEvent event) throws IllegalStateException;

    /**
     * Handles the event of a server crash.
     * @param event The ServerCrashedEvent to be handled.
     */
    void evaluateEvent(ServerCrashedEvent event) throws IllegalStateException;
}
