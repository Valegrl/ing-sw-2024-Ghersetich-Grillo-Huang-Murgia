package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.ChooseCardsSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.EndedGameEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.UpdateLocalModelEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetLobbyInfoEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ServerDisconnectedEvent;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.ChatMessagesList;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.viewModel.ViewModel;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayArea;

import java.rmi.RemoteException;
import java.util.*;

/**
 * The ViewController class is responsible for handling events from the view and forwarding them to the {@link ClientManager}.
 * It also receives events from the {@link ClientManager} and forwards them to the view to be processed.
 * The class contains a model of the game in the client's local.
 * The class contains a list of event IDs that should be ignored and not forwarded as they can be processed locally.
 * The ViewController class uses a queue for managing the events coming from the server and the ones generated locally {@code tasksQueue}.
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
     * The flag that indicates if the task queue reader needs to be stopped.
     */
    private boolean taskQueueStopped = false;

    /**
     * The username of the logged-in user.
     */
    private String username;

    /**
     * The available lobbies.
     */
    private List<LobbyState> lobbies;

    /**
     * The offline games.
     */
    private List<LobbyState> offlineGames;

    /**
     * The map of players and their ready status in the lobby.
     */
    private Map<String, Boolean> playersStatus;

    /**
     * The id of the joined lobby.
     */
    private String lobbyId;

    /**
     * The list of global chat messages for the current game.
     * A maximum of 50 messages is stored.
     */
    private final ChatMessagesList<ChatMessage> chatMessages = new ChatMessagesList<>(50); // FIXME config

    /**
     * The list of private chat messages received by the user.
     * A maximum of 50 messages is stored.
     */
    private final ChatMessagesList<PrivateChatMessage> privateChatMessages = new ChatMessagesList<>(50); // FIXME config

    /**
     * The setup of the game.
     */
    private ViewStartSetup setup;

    /**
     * The {@link ViewModel} for the current game.
     */
    private ViewModel model;

    /**
     * The constructor for the ViewController class.
     *
     * @param view The view this controller is associated with.
     */
    public ViewController(View view) {
        this.view = view;
        this.playersStatus = new LinkedHashMap<>();
        try {
            this.clientManager = ClientManager.getInstance();
            clientManager.setViewController(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        new Thread(() -> {
            while (!taskQueueStopped) {
                Event event;
                synchronized (tasksQueue) {
                    while(tasksQueue.isEmpty()) {
                        try{
                            tasksQueue.wait();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    event = tasksQueue.poll();
                }
                event.receiveEvent(ViewController.this);
            }
        }).start();
    }

    /**
     * Handles a new event from the view, which can be sent to the server or processed locally.
     *
     * @param event The new event from the view.
     */
    public void newViewEvent(Event event) {

        if(EventID.isLocal(event.getID())) {
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
    public void externalEvent(Event event) {
        synchronized (tasksQueue) {
            tasksQueue.add(event);
            tasksQueue.notifyAll();
        }
    }

    //TODO review evaluateEvent methods ChooseCardsSetupEvent and ChooseTokenSetupEvent
    @Override
    public void evaluateEvent(ChooseTokenSetupEvent event) {
        if(view.getState().inGame()) {
            List<Token> availableColors = event.getAvailableColors();
            StringBuilder message = new StringBuilder(event.getMessage() + "\n");
            for(Token i : availableColors) {
                message.append(i.getColor()).append("\n");
            }
            view.handleResponse(event.getID(), null, message.toString());
        } else {
            System.out.println("Game state: event in wrong state");
        }

    }

    @Override
    public void evaluateEvent(InvalidEvent event) {
        view.handleResponse(event.getID(), null, null);
    }

    @Override
    public void evaluateEvent(KickedPlayerFromLobbyEvent event) {
        if(view.getState().inLobby()) {
            lobbyId = null;
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(UpdateLobbyPlayersEvent event) {
        if(view.getState().inLobby()) {
            playersStatus = event.getPlayers();
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Lobby state: event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(UpdateGamePlayersEvent event) {
        if(view.getState().inLobby() || view.getState().inGame()) {
            playersStatus = event.getPlayers();
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChooseCardsSetupEvent event) {
        if(view.getState().inGame()) {
            setup = event.getViewSetup();
            // TODO prepare message for the view
            StringBuilder message = new StringBuilder("Assigned setup:\n\n");
            message.append(setup.getStartCard().printSetupStartCard());
            view.handleResponse(event.getID(), null, message.toString());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(EndedGameEvent event) {

    }

    @Override
    public void evaluateEvent(UpdateLocalModelEvent event) {

    }

    @Override
    public void evaluateEvent(AvailablePositionsEvent event) {
        if(view.getState().inGame()) {
            SelfViewPlayArea selfVPA = this.model.getSelfPlayer().getPlayArea();
            StringBuilder message = new StringBuilder(selfVPA.printAvailablePos());
            view.handleResponse(event.getID(), null, message.toString());
        } else {
            System.out.println("Game state: event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(IsMyTurnEvent event) {
        if(view.getState().inGame()) {
            StringBuilder message = new StringBuilder();
            /*
            if(model.getThisPlayerIndex() == model.getTurnPlayerIndex()){
                message.append("It's your turn! \n");
                view.handleResponse(event.getID(), null, message.toString());
            }
            else{
                message.append("It's not your turn! \n");
                view.handleResponse(event.getID(), null, message.toString());
            }
            */

        } else {
            System.out.println("Game state: event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(SeeOpponentPlayAreaEvent event) {

    }

    @Override
    public void evaluateEvent(ChosenCardsSetupEvent event) {
        if(view.getState().inGame()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
            //TODO View has to handle seeing the options for setup, ie the objective cards
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChosenTokenSetupEvent event) {
        if(view.getState().inGame()) {
            //TODO View has to handle seeing the options for setup, token
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(DrawCardEvent event) {
        if(view.getState().inGame()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                //TODO Update self playArea;
            }
            else{

            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(PlaceCardEvent event) {

    }

    @Override
    public void evaluateEvent(QuitGameEvent event) {
        setup = null;
    }

    @Override
    public void evaluateEvent(GetLobbyInfoEvent event) {
        if(view.getState().inLobby()) {
            view.handleResponse(event.getID(), null, lobbyInfoMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    //TODO (for all the evaluateEvent, do something with the immutable model when feedback is success ?
    //TODO (for all the evaluateEvent, check if incoming event is relevant to the player's phase (in-game, in-lobby, out-game, out-lobby)
    //TODO should i print for IllegalStateException?
    @Override
    public void evaluateEvent(KickFromLobbyEvent event) {
        if(view.getState().inLobby()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else{
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(PlayerReadyEvent event) {
        if(view.getState().inLobby()) {
            if (event.getFeedback().equals(Feedback.SUCCESS))
                this.getPlayersStatus().put(username, true);
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(PlayerUnreadyEvent event) {
        if(view.getState().inLobby()) {
            if (event.getFeedback().equals(Feedback.SUCCESS))
                this.getPlayersStatus().put(username, false);
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(QuitLobbyEvent event) {
        if(view.getState().inLobby()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                this.lobbyId = null;
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    /**
     * The method checks if the client's view is in the menu state.
     * It then builds a message containing all the available lobbies and sends it to the view.
     * @param event The event to be handled.
     */
    @Override
    public void evaluateEvent(AvailableLobbiesEvent event) {
        if(view.getState().inMenu()) {
            lobbies = event.getLobbies();
            StringBuilder message = new StringBuilder();
            for(int i = 0; i < lobbies.size(); i++) {
                message.append("\u001B[1m").append(i + 1).append("- ").append("\u001B[0m").append(lobbies.get(i)).append("\n");
            }
            view.handleResponse(event.getID(), event.getFeedback(), message.toString());
        } else {
            System.out.println("Menu state: event in wrong state!!");
        }
    }

    @Override
    public void evaluateEvent(CreateLobbyEvent event) {
        if(view.getState().inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                lobbyId = event.getLobbyID();
                playersStatus.put(username, false);
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state!!");
        }
    }

    @Override
    public void evaluateEvent(DeleteAccountEvent event) {
        if(view.getState().inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)) {
                this.username = null;
                this.model = null;
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(GetMyOfflineGamesEvent event) {
        if(view.getState().inMenu()) {
            offlineGames = event.getGames();
            StringBuilder message = new StringBuilder(event.getMessage() + "\n");
            for(int i = 0; i < offlineGames.size(); i++) {
                message.append("\u001B[1m").append(i + 1).append("- ").append("\u001B[0m").append(offlineGames.get(i)).append("\n");
            }
            view.handleResponse(event.getID(), event.getFeedback(), message.toString());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) {
        if(view.getState().inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)) {
                this.lobbyId = event.getLobbyID();
                this.playersStatus = event.getReadyStatusPlayers();
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    /**
     * The method checks if the client's view is in the menu state.
     * The ViewController gets the client's username and calls a method for setting it on the {@link View}.
     * @param event The event to be handled.
     */
    @Override
    public void evaluateEvent(LoginEvent event) {
        if(view.getState().inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)) {
                String username = event.getUsername();
                this.username = username;
                view.setUsername(username);
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(LogoutEvent event) {
        if(view.getState().inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)) {
                this.username = null;
                this.model = null;
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(ReconnectToGameEvent event) {
        if(view.getState().inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)) {
                this.lobbyId = event.getGameID();
                //TODO this.model should get from the server their model of the DCed game
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(RegisterEvent event) {
        if(view.getState().inMenu()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChatGMEvent event) {
        chatMessages.add(event.getChatMessage());
        if (view.getState().inChat()) {
            // TODO update view
        }
    }

    @Override
    public void evaluateEvent(ChatPMEvent event) {
        privateChatMessages.add(event.getChatMessage());
        if (view.getState().inChat()) {
            // TODO update view
        }
    }

    @Override
    public void evaluateEvent(ServerDisconnectedEvent event) {
        username = null;
        lobbyId = null;
        model = null;
        taskQueueStopped = true;
        view.serverDisconnected();
    }

    public String getUsername() {
        return username;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public List<LobbyState> getLobbies() {
        return lobbies;
    }

    public List<LobbyState> getOfflineGames() {
        return offlineGames;
    }

    public Map.Entry<String, Boolean> getLobbyLeader() {
        return playersStatus.entrySet().iterator().next();
    }

    public Map<String, Boolean> getPlayersStatus() {
        return playersStatus;
    }

    public boolean isLobbyLeader(){
        return playersStatus.entrySet().iterator().next().getKey().equals(username);
    }

    private String lobbyInfoMessage() {
        StringBuilder m = new StringBuilder();
        m.append("Lobby '").append(lobbyId).append("':\n");
        playersStatus.forEach((key, value) -> {
            String readyStatusString = value ? "Ready" : "Not ready";
            m.append("  ").append(key).append(" / ").append(readyStatusString).append("\n");
        });
        return m.toString();
    }
}
