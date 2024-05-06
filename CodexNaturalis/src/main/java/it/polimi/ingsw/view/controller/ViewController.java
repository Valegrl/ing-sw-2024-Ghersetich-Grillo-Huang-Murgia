package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.UpdateLocalModelEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ServerCrashedEvent;
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

    /**
     * The state in which the view is.
     */
    private ViewState viewState;

    /**
     * The account associated with the View.
     */
    private String username;

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
        this.viewState = new ViewInMenu(view);

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
                    Event event;
                    synchronized (tasksQueue){
                        while(tasksQueue.isEmpty()){
                            try{
                                tasksQueue.wait();
                            }
                            catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        event = tasksQueue.poll();
                    }
                    event.receiveEvent(ViewController.this);
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
                tasksQueue.notifyAll();
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

    @Override
    public void evaluateEvent(ChooseCardsSetupEvent event) {

    }

    @Override
    public void evaluateEvent(ChooseTokenSetupEvent event) {

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
    public void evaluateEvent(UpdateGamePlayersEvent event) {

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
    public void evaluateEvent(ChosenCardsSetupEvent event) {

    }

    @Override
    public void evaluateEvent(ChosenTokenSetupEvent event) {

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

    /*Events done on 4/05/2024*/
    //TODO (for all the evaluateEvent, do something with the immutable model when feedback is success ?
    //TODO (for all the evaluateEvent, check if incoming event is relevant to the player's phase (in-game, in-lobby, out-game, out-lobby)
    //TODO should i print for IllegalStateException?
    @Override
    public void evaluateEvent(KickFromLobbyEvent event) {
        try{
            viewState.evaluateEvent(event);
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(PlayerReadyEvent event) {
        //TODO change state from Lobby to game ?
        try{
            viewState.evaluateEvent(event);
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(PlayerUnreadyEvent event) {

    }

    @Override
    public void evaluateEvent(QuitLobbyEvent event) {

    }

    @Override
    public void evaluateEvent(AvailableLobbiesEvent event) {
        try{
            viewState.evaluateEvent(event);
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(CreateLobbyEvent event) {
        try{
            viewState.evaluateEvent(event);
            if(event.getFeedback() == Feedback.SUCCESS) {
                id = event.getLobbyID();
                viewState = new ViewInLobby(view);
            }
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(DeleteAccountEvent event) {

        try{
            viewState.evaluateEvent(event);
            if(event.getFeedback().equals(Feedback.SUCCESS)) {
                this.username = null;
            }
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(GetMyOfflineGamesEvent event) {
        try{
            viewState.evaluateEvent(event);
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) {

        //TODO initialise immutable model ?
        try{
            viewState.evaluateEvent(event);
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                id = event.getLobbyID();
                viewState = new ViewInLobby(view);
            }
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(LoginEvent event) {
        try{
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                this.username = event.getUsername();
            }
            viewState.evaluateEvent(event);
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(LogoutEvent event) {
        try{
            viewState.evaluateEvent(event);
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                this.username = null;
            }
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(ReconnectToGameEvent event) {
        //TODO Reinitialise the local model ? Connect directly to the game ?
        try{
            viewState.evaluateEvent(event);
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                id = event.getGameID();
                viewState = new ViewInGame(view);
            }
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(RegisterEvent event) {
        try{
            viewState.evaluateEvent(event);
        }
        catch(IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void evaluateEvent(ServerCrashedEvent event) {

    }

    /**
     * This method handles a server crash. //TODO: use event. Delete this method.
     */
    public void serverCrashed () {
        username = null;

        view.serverCrashed();
    }
}
