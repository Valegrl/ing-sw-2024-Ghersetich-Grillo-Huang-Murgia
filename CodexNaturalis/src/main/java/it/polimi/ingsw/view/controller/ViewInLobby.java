package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.UpdateLocalModelEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
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
import it.polimi.ingsw.view.View;

public class ViewInLobby implements ViewState{
    private View view;

    public ViewInLobby(View view){
        this.view = view;
    }

    @Override
    public void evaluateEvent(ChooseCardsSetupEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't choose cards setup.");
    }

    @Override
    public void evaluateEvent(ChooseTokenSetupEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't choose token setup.");
    }

    @Override
    public void evaluateEvent(InvalidEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(KickedPlayerFromLobbyEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(UpdateLobbyPlayersEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(UpdateGamePlayersEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't update players' game.");
    }

    @Override
    public void evaluateEvent(UpdateLocalModelEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't update local game model.");
    }

    @Override
    public void evaluateEvent(AvailablePositionsEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't view the local available card positions.");
    }

    @Override
    public void evaluateEvent(IsMyTurnEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't check if it's the player's turn.");
    }

    @Override
    public void evaluateEvent(SeeOpponentPlayAreaEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't see the opponent's play area.");
    }

    @Override
    public void evaluateEvent(ChosenCardsSetupEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player's cards-setup choice.");
    }

    @Override
    public void evaluateEvent(ChosenTokenSetupEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player's token-setup choice.");
    }

    @Override
    public void evaluateEvent(DrawCardEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player drawing a card.");
    }

    @Override
    public void evaluateEvent(PlaceCardEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player placing a card.");
    }

    @Override
    public void evaluateEvent(QuitGameEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player quitting the game.");
    }

    @Override
    public void evaluateEvent(KickFromLobbyEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        String kickedPlayer = event.getPlayerToKick();
        view.notifyKickFromLobby(feedback, message, kickedPlayer);
    }

    @Override
    public void evaluateEvent(PlayerReadyEvent event) throws IllegalStateException {
        String message = event.getMessage();
        view.printMessage(message);
    }

    @Override
    public void evaluateEvent(PlayerUnreadyEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(QuitLobbyEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(AvailableLobbiesEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle viewing available lobbies.");
    }

    @Override
    public void evaluateEvent(CreateLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle creating a lobby.");
    }

    @Override
    public void evaluateEvent(DeleteAccountEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle deleting an account.");
    }

    @Override
    public void evaluateEvent(GetMyOfflineGamesEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle getting my offline games event.");
    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player joining a lobby.");
    }

    @Override
    public void evaluateEvent(LoginEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player logging in.");
    }

    @Override
    public void evaluateEvent(LogoutEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player logging out.");
    }

    @Override
    public void evaluateEvent(ReconnectToGameEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player reconnecting to a game.");
    }

    @Override
    public void evaluateEvent(RegisterEvent event) throws IllegalStateException {
        throw new IllegalStateException("In lobby state can't handle the local player registering.");
    }

    @Override
    public void evaluateEvent(ServerCrashedEvent event) throws IllegalStateException {

    }
}
