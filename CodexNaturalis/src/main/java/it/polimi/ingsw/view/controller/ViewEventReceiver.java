package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.*;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetChatMessagesEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetLobbyInfoEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ServerDisconnectedEvent;
import it.polimi.ingsw.model.GameStatus;

import java.util.logging.Logger;

/**
 * The ViewEventReceiver interface defines the methods that a class must implement to receive and handle View events.
 */
public interface ViewEventReceiver {
    /**
     * Logger instance for the ViewEventReceiver class.
     * This logger is used to log warning messages when unexpected events are received.
     */
    Logger logger = Logger.getLogger(ViewEventReceiver.class.getName());

    /**
     * Default method to evaluate an event. This method is used only for unexpected events that cannot
     * be handled by the View.
     *
     * @param event The event to be evaluated.
     */
    default void evaluateEvent(Event event){
        logger.warning("Unexpected event received: " + event);
    }

    /**
     * Handles the event of the token-setup from Server.
     * @param event The ChooseTokenSetupEvent to be handled.
     */
    void evaluateEvent(ChooseTokenSetupEvent event);

    /**
     * Called when an invalid event is sent by the local player.
     * @param event The invalid event sent by the local player.
     */
    void evaluateEvent(InvalidEvent event);

    /**
     * Handles the event when the local player is kicked from the lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(KickedPlayerFromLobbyEvent event);

    /**
     * Handles the event of updating the lobby players.
     * @param event The event to be handled.
     */
    void evaluateEvent(UpdateLobbyPlayersEvent event);

    /**
     * Handles the event of updating the game players.
     * @param event The event to be handled.
     */
    void evaluateEvent(UpdateGamePlayersEvent event);

    /**
     * Handles the event of the cards-setup from Server
     * @param event The ChooseCardsSetupEvent to be handled.
     */
    void evaluateEvent(ChooseCardsSetupEvent event);

    /**
     * Handles the successful draw card event of the local player.
     * @param event The MyDrawCardEvent to be handled.
     */
    void evaluateEvent(MyDrawCardEvent event);

    /**
     * Handles the successful place card event of the local player.
     * @param event The MyPlaceCardEvent to be handled.
     */
    void evaluateEvent(MyPlaceCardEvent event);

    /**
     * Handles the event of a new turn during {@link GameStatus#RUNNING}.
     * @param event The NewTurnEvent to be handled.
     */
    void evaluateEvent(NewTurnEvent event);

    /**
     * Handles the successful draw card event of a remote player.
     * @param event The OtherDrawCardEvent to be handled.
     */
    void evaluateEvent(OtherDrawCardEvent event);

    /**
     * Handles the successful place card event of a remote player.
     * @param event The OtherPlaceCardEvent to be handled.
     */
    void evaluateEvent(OtherPlaceCardEvent event);

    /**
     * Handles the event of ended game.
     * @param event The event to be handled.
     */
    void evaluateEvent(EndedGameEvent event);

    /**
     * Handles the event of updating the local model.
     * @param event The event to be handled.
     */
    void evaluateEvent(UpdateLocalModelEvent event);

    /**
     *
     * Handles the event of available positions in the play area.
     * @param event The event to be handled.
     */
    void evaluateEvent(AvailablePositionsEvent event);

    /**
     * Handles the event of checking if it's the player's turn.
     * @param event The event to be handled.
     */
    void evaluateEvent(IsMyTurnEvent event);

    /**
     * Handles the event of seeing the opponent's play area.
     * @param event The event to be handled.
     */
    void evaluateEvent(SeeOpponentPlayAreaEvent event);

    /**
     * Handles the event used for the local player's cards-setup choice.
     * @param event The event to be handled.
     */
    void evaluateEvent(ChosenCardsSetupEvent event);

    /**
     * Handles the event of the local player's token-setup choice.
     * @param event The ChosenTokenSetupEvent to be handled.
     */
    void evaluateEvent(ChosenTokenSetupEvent event);

    /**
     * Handles the event of the local player drawing a card.
     * @param event The event to be handled.
     */
    void evaluateEvent(DrawCardEvent event);

    /**
     * Handles the event of the local player placing a card.
     * @param event The event to be handled.
     */
    void evaluateEvent(PlaceCardEvent event);

    /**
     * Handles the event of the local player quitting the game.
     * @param event The event to be handled.
     */
    void evaluateEvent(QuitGameEvent event);

    /**
     * Handles the event of initializing lobby info when a player joins.
     * @param event The event to be handled.
     */
    void evaluateEvent(GetLobbyInfoEvent event);

    /**
     * Handles the event of getting chat messages.
     * @param event The event to be handled.
     */
    void evaluateEvent(GetChatMessagesEvent event);

    /**
     * Handles the event of kicking a chosen player from the lobby.
     * This event is only applicable if the local player is the host of the lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(KickFromLobbyEvent event);

    /**
     * Handles the event of the local player being ready.
     * @param event The event to be handled.
     */
    void evaluateEvent(PlayerReadyEvent event);

    /**
     * Handles the event of the local player being unready.
     * @param event The event to be handled.
     */
    void evaluateEvent(PlayerUnreadyEvent event);

    /**
     * Handles the event of the local player quitting the lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(QuitLobbyEvent event);

    /**
     * Handles the event of available lobbies.
     * @param event The event to be handled.
     */
    void evaluateEvent(AvailableLobbiesEvent event);

    /**
     * Handles the event of creating a lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(CreateLobbyEvent event);

    /**
     * Handles the event of deleting an account.
     * @param event The event to be handled.
     */
    void evaluateEvent(DeleteAccountEvent event);

    /**
     * Handles the event of getting the local player's offline games.
     * @param event The event to be handled.
     */
    void evaluateEvent(GetMyOfflineGamesEvent event);

    /**
     * Handles the event of the local player joining a lobby.
     * @param event The event to be handled.
     */
    void evaluateEvent(JoinLobbyEvent event);

    /**
     * Handles the event of the local player logging in.
     * @param event The event to be handled.
     */
    void evaluateEvent(LoginEvent event);

    /**
     * Handles the event of the local player logging out.
     * @param event The event to be handled.
     */
    void evaluateEvent(LogoutEvent event);

    /**
     * Handles the event of the local player reconnecting to a game.
     * @param event The event to be handled.
     */
    void evaluateEvent(ReconnectToGameEvent event);

    /**
     * Handles the event of the local player registering.
     * @param event The event to be handled.
     */
    void evaluateEvent(RegisterEvent event);

    /**
     * Handles the event of a global message chat.
     *
     * @param event The ChatGMEvent to be handled.
     */
    void evaluateEvent(ChatGMEvent event);

    /**
     * Handles the event of a private message chat.
     *
     * @param event The ChatPMEvent to be handled.
     */
    void evaluateEvent(ChatPMEvent event);

    /**
     * Handles the event of a server crash.
     * @param event The ServerCrashedEvent to be handled.
     */
    void evaluateEvent(ServerDisconnectedEvent event);
}
