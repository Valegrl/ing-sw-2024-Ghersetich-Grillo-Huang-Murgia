package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.EventID;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The ViewController class is responsible for handling events from the view and forwarding them to the {@link ClientManager}.
 * It also receives events from the {@link ClientManager} and forwards them to the view to be processed.
 * The class contains a list of event IDs that should be ignored and not forwarded as they can be processed locally.
 * The ViewController class uses two queues to manage events coming in and out:
 * - The first thread is responsible for taking events from the outQueue and sending them to the {@link ClientManager}.
 * - The second thread is responsible for taking events from the inQueue and evaluating them.
 */
public class ViewController implements ViewEventReceiver {

    /**
     * The view that this controller is managing.
     */
    private final View view;

    /**
     * The client manager that the view controller is using together to send and handle events.
     */
    private final ClientManager clientManager;

    /**
     * The queue of events that are to be sent out.
     */
    private final Queue<Event> outQueue = new LinkedList<>();

    /**
     * The queue of events that are received and need to be processed.
     */
    private final Queue<Event> inQueue = new LinkedList<>();

    /**
     * The constructor for the ViewController class.
     *
     * @param view The view this controller is associated with.
     */
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
                        evaluateEvent(event);
                    }
                }
            }
        }.start();
    }

    /**
     * Handles a new event from the view.
     *
     * @param event The new event from the view.
     */
    public void newViewEvent(Event event) {
        //TODO: filter
        if(EventID.isLocal(event.getID())){
            this.evaluateEvent(event);
        }
        else{
            synchronized (outQueue){
                outQueue.add(event);
                outQueue.notifyAll();
            }
        }
    }

    /**
     * Handles an external event coming from the server.
     *
     * @param event The external event.
     */
    public void externalEvent(Event event){
        synchronized (inQueue){
            inQueue.add(event);
            inQueue.notifyAll();
        }
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

    /**
     * This method handles a server crash.
     */
    public void serverCrashed () {
        view.serverCrashed();
    }
}
