package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.InvalidEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.utils.Pair;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.logging.Level;

public class VirtualView {
    private GameListener listener;
    private Controller controller;
    private GameController gameController;
    private Pair<String, String> account;
    private boolean disconnected;
    private final Queue<Event> eventQueue = new LinkedList<>();
    private static final Logger logger = Logger.getLogger("VirtualView");

    public VirtualView(GameListener gl) {
        this.listener = gl;
        this.controller = Controller.getInstance();
        this.disconnected = false;
        this.account = null;

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

    public void evaluateEvent(ChooseSetupEvent event){}

    public void evaluateEvent(DrawCardEvent event){}

    public void evaluateEvent(PlaceCardEvent event){}

    public void evaluateEvent(QuitGameEvent event){
        if(gameController!=null)
            listener.update(gameController.quitGame(this));
        else
            listener.update(new QuitLobbyEvent(Feedback.FAILURE, "Unexpected event."));
    }

    public void evaluateEvent(KickFromLobbyEvent event){}

    public void evaluateEvent(PlayerReadyEvent event){}

    public void evaluateEvent(PlayerUnreadyEvent event){}

    public void evaluateEvent(QuitLobbyEvent event){
        if(gameController!=null)
            listener.update(gameController.quitLobby(this));
        else
            listener.update(new QuitLobbyEvent(Feedback.FAILURE, "Unexpected event."));
    }

    public void evaluateEvent(AvailableLobbiesEvent event){
        listener.update(controller.getLobbiesAvailable());
    }

    public void evaluateEvent(CreateLobbyEvent event){
        Pair<String, Integer> data = event.getData();
        Pair<CreateLobbyEvent, GameController> response = controller.createLobby(this, listener, data.key(), data.value());
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
        Pair<JoinLobbyEvent, GameController> response = controller.joinLobby(this, listener, event.getData().key());
        gameController = response.value();
        listener.update(response.key());
    }

    public void evaluateEvent(LoginEvent event){
        Pair<String, String> account = event.getData();
        LoginEvent response = controller.login(this, account);
        if (response.getFeedback() == Feedback.SUCCESS)
            this.account = account;
        listener.update(response);
    }

    public void evaluateEvent(LogoutEvent event){
        listener.update(controller.logout(this));
    }

    public void evaluateEvent(ReconnectToGameEvent event){
        Pair<ReconnectToGameEvent, GameController> response = controller.reconnectToGame(this, listener, event.getData());
        gameController = response.value();
        listener.update(response.key());
    }

    public void evaluateEvent(RegisterEvent event){
        Pair<String, String> account = event.getData();
        listener.update(controller.register(account));
    }

    public void evaluateEvent(ClientDisconnectedEvent event){
        disconnected = true;
        controller.clientDisconnected(this);
    }
}
