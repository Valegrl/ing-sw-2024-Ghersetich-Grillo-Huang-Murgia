package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.InvalidEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.Pair;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.logging.Level;

public class VirtualView {
    private GameListener listener;
    private Controller controller;
    private GameController gameController;
    private boolean disconnected;
    private final Queue<Event> eventQueue = new LinkedList<>();
    private static final Logger logger = Logger.getLogger("VirtualView");

    //TODO: check not null parameters

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

    public void handle(Event event){
        synchronized (eventQueue){
            eventQueue.add(event);
            eventQueue.notifyAll();
        }
    }

    public void evaluateEvent(Event event){
        logger.log(Level.SEVERE, "Event not recognized.");
        listener.update(new InvalidEvent());
    }

    public void evaluateEvent(ChosenCardsSetupEvent event){
        if(gameController != null)
            listener.update(gameController.chosenCardsSetup(this, event.getObjectiveCardID(), event.getFlipStartCard()));
        else
            listener.update(new ChosenCardsSetupEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    public void evaluateEvent(ChosenTokenSetupEvent event){
        if(gameController != null)
            listener.update(gameController.chosenTokenSetup(this, event.getColor()));
        else
            listener.update(new ChosenTokenSetupEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    public void evaluateEvent(DrawCardEvent event){}

    public void evaluateEvent(PlaceCardEvent event){}

    public void evaluateEvent(QuitGameEvent event){
        if(gameController != null)
            listener.update(gameController.quitGame(this));
        else
            listener.update(new QuitLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    public void evaluateEvent(KickFromLobbyEvent event){
        if(gameController != null)
            listener.update(gameController.kickPlayer(this, event.getPlayerToKick()));
        else
            listener.update(new KickFromLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    public void evaluateEvent(PlayerReadyEvent event){
        if(gameController != null) {
            listener.update(gameController.readyToStart(this));
            if (!gameController.isGameStarted())
                gameController.startGame();
        }
        else
            listener.update(new KickFromLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    public void evaluateEvent(PlayerUnreadyEvent event){
        if(gameController != null)
            listener.update(gameController.unReadyToStart(this));
        else
            listener.update(new KickFromLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    public void evaluateEvent(QuitLobbyEvent event){
        if(gameController != null)
            listener.update(gameController.quitLobby(this));
        else
            listener.update(new QuitLobbyEvent(Feedback.FAILURE, "An unexpected action occurred."));
    }

    public void evaluateEvent(AvailableLobbiesEvent event){
        listener.update(controller.getLobbiesAvailable(this));
    }

    public void evaluateEvent(CreateLobbyEvent event){
        Pair<CreateLobbyEvent, GameController> response;
        response = controller.createLobby(this, listener, event.getLobbyID(), event.getRequiredPlayers());
        gameController = response.value();
        listener.update(response.key());
    }

    public void evaluateEvent(DeleteAccountEvent event){
        listener.update(controller.deleteAccount(this));
    }

    public void evaluateEvent(GetMyOfflineGamesEvent event){
        listener.update(controller.getMyOfflineGamesAvailable(this));
    }

    public void evaluateEvent(JoinLobbyEvent event){
        Pair<JoinLobbyEvent, GameController> response = controller.joinLobby(this, listener, event.getLobbyID());
        gameController = response.value();
        listener.update(response.key());
    }

    public void evaluateEvent(LoginEvent event){
        Account account = event.getAccount();
        LoginEvent response = controller.login(this, account);
        listener.update(response);
    }

    public void evaluateEvent(LogoutEvent event){
        listener.update(controller.logout(this));
    }

    public void evaluateEvent(ReconnectToGameEvent event){
        Pair<ReconnectToGameEvent, GameController> response;
        response = controller.reconnectToGame(this, listener, event.getGameID());
        gameController = response.value();
        listener.update(response.key());
    }

    public void evaluateEvent(RegisterEvent event){
        Account account = event.getAccount();
        listener.update(controller.register(account));
    }

    public void evaluateEvent(ClientDisconnectedEvent event){
        disconnected = true;
        controller.clientDisconnected(this);
    }
}
