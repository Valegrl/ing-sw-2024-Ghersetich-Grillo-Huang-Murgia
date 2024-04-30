package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;

/**
 * The UIEventReceiver interface defines the methods that a class must implement to receive and handle UI events.
 */
public interface UIEventReceiver {
    /**
     * Default method to evaluate an event. This method is used only for unexpected events that cannot
     * be handled by the UI.
     *
     * @param event The event to be evaluated.
     */
    default void evaluateEvent(Event event){
        //TODO: Implement a logger to save unexpected events
        //TODO: ignore event
    }

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
     * Handles the event used to communicate that another player is choosing their setup.
     * @param event The event to be handled.
     */
    void evaluateEvent(PlayerIsChoosingSetupEvent event);

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
     * Handles the event used for the local player's setup choice.
     * @param event The event to be handled.
     */
    void evaluateEvent(ChooseSetupEvent event);

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
}
