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
import it.polimi.ingsw.view.View;

public class ViewInGame implements  ViewState {
    private View view;

    public ViewInGame(View view){
        this.view = view;
    }

    @Override
    public void evaluateEvent(ChooseCardsSetupEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(ChooseTokenSetupEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(InvalidEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(KickedPlayerFromLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle a player kicked from lobby.");
    }

    @Override
    public void evaluateEvent(UpdateLobbyPlayersEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the lobby update.");
    }

    @Override
    public void evaluateEvent(UpdateGamePlayersEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(UpdateLocalModelEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(AvailablePositionsEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(IsMyTurnEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(SeeOpponentPlayAreaEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(ChosenCardsSetupEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(ChosenTokenSetupEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(DrawCardEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(PlaceCardEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(QuitGameEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(KickFromLobbyEvent event) throws IllegalStateException {

    }

    @Override
    public void evaluateEvent(PlayerReadyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the local player being ready.");
    }

    @Override
    public void evaluateEvent(PlayerUnreadyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the local player being unready.");
    }

    @Override
    public void evaluateEvent(QuitLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the local player quitting the lobby.");
    }

    @Override
    public void evaluateEvent(AvailableLobbiesEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle viewing available lobbies.");
    }

    @Override
    public void evaluateEvent(CreateLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle creating a lobby.");
    }

    @Override
    public void evaluateEvent(DeleteAccountEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle deleting an account.");
    }

    @Override
    public void evaluateEvent(GetMyOfflineGamesEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle getting my offline games event.");
    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the local player joining a lobby.");
    }

    @Override
    public void evaluateEvent(LoginEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the local player logging in.");
    }

    @Override
    public void evaluateEvent(LogoutEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the local player logging out.");
    }

    @Override
    public void evaluateEvent(ReconnectToGameEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the local player reconnecting to a game.");
    }

    @Override
    public void evaluateEvent(RegisterEvent event) throws IllegalStateException {
        throw new IllegalStateException("In game state can't handle the local player registering.");
    }

    @Override
    public void evaluateEvent(ServerCrashedEvent event) throws IllegalStateException {

    }
}
