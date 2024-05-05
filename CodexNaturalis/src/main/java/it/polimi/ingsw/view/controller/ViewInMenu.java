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
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.View;

import java.lang.*;
import java.util.List;

public class ViewInMenu implements ViewState{
    private View view;

    public ViewInMenu(View view){
        this.view = view;
    }

    @Override
    public void evaluateEvent(ChooseCardsSetupEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle choosing cards setup.");
    }

    @Override
    public void evaluateEvent(ChooseTokenSetupEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle choosing token setup.");
    }

    @Override
    public void evaluateEvent(InvalidEvent event) {

    }

    @Override
    public void evaluateEvent(KickedPlayerFromLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle a player kicked from lobby.");
    }

    @Override
    public void evaluateEvent(UpdateLobbyPlayersEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle updating lobby.");
    }

    @Override
    public void evaluateEvent(UpdateGamePlayersEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle updating players' local games.");
    }

    @Override
    public void evaluateEvent(UpdateLocalModelEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle updating local game model.");
    }

    @Override
    public void evaluateEvent(AvailablePositionsEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle viewing the local available card positions.");
    }

    @Override
    public void evaluateEvent(IsMyTurnEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle checking if it's the player's turn.");
    }

    @Override
    public void evaluateEvent(SeeOpponentPlayAreaEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle seeing the opponent's play area.");
    }

    @Override
    public void evaluateEvent(ChosenCardsSetupEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle the local player's cards-setup choice.");
    }

    @Override
    public void evaluateEvent(ChosenTokenSetupEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle the local player's token-setup choice.");
    }

    @Override
    public void evaluateEvent(DrawCardEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle the local player drawing a card.");
    }

    @Override
    public void evaluateEvent(PlaceCardEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle the local player placing a card.");
    }

    @Override
    public void evaluateEvent(QuitGameEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle the local player quitting the game.");

    }

    @Override
    public void evaluateEvent(KickFromLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle kicking a chosen player from the lobby.");
    }

    @Override
    public void evaluateEvent(PlayerReadyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle the local player being ready.");
    }

    @Override
    public void evaluateEvent(PlayerUnreadyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle the local player being unready.");
    }

    @Override
    public void evaluateEvent(QuitLobbyEvent event) throws IllegalStateException {
        throw new IllegalStateException("In menu state can't handle the local player quitting the lobby.");
    }

    @Override
    public void evaluateEvent(AvailableLobbiesEvent event) throws IllegalStateException{
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        List<LobbyState> availableLobbies = event.getLobbies();
        view.displayAvailableLobbies(feedback, message, availableLobbies);
    }

    @Override
    public void evaluateEvent(CreateLobbyEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        String lobbyID = event.getLobbyID();
        int requiredPlayers = event.getRequiredPlayers();
        view.notifyCreatedLobby(feedback, message, lobbyID, requiredPlayers);
    }

    @Override
    public void evaluateEvent(DeleteAccountEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        view.notifyDeleteAccount(feedback, message);
    }

    @Override
    public void evaluateEvent(GetMyOfflineGamesEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        List<LobbyState> offlineGames = event.getGames();
        view.displayOfflineGames(feedback, message, offlineGames);
    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        String lobbyID = event.getLobbyID();
        List<Pair<String, Boolean>> playersReadyStatus = event.getReadyStatusPlayers();
        view.displayJoinedLobby(feedback, message, lobbyID, playersReadyStatus);
    }

    @Override
    public void evaluateEvent(LoginEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        Account account = event.getAccount();
        view.notifyLogin(feedback, message, account);
    }

    @Override
    public void evaluateEvent(LogoutEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        view.notifyLogout(feedback, message);
    }

    @Override
    public void evaluateEvent(ReconnectToGameEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        view.notifyReconnectToGame(feedback, message);
    }

    @Override
    public void evaluateEvent(RegisterEvent event) throws IllegalStateException {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        Account account = event.getAccount();
        view.notifyRegisterAccount(feedback, message, account);
    }

    @Override
    public void evaluateEvent(ServerCrashedEvent event) throws IllegalStateException {

    }
}
