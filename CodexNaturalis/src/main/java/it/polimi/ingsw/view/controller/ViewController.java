package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.InvalidEvent;
import it.polimi.ingsw.eventUtils.event.fromController.KickedPlayerFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromController.UpdateLobbyPlayersEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.PlayerIsChoosingSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.UpdateLocalModelEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
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
import it.polimi.ingsw.immutableModel.ImmPlayer;
import it.polimi.ingsw.immutableModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.immutableModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.View;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map;

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
     * The queue of events that are received and need to be processed.
     */
    private final Queue<Event> tasksQueue = new LinkedList<>();

    /*Immutable model part*/
    /**
     * The unique identifier of the game.
     */
    private String id;

    /**
     * The list of players in the game.
     */
    private List<ImmPlayer> player;

    /**
     * The index of the player whose turn it is.
     */
    private int turnPlayerIndex;

    /**
     * The scoreboard of the game.
     */
    private Map<ImmPlayer, Integer> scoreboard;

    /**
     * The common objectives of the game.
     */
    private ImmObjectiveCard[] commonObjectives;

    /**
     * The current status of the game.
     */
    private GameStatus gameStatus;

    /**
     * The visible resource cards in the game.
     */
    private ImmPlayableCard[] visibleResourceCards;

    /**
     * The visible gold cards in the game.
     */
    private ImmPlayableCard[] visibleGoldCards;

    /**
     * The top card of the resource deck.
     */
    private Item topResourceDeck;

    /**
     * The top card of the gold deck.
     */
    private Item topGoldDeck;


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


        new Thread(){
            @Override
            public void run(){
                while (true){
                    synchronized (tasksQueue){
                        while(tasksQueue.isEmpty()){
                            try{
                                tasksQueue.wait();
                            }
                            catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        Event event = tasksQueue.poll();
                        event.receiveEvent(ViewController.this);
                    }
                }
            }
        }.start();
    }

    /**
     * Handles a new event from the view, which can be sent to the server or processed locally.
     *
     * @param event The new event from the view.
     */
    public void newViewEvent(Event event) {
        if(EventID.isLocal(event.getID())){
            synchronized (tasksQueue) {
                tasksQueue.add(event);
            }
        }
        else{
            try {
                clientManager.handleEvent(event);
            }
            catch(RemoteException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles an external event coming from the server.
     *
     * @param event The external event.
     */
    public void externalEvent(Event event){
        synchronized (tasksQueue){
            tasksQueue.add(event);
            tasksQueue.notifyAll();
        }
    }

    //TODO depending on the events arriving (model) I might have the entire model or one update, I must copy it and
    //TODO set it in the local semi-immutable model
    //TODO add methods on the View interface
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
        List<LobbyState> availableLobbies = event.getLobbies();
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();

        view.displayAvailableLobbies(feedback, message, availableLobbies);
    }

    @Override
    public void evaluateEvent(CreateLobbyEvent event) {
        //TODO deep copy for pair ?
        String id = null;
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        if(feedback.equals(Feedback.SUCCESS)) {
            id = event.getLobbyID();
        }
        view.notifyCreatedLobby(feedback, message, event.getLobbyID(), event.getRequiredPlayers());
    }

    @Override
    public void evaluateEvent(DeleteAccountEvent event) {
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        //TODO do something with the immutable model when feedback is success ?
        view.notifyDeleteAccount(feedback, message);
    }

    @Override
    public void evaluateEvent(GetMyOfflineGamesEvent event) {
        List<LobbyState> offlineGames = event.getGames();
        Feedback feedback = event.getFeedback();
        String message = event.getMessage();
        view.displayOfflineGames(feedback, message, offlineGames);
    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) {
        Feedback feedback = event.getFeedback();
        if(feedback.equals(Feedback.SUCCESS)) {
            id = event.getLobbyID();
        }
        List<Pair<String, Boolean>> playersReadyStatus = event.getReadyStatusPlayers();
        view.displayJoinedLobby(id, playersReadyStatus);
    }

    @Override
    public void evaluateEvent(LoginEvent event) {
        Account account = event.getAccount();
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
