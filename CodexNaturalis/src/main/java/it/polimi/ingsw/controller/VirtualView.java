package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.InvalidEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The {@link VirtualView} class serves as an intermediary between the view and the controller in the MVC architecture.
 * It is responsible for handling and processing {@link Event} instances received from the view and forwarding them to
 * the appropriate controller for further action.
 * <p>
 * It maintains a queue of events to be processed, ensuring that all incoming events are handled in the order they are received.
 * The class also manages the connection status of the associated client. It keeps track of whether the client is
 * connected or disconnected. Each specific type of event is evaluated by a corresponding method in this class, which
 * then forwards the event to the appropriate controller.
 * The {@link GameListener} associated with this VirtualView receives updates about the game state, which are then
 * forwarded to the client.
 */
public class VirtualView {

    /**
     * The {@link GameListener} that receives updates from the VirtualView.
     */
    private final GameListener listener;

    /**
     * The main {@link Controller} that handles events.
     */
    private final Controller controller;

    /**
     * The {@link GameController} that handles game-specific events.
     */
    private GameController gameController;

    /**
     * A flag indicating the connection status of the client associated with the VirtualView.
     * If true, the client is disconnected. If false, the client is connected.
     */
    private boolean disconnected;

    /**
     * A queue of {@link Event} instances to be processed.
     */
    private final Queue<Event> eventQueue = new LinkedList<>();

    /**
     * Logger for logging information and errors.
     */
    private static final Logger logger = Logger.getLogger("VirtualView");


    /**
     * Constructor for the VirtualView class.
     *
     * @param gl The {@link GameListener} that will receive updates from the VirtualView.
     */
    public VirtualView(GameListener gl) {
        this.listener = gl;
        this.controller = Controller.getInstance();
        this.disconnected = false;

        new Thread(() -> {
            while (!disconnected) {
                Event eventToManage;
                synchronized (eventQueue) {
                    while (eventQueue.isEmpty()) {
                        try {
                            eventQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    eventToManage = eventQueue.poll();
                }
                eventToManage.receiveEvent(this);
            }
            logger.log(Level.INFO, "Disconnected.");
        }).start();
    }

    /**
     * Adds an {@link Event} to the event queue.
     *
     * @param event The event to be added to the queue.
     */
    public void handle(Event event){
        synchronized (eventQueue){
            eventQueue.add(event);
            eventQueue.notifyAll();
        }
    }

    /**
     * Evaluates an {@link Event} and forwards it to the appropriate controller.
     *
     * @param event The event to be evaluated.
     */
    public void evaluateEvent(Event event){
        logger.log(Level.SEVERE, "Event not recognized.");
        listener.update(new InvalidEvent());
    }
    //TODO check double methods (only if SUCCESS) + synchronized
    /**
     * Evaluates a {@link ChosenCardsSetupEvent} and forwards it to the appropriate controller.
     *
     * @param event The ChosenCardsSetupEvent to be evaluated.
     */
    public void evaluateEvent(ChosenCardsSetupEvent event){
        if(gameController != null) {
            String CardID = event.getObjectiveCardID();
            Boolean flipStartCard = event.getFlipStartCard();
            if (CardID != null && flipStartCard != null) {
                listener.update(gameController.chosenCardsSetup(this, CardID, flipStartCard));
                gameController.startTokenSetup();
            } else
                listener.update(new ChosenCardsSetupEvent(Feedback.FAILURE, "Some parameters of the event are null."));
        } else
            listener.update(new ChosenCardsSetupEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates a {@link ChosenTokenSetupEvent} and forwards it to the appropriate controller.
     *
     * @param event The ChosenTokenSetupEvent to be evaluated.
     */
    public void evaluateEvent(ChosenTokenSetupEvent event){
        if(gameController != null) {
            Token token = event.getColor();
            if (token != null) {
                listener.update(gameController.chosenTokenSetup(this, token));
                gameController.startRunning();
            } else
                listener.update(new ChosenCardsSetupEvent(Feedback.FAILURE, "Some parameters of the event are null."));
        } else
            listener.update(new ChosenTokenSetupEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates a {@link DrawCardEvent} and forwards it to the appropriate controller.
     *
     * @param event The DrawCardEvent to be evaluated.
     */
    public void evaluateEvent(DrawCardEvent event){
        if(gameController != null) {
            CardType cardType = event.getCardType();
            if (cardType != null)
                listener.update(gameController.drawCard(this, cardType, event.getIndex()));
            else
                listener.update(new DrawCardEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
        }else
            listener.update(new DrawCardEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates a {@link PlaceCardEvent} and forwards it to the appropriate controller.
     *
     * @param event The PlaceCardEvent to be evaluated.
     */
    public void evaluateEvent(PlaceCardEvent event){
        if(gameController != null) {
            String cardID = event.getCardID();
            Coordinate pos = event.getPos();
            if (cardID != null && pos != null)
                listener.update(gameController.placeCard(this, cardID, pos, event.isFlipped()));
            else
                listener.update(new PlaceCardEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
        } else
            listener.update(new PlaceCardEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates a {@link QuitGameEvent} and forwards it to the appropriate controller.
     *
     * @param event The QuitGameEvent to be evaluated.
     */
    public void evaluateEvent(QuitGameEvent event){
        if(gameController != null)
            listener.update(gameController.quitGame(this));
        else
            listener.update(new QuitLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates a {@link KickFromLobbyEvent} and forwards it to the appropriate controller.
     *
     * @param event The KickFromLobbyEvent to be evaluated.
     */
    public void evaluateEvent(KickFromLobbyEvent event){
        if(gameController != null) {
            String playerToKick = event.getPlayerToKick();
            if (playerToKick != null)
                listener.update(gameController.kickPlayer(this, playerToKick));
            else
                listener.update(new KickFromLobbyEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
        } else
            listener.update(new KickFromLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates a {@link PlayerReadyEvent} and forwards it to the appropriate controller.
     *
     * @param event The PlayerReadyEvent to be evaluated.
     */
    public void evaluateEvent(PlayerReadyEvent event){
        if(gameController != null) {
            listener.update(gameController.readyToStart(this));
            gameController.startCardsSetup();
        }
        else
            listener.update(new KickFromLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates a {@link PlayerUnreadyEvent} and forwards it to the appropriate controller.
     *
     * @param event The PlayerUnreadyEvent to be evaluated.
     */
    public void evaluateEvent(PlayerUnreadyEvent event){
        if(gameController != null)
            listener.update(gameController.unReadyToStart(this));
        else
            listener.update(new KickFromLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates a {@link QuitLobbyEvent} and forwards it to the appropriate controller.
     *
     * @param event The QuitLobbyEvent to be evaluated.
     */
    public void evaluateEvent(QuitLobbyEvent event){
        if(gameController != null)
            listener.update(gameController.quitLobby(this));
        else
            listener.update(new QuitLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    /**
     * Evaluates an {@link AvailableLobbiesEvent} and forwards it to the appropriate controller.
     *
     * @param event The AvailableLobbiesEvent to be evaluated.
     */
    public void evaluateEvent(AvailableLobbiesEvent event){
        listener.update(controller.getLobbiesAvailable(this));
    }

    /**
     * Evaluates a {@link CreateLobbyEvent} and forwards it to the appropriate controller.
     *
     * @param event The CreateLobbyEvent to be evaluated.
     */
    public void evaluateEvent(CreateLobbyEvent event){
        String lobbyID = event.getLobbyID();
        if (lobbyID != null) {
            Pair<CreateLobbyEvent, GameController> response;
            response = controller.createLobby(this, listener, lobbyID, event.getRequiredPlayers());
            gameController = response.value();
            listener.update(response.key());
        } else
            listener.update(new CreateLobbyEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a {@link DeleteAccountEvent} and forwards it to the appropriate controller.
     *
     * @param event The DeleteAccountEvent to be evaluated.
     */
    public void evaluateEvent(DeleteAccountEvent event){
        listener.update(controller.deleteAccount(this));
    }

    /**
     * Evaluates a {@link GetMyOfflineGamesEvent} and forwards it to the appropriate controller.
     *
     * @param event The GetMyOfflineGamesEvent to be evaluated.
     */
    public void evaluateEvent(GetMyOfflineGamesEvent event){
        listener.update(controller.getMyOfflineGamesAvailable(this));
    }

    /**
     * Evaluates a {@link JoinLobbyEvent} and forwards it to the appropriate controller.
     *
     * @param event The JoinLobbyEvent to be evaluated.
     */
    public void evaluateEvent(JoinLobbyEvent event){
        String lobbyID = event.getLobbyID();
        if (lobbyID != null) {
            Pair<JoinLobbyEvent, GameController> response = controller.joinLobby(this, listener, lobbyID);
            gameController = response.value();
            listener.update(response.key());
        } else
            listener.update(new JoinLobbyEvent(Feedback.FAILURE, new LinkedHashMap<>(), "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a {@link LoginEvent} and forwards it to the appropriate controller.
     *
     * @param event The LoginEvent to be evaluated.
     */
    public void evaluateEvent(LoginEvent event){
        Account account = event.getAccount();
        if (account != null) {
            LoginEvent response = controller.login(this, account);
            listener.update(response);
        } else
            listener.update(new LoginEvent(Feedback.FAILURE, null, "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a {@link LogoutEvent} and forwards it to the appropriate controller.
     *
     * @param event The LogoutEvent to be evaluated.
     */
    public void evaluateEvent(LogoutEvent event){
        listener.update(controller.logout(this));
    }

    /**
     * Evaluates a {@link ReconnectToGameEvent} and forwards it to the appropriate controller.
     *
     * @param event The ReconnectToGameEvent to be evaluated.
     */
    public void evaluateEvent(ReconnectToGameEvent event){
        String gameID = event.getGameID();
        if (gameID != null) {
            Pair<ReconnectToGameEvent, GameController> response;
            response = controller.reconnectToGame(this, listener, gameID);
            gameController = response.value();
            listener.update(response.key());
            // TODO update from model if SUCCESS
        } else
            listener.update(new ReconnectToGameEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a {@link RegisterEvent} and forwards it to the appropriate controller.
     *
     * @param event The RegisterEvent to be evaluated.
     */
    public void evaluateEvent(RegisterEvent event){
        Account account = event.getAccount();
        if (account != null)
            listener.update(controller.register(account));
        else
            listener.update(new RegisterEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a {@link ClientDisconnectedEvent} and forwards it to the appropriate controller.
     *
     * @param event The ClientDisconnectedEvent to be evaluated.
     */
    public void evaluateEvent(ClientDisconnectedEvent event){
        disconnected = true;
        controller.clientDisconnected(this);
    }
}
