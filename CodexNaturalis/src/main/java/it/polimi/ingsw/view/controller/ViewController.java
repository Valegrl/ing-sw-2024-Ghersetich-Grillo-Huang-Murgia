package it.polimi.ingsw.view.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.*;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetChatMessagesEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetLobbyInfoEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ServerDisconnectedEvent;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.viewModel.ViewModel;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.turnAction.draw.DrawCardData;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.IntStream;

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
     * The list of available tokens for the player to choose from.
     */
    private List<Token> availableTokens;

    /**
     * The {@link ViewModel} for the current game.
     */
    private ViewModel model;


    private final Object modelLock = new Object();

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

    @Override
    public void evaluateEvent(ChooseTokenSetupEvent event) {
        availableTokens = event.getAvailableColors();
        if(view.inGame() || view.inChat()) {
            view.handleResponse(event.getID(), null, event.getMessage());
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
        if(view.inLobby() || view.inChat()) {
            clearViewData();
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(UpdateLobbyPlayersEvent event) {
        if(view.inLobby() || view.inChat()) {
            playersStatus = event.getPlayers();
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Lobby state: event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(UpdateGamePlayersEvent event) {
        if(view.inLobby() || view.inGame() || view.inChat()) {
            playersStatus = event.getPlayers();
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChooseCardsSetupEvent event) {
        if(view.inGame()) {
            setup = event.getViewSetup();
            String message = "Assigned setup:\n" + setup.getStartCard().printSetupStartCard() + "\n" +
                    setup.printSetupObjCards() +
                    // end of assigned setup message: split[0]
                    "%" +
                    // start of hand message: split[1]
                    setup.printSetupHand() +
                    "%" +
                    // start of common objectives message: split[2]
                    setup.printSetupCommonObjectives() +
                    "%" +
                    // start of decks message: split[3]
                    setup.printSetupDecks() +
                    "%" +
                    // start of hands message: split[4]
                    setup.printSetupOpponentsHands();
            view.handleResponse(event.getID(), null, message);
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(SelfDrawCardEvent event) {
        synchronized (modelLock) {
            model.getSelfPlayer().getPlayArea().setHand(event.getMyDrawCardData().getHand());
            drawCardUpdateModel(event.getMyDrawCardData());
        }
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(SelfPlaceCardEvent event) {
        synchronized (modelLock) {
            model.getSelfPlayer().setPlayArea(event.getMyPlaceCardData().getPlayArea());
        }
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(NewTurnEvent event) {
        synchronized (modelLock) {
            model.setTurnPlayerIndex(event.getTurnIndex());
            model.setGameStatus(event.getGameStatus());
        }

        if (view.inGame() || view.inChat()) { // TODO handle in chat
            String message = "New turn!";
            view.handleResponse(event.getID(), null, message);
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(OtherDrawCardEvent event) {
        String player = event.getOtherDrawCardData().getOpponent();
        synchronized (modelLock) {
            model.getOpponent(player).getPlayArea().setHand(event.getOtherDrawCardData().getHand());
            drawCardUpdateModel(event.getOtherDrawCardData());
        }
        if (view.inGame() || view.inChat()) { // TODO handle in chat
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(OtherPlaceCardEvent event) {
        String player = event.getOtherPlaceCardData().getOpponent();
        synchronized (modelLock) {
            model.getOpponent(player).setPlayArea(event.getOtherPlaceCardData().getOpponentPlayArea());
        }
        if (view.inGame() || view.inChat()) { // TODO handle in chat
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(EndedGameEvent event) {

    }

    @Override
    public void evaluateEvent(UpdateLocalModelEvent event) {
        synchronized (modelLock) {
            model = event.getModel();
        }
        if (view.inGame() || view.inChat()) {
            view.handleResponse(event.getID(), null, "Game started!");
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(AvailablePositionsEvent event) {
        String avPosMessage;
        synchronized (modelLock) {
            avPosMessage = model.getSelfPlayer().getPlayArea().printAvailablePos();
        }
        if(view.inGame()) {
            view.handleResponse(event.getID(), null, avPosMessage);
        } else {
            System.out.println("Game state: event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(IsMyTurnEvent event) {
        if(view.inGame()) {
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
        if(view.inGame()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChosenTokenSetupEvent event) {
        if(view.inGame()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(DrawCardEvent event) {
        if (view.inGame()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(PlaceCardEvent event) {
        if (view.inGame()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(QuitGameEvent event) {
        if (view.inGame()) {
            if (event.getFeedback().equals(Feedback.SUCCESS)) {
                clearViewData();
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(GetLobbyInfoEvent event) {
        if(view.inLobby()) {
            view.handleResponse(event.getID(), null, lobbyInfoMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(GetChatMessagesEvent event) {
        if(view.inChat()) {
            ChatMessagesList<ChatMessage> messages = new ChatMessagesList<>(chatMessages.size() + privateChatMessages.size());
            messages.addAll(chatMessages);
            messages.addAll(privateChatMessages);
            view.handleResponse(event.getID(), null, messages.toString());
        }
    }

    //TODO (for all the evaluateEvent, do something with the immutable model when feedback is success ?
    // (for all the evaluateEvent, check if incoming event is relevant to the player's phase (in-game, in-lobby, out-game, out-lobby)
    // should i print for IllegalStateException?
    @Override
    public void evaluateEvent(KickFromLobbyEvent event) {
        if(view.inLobby()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else{
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(PlayerReadyEvent event) {
        if(view.inLobby()) {
            if (event.getFeedback().equals(Feedback.SUCCESS))
                getPlayersStatus().put(username, true);
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(PlayerUnreadyEvent event) {
        if(view.inLobby()) {
            if (event.getFeedback().equals(Feedback.SUCCESS))
                getPlayersStatus().put(username, false);
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(QuitLobbyEvent event) {
        if(view.inLobby()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                clearViewData();
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
        if(view.inMenu()) {
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
        if(view.inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)){
                lobbyId = event.getLobbyID();
                playersStatus = new LinkedHashMap<>();
                playersStatus.put(username, false);
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state!!");
        }
    }

    @Override
    public void evaluateEvent(DeleteAccountEvent event) {
        if(view.inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)) {
                this.username = null;
                clearViewData();
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(GetMyOfflineGamesEvent event) {
        if(view.inMenu()) {
            offlineGames = event.getGames();
            StringBuilder message = new StringBuilder(event.getMessage() + "\n");
            for(int i = 0; i < offlineGames.size(); i++) {
                message.append("  ").append(AnsiCodes.BOLD).append(i + 1).append("- ").append(AnsiCodes.RESET).append(offlineGames.get(i)).append("\n");
            }
            view.handleResponse(event.getID(), event.getFeedback(), message.toString());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) {
        if(view.inMenu()) {
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
        if(view.inMenu()) {
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
        if(view.inMenu()) {
            if(event.getFeedback().equals(Feedback.SUCCESS)) {
                username = null;
                clearViewData();
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(ReconnectToGameEvent event) {
        if(view.inMenu()) {
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
        if(view.inMenu()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChatGMEvent event) {
        ChatMessage message = event.getChatMessage();
        chatMessages.add(message);
        if (view.inChat()) {
            if (event.getFeedback().equals(Feedback.SUCCESS)) {
                view.handleResponse(event.getID(), event.getFeedback(), message.toString());
            } else {
                view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
            }
        }
    }

    @Override
    public void evaluateEvent(ChatPMEvent event) {
        PrivateChatMessage message = event.getChatMessage();
        privateChatMessages.add(message);
        if (view.inChat()) {
            if (event.getFeedback().equals(Feedback.SUCCESS)) {
                view.handleResponse(event.getID(), event.getFeedback(), message.toString());
            } else {
                view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
            }
        }
    }

    @Override
    public void evaluateEvent(ServerDisconnectedEvent event) {
        username = null;
        clearViewData();
        taskQueueStopped = true;
        view.serverDisconnected();
    }

    private void clearViewData() {
        synchronized (modelLock) {
            model = null;
            playersStatus = null;
            lobbyId = null;
            setup = null;
            availableTokens = null;
            chatMessages.clear();
            privateChatMessages.clear();
        }
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

    public ViewModel getModel() {
        return new ViewModel(model);
    }

    public List<String> getPlayerUsernames() {
        synchronized (modelLock) {
            return model.getPlayerUsernames();
        }
    }

    public int getSelfHandSize() {
        synchronized (modelLock) {
            return model.getSelfPlayer().getPlayArea().getHand().size();
        }
    }

    public ImmPlayableCard getSelfHandCard(int index) {
        synchronized (modelLock) {
            return model.getSelfPlayer().getPlayArea().getHand().get(index);
        }
    }

    public List<String> getSelfHandCardIds() {
        synchronized (modelLock) {
            return model.getSelfPlayer().getPlayArea().getHand().stream()
                    .map(c -> Item.itemToColor(c.getPermanentResource(), c.getId()))
                    .toList();
        }
    }

    public List<String> getDeckVisibleIds(CardType cardType) {
        synchronized (modelLock) {
            if (cardType.equals(CardType.GOLD)) {
                return new ArrayList<>(Arrays.stream(model.getVisibleGoldCards()).toList().stream()
                        .filter(Objects::nonNull)
                        .map(ImmPlayableCard::toString)
                        .toList());
            } else {
                return new ArrayList<>(Arrays.stream(model.getVisibleResourceCards()).toList().stream()
                        .filter(Objects::nonNull)
                        .map(ImmPlayableCard::toString)
                        .toList());
            }
        }
    }

    public int getVisibleDeckCardIndex(CardType cardType, String id) {
        synchronized (modelLock) {
            if (cardType.equals(CardType.GOLD)) {
                ImmPlayableCard[] visibleGoldCards = model.getVisibleGoldCards();
                return IntStream.range(0, visibleGoldCards.length)
                        .filter(i -> id.equals(visibleGoldCards[i].toString()))
                        .findFirst()
                        .orElse(-1);
            } else {
                ImmPlayableCard[] visibleResourceCards = model.getVisibleResourceCards();
                return IntStream.range(0, visibleResourceCards.length)
                        .filter(i -> id.equals(visibleResourceCards[i].toString()))
                        .findFirst()
                        .orElse(-1);
            }
        }
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

    public GameStatus getGameStatus() {
        synchronized (modelLock) {
            return model.getGameStatus();
        }
    }

    public List<Token> getAvailableTokens() {
        return availableTokens;
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

    public void chosenSetup(int chosenObj, boolean showingFace) {
        assert chosenObj < 3 && chosenObj > 0;
        String chosenObjective = setup.getSecretObjectiveCards()[chosenObj - 1].getId();
        ChosenCardsSetupEvent chosenCardsSetupEvent = new ChosenCardsSetupEvent(chosenObjective, showingFace);
        newViewEvent(chosenCardsSetupEvent);
    }

    public String availableTokensMessage() {
        StringBuilder m = new StringBuilder();
        m.append("Choose your colored token from the following available ones:\n");
        for(int i = 0; i < availableTokens.size(); i++) {
            m.append("  ").append(i + 1).append("- ").append(availableTokens.get(i)).append("\n");
        }
        return m.toString();
    }

    public boolean hasTurn() {
        synchronized (modelLock) {
            return model.getTurnPlayerIndex() == model.getPlayerUsernames().indexOf(username);
        }
    }

    public String playersListToString() {
        StringBuilder m = new StringBuilder();
        int turnPlayerIndex;
        List<String> playerUsernames;
        synchronized (modelLock) {
            turnPlayerIndex = model.getTurnPlayerIndex();
            playerUsernames = List.copyOf(model.getPlayerUsernames());
        }

        int lastPlayer = ((turnPlayerIndex - 1) + playerUsernames.size()) % playerUsernames.size();
        for (int i = turnPlayerIndex; i != lastPlayer; i = (i + 1) % playerUsernames.size()) {
            // TODO check if online?
            if (i == turnPlayerIndex) {
                m.append(AnsiCodes.BOLD).append(playerUsernames.get(i)).append(AnsiCodes.RESET).append(" -> ");
            } else if (i == playerUsernames.indexOf(username)) {
                m.append(AnsiCodes.GOLD).append(playerUsernames.get(i)).append(AnsiCodes.RESET)
                        .append(" -> ");
            } else {
                m.append(playerUsernames.get(i)).append(" -> ");
            }
        }
        if (playerUsernames.get(lastPlayer).equals(username))
            m.append(AnsiCodes.GOLD).append(username).append(AnsiCodes.RESET);
        else
            m.append(playerUsernames.get(lastPlayer));
        return m.toString();
    }

    public String decksToString() {
        ViewModel vm = getModel();
        return vm.decksToString();
    }

    public String objectiveCardsToString() {
        ViewModel vm = getModel();
        return vm.objectiveCardsToString();
    }

    public String selfPlayAreaToString() {
        ViewModel vm = getModel();
        return vm.selfPlayAreaToString();
    }

    public String opponentPlayAreaToString(String player) {
        ViewModel vm = getModel();
        return vm.opponentPlayAreaToString(player);
    }

    private void drawCardUpdateModel(DrawCardData data) {
        synchronized (modelLock) {
            model.setGameStatus(data.getGameStatus());
            model.setTopGoldDeck(data.getTopGoldDeck());
            model.setTopResourceDeck(data.getTopResourceDeck());
            model.setVisibleGoldCards(data.getVisibleGoldCards());
            model.setVisibleResourceCards(data.getVisibleResourceCards());
        }
    }

    public boolean isAvailablePos(Coordinate c) {
        synchronized (modelLock) {
            return model.getSelfPlayer().getPlayArea().getAvailablePos().contains(c);
        }
    }

    public ViewStartSetup getSetup(){
        return this.setup;
    }
}
