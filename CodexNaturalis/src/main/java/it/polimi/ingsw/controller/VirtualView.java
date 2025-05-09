package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.InvalidEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.*;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
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
@SuppressWarnings("all")
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
                ChosenCardsSetupEvent response = gameController.chosenCardsSetup(this, CardID, flipStartCard);
                listener.update(response);
                if (response.getFeedback() == Feedback.SUCCESS)
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
                ChosenTokenSetupEvent response = gameController.chosenTokenSetup(this, token);
                listener.update(response);
                if (response.getFeedback() == Feedback.SUCCESS)
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
            if (cardType != null) {
                DrawCardEvent response = gameController.drawCard(this, cardType, event.getIndex());
                if (response.getFeedback() == Feedback.FAILURE)
                    listener.update(response);
            } else
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
            if (cardID != null && pos != null) {
                PlaceCardEvent response = gameController.placeCard(this, cardID, pos, event.isFlipped());
                if (response.getFeedback() == Feedback.FAILURE)
                    listener.update(response);
            } else
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
        if(gameController != null) {
            listener.update(gameController.quitGame(this));
        } else
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
            if(response.key().getFeedback().equals(Feedback.SUCCESS)) {
                logger.log(Level.INFO, "New lobby " + event.getLobbyID() + " created.");
            }
        } else
            listener.update(new CreateLobbyEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a {@link DeleteAccountEvent} and forwards it to the appropriate controller.
     *
     * @param event The DeleteAccountEvent to be evaluated.
     */
    public void evaluateEvent(DeleteAccountEvent event) {
        String username = controller.getVirtualViewUsername(this);
        DeleteAccountEvent deleteAccountEvent = controller.deleteAccount(this);
        listener.update(deleteAccountEvent);
        if (deleteAccountEvent.getFeedback().equals(Feedback.SUCCESS)) {
            logger.log(Level.INFO, "Account " + username + " deleted.");
        }
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
            if (response.key().getFeedback().equals(Feedback.SUCCESS)) {
                logger.log(Level.INFO, "User " + controller.getVirtualViewUsername(this) + " joined " + event.getLobbyID() + " lobby.");
            }
        } else
            listener.update(new JoinLobbyEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
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
            if (response.getFeedback().equals(Feedback.SUCCESS)) {
                logger.log(Level.INFO, "User " + controller.getVirtualViewUsername(this) + " logged in.");
            }
        } else
            listener.update(new LoginEvent(Feedback.FAILURE, null, "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a {@link LogoutEvent} and forwards it to the appropriate controller.
     *
     * @param event The LogoutEvent to be evaluated.
     */
    public void evaluateEvent(LogoutEvent event) {
        String username = controller.getVirtualViewUsername(this);
        LogoutEvent response = controller.logout(this);
        listener.update(response);
        if (response.getFeedback().equals(Feedback.SUCCESS)) {
            logger.log(Level.INFO, "User " + username + " logged out.");
        }
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
            if(response.key().getFeedback() == Feedback.FAILURE)
                listener.update(response.key());
            else
                logger.log(Level.INFO, "User " + controller.getVirtualViewUsername(this) + " reconnected to " + event.getGameID() + " game.");
        } else
            listener.update(new ReconnectToGameEvent(Feedback.FAILURE, null, "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a {@link RegisterEvent} and forwards it to the appropriate controller.
     *
     * @param event The RegisterEvent to be evaluated.
     */
    public void evaluateEvent(RegisterEvent event){
        Account account = event.getAccount();
        if (account != null) {
            RegisterEvent response = controller.register(account);
            listener.update(response);
            if (response.getFeedback().equals(Feedback.SUCCESS)) {
                logger.log(Level.INFO, "New user " + account.getUsername() + " registered.");
            }
        } else
            listener.update(new RegisterEvent(Feedback.FAILURE, "Some of the event's parameters are null."));
    }

    /**
     * Evaluates a ChatGMEvent and forwards it to the GameController.
     *
     * @param event The ChatGMEvent to be evaluated.
     */
    public void evaluateEvent(ChatGMEvent event){
        if(gameController != null) {
            ChatMessage chatMessage = event.getChatMessage();
            if (chatMessage != null) {
                String text = chatMessage.getMessage();
                LocalTime time = chatMessage.getTime();
                UUID id = chatMessage.getMessage_id();
                if (text != null && time != null && id != null)
                    listener.update(gameController.chatGlobalMessage(this, chatMessage));
                else
                    listener.update(new ChatGMEvent(Feedback.FAILURE, "Some information in the message is null.", chatMessage));
            } else
                listener.update(new ChatGMEvent(Feedback.FAILURE, "Message is null.", null));
        } else {
            listener.update(new ChatGMEvent(Feedback.FAILURE, "An unexpected action occurred.", null));
        }
    }

    /**
     * Evaluates a ChatPMEvent and forwards it to the GameController.
     *
     * @param event The ChatPMEvent to be evaluated.
     */
    public void evaluateEvent(ChatPMEvent event){
        if(gameController != null) {
            PrivateChatMessage pChatMessage = event.getChatMessage();
            if (pChatMessage != null) {
                String text = pChatMessage.getMessage();
                LocalTime time = pChatMessage.getTime();
                UUID id = pChatMessage.getMessage_id();
                String recipient = pChatMessage.getRecipient();
                if (text != null && time != null && id != null && recipient != null)
                    listener.update(gameController.chatPrivateMessage(this, pChatMessage));
                else
                    listener.update(new ChatPMEvent(Feedback.FAILURE, "Some information in the message is null.", pChatMessage));
            } else
                listener.update(new ChatPMEvent(Feedback.FAILURE, "Recipient or message is null.", null));
        } else {
            listener.update(new ChatPMEvent(Feedback.FAILURE, "An unexpected action occurred.", null));
        }
    }

    /**
     * Evaluates a {@link ClientDisconnectedEvent} and forwards it to the appropriate controller.
     *
     * @param event The ClientDisconnectedEvent to be evaluated.
     */
    public void evaluateEvent(ClientDisconnectedEvent event){
        disconnected = true;
        String message;
        String username =  controller.getVirtualViewUsername(this);
        controller.clientDisconnected(this);
        if(!username.isEmpty())
            message = "Client " + username + " disconnected.";
        else message = "Unlogged client disconnected";
        logger.log(Level.INFO, message);
    }
}
