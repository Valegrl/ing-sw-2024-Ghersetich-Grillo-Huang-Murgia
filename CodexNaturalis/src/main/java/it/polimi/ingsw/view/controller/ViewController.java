package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.InvalidEvent;
import it.polimi.ingsw.eventUtils.event.fromController.KickedPlayerFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromController.UpdateLobbyPlayersEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.PlayerIsChoosingSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.UpdateLocalModelEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChooseSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.DrawCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.PlaceCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.AvailablePositionsEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.IsMyTurnEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.SeeOpponentPlayAreaEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.View;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public class ViewController implements ViewEventReceiver {

    private final View view;

    private final ClientManager clientManager;

    private final Queue<Event> outQueue = new LinkedList<>();

    private final Queue<Event> inQueue = new LinkedList<>();

    public ViewController(View view) {
        this.view = view;
        try {
            this.clientManager = ClientManager.getInstance();
            clientManager.setViewController(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    synchronized (outQueue){
                        while(outQueue.isEmpty()){
                            try{
                                outQueue.wait();
                            }
                            catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        Event event = outQueue.poll();
                        try{
                            clientManager.handleEvent(event);
                        }
                        catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                while (true){
                    synchronized (inQueue){
                        while(inQueue.isEmpty()){
                            try{
                                inQueue.wait();
                            }
                            catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        Event event = inQueue.poll();
                        //Something, view has to handle the event
                    }
                }
            }
        }.start();
    }

    public void newViewEvent(Event event) {
        //TODO: filter
    }

    public void externalEvent(Event event){

    }

    @Override
    public void evaluateEvent(InvalidEvent event) {

    }

    @Override
    public void evaluateEvent(KickedPlayerFromLobbyEvent event) {

    }

    @Override
    public void evaluateEvent(UpdateLobbyPlayersEvent event) {

    }

    @Override
    public void evaluateEvent(PlayerIsChoosingSetupEvent event) {

    }

    @Override
    public void evaluateEvent(UpdateLocalModelEvent event) {

    }

    @Override
    public void evaluateEvent(AvailablePositionsEvent event) {

    }

    @Override
    public void evaluateEvent(IsMyTurnEvent event) {

    }

    @Override
    public void evaluateEvent(SeeOpponentPlayAreaEvent event) {

    }

    @Override
    public void evaluateEvent(ChooseSetupEvent event) {

    }

    @Override
    public void evaluateEvent(DrawCardEvent event) {

    }

    @Override
    public void evaluateEvent(PlaceCardEvent event) {

    }

    @Override
    public void evaluateEvent(QuitGameEvent event) {

    }

    @Override
    public void evaluateEvent(KickFromLobbyEvent event) {

    }

    @Override
    public void evaluateEvent(PlayerReadyEvent event) {

    }

    @Override
    public void evaluateEvent(PlayerUnreadyEvent event) {

    }

    @Override
    public void evaluateEvent(QuitLobbyEvent event) {

    }

    @Override
    public void evaluateEvent(AvailableLobbiesEvent event) {

    }

    @Override
    public void evaluateEvent(CreateLobbyEvent event) {

    }

    @Override
    public void evaluateEvent(DeleteAccountEvent event) {

    }

    @Override
    public void evaluateEvent(GetMyOfflineGamesEvent event) {

    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) {

    }

    @Override
    public void evaluateEvent(LoginEvent event) {

    }

    @Override
    public void evaluateEvent(LogoutEvent event) {

    }

    @Override
    public void evaluateEvent(ReconnectToGameEvent event) {

    }

    @Override
    public void evaluateEvent(RegisterEvent event) {

    }

    public void serverCrashed () {
        view.serverCrashed();
    }
}
