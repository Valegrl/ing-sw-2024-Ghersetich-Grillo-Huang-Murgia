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
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.viewModel.EndedGameData;
import it.polimi.ingsw.viewModel.ViewModel;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.turnAction.draw.DrawCardData;
import it.polimi.ingsw.viewModel.turnAction.place.PlaceCardData;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayer;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * The ViewController class is responsible for handling events from the view and forwarding them to the {@link ClientManager}.
 * It also receives events from the {@link ClientManager} and forwards them to the view to be processed.
 * The class contains a {@link ViewModel} of the game in the client's local and some data useful for the view.
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
     * The available-to-reconnect offline games.
     */
    private List<LobbyState> offlineGames;

    /**
     * The map of players and their ready/online status in the lobby.
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
     * The {@link ViewStartSetup} of the game.
     */
    private ViewStartSetup setup;

    /**
     * The list of available {@link Token tokens} for the player to choose from.
     */
    private List<Token> availableTokens;

    /**
     * The pair of booleans representing if the player is in the setup phase
     * and if the player is specifically in the token setup phase.
     */
    private Pair<Boolean, Boolean> inSetup = new Pair<>(false, false);

    /**
     * The {@link ViewModel} for the current game.
     */
    private ViewModel model;

    /**
     * The previous game state, a Pair of a {@link GameStatus}
     * and a boolean representing if the self-player had already placed a card or not.
     */
    private Pair<GameStatus, ViewState> previousGameStatus;

    /**
     * The {@link EndedGameData} for the current game.
     */
    private EndedGameData endedGameData;

    /**
     * The lock used to synchronize the model.
     */
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
                        try {
                            tasksQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    event = tasksQueue.poll();
                }
                event.receiveEvent(this);
            }
        }).start();
    }

    /**
     * Handles a new event from the view, which can be sent to the server or processed locally.
     *
     * @param event The new event from the view.
     */
    public void newViewEvent(Event event) {

        if (EventID.isLocal(event.getID())) {
            synchronized (tasksQueue) {
                tasksQueue.add(event);
                tasksQueue.notifyAll();
            }
        } else {
            try {
                clientManager.handleEvent(event);
            } catch(RemoteException e) {
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
        if (view.inGame()) {
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
        if (view.inLobby()) {
            clearViewData();
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(SelfTurnTimerExpiredEvent event) {
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, "The timer for your turn has expired.");
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(UpdateLobbyPlayersEvent event) {
        if (view.inLobby()) {
            playersStatus = event.getPlayers();
            view.handleResponse(event.getID(), null, event.getMessage());
        } else {
            System.out.println("Lobby state: event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(UpdateGamePlayersEvent event) {
        playersStatus = event.getPlayers();
        if (view.inLobby()) {
            view.handleResponse(event.getID(), null, event.getMessage());
        } else if (view.inGame()) {
            if (!inSetup.key() && !model.getGameStatus().equals(GameStatus.WAITING) && playersOnline() == 1 && playersStatus.size() > 1) { // checking if the game needs to be set on WAITING or ENDED
                model.setGameStatus(GameStatus.WAITING);
                view.stopInputRead(true);
                view.handleResponse(EventID.NEW_GAME_STATUS.getID(), null, event.getMessage());
                // note that previousGameStatus will be set from the current state using the setter method
                // this is to make chat set the previousState to its previous state
            } else if (!inSetup.key() && model.getGameStatus().equals(GameStatus.WAITING) && playersOnline() > 1) { // checking if the game needs to be running again
                model.setGameStatus(previousGameStatus.key());
                if (hasTurn()) {
                    view.handleResponse(event.getID(), null, event.getMessage());
                    view.handleResponse(EventID.NEW_GAME_STATUS.getID(), null, "The game will start from where it was interrupted.");
                }
            } else {
                view.handleResponse(event.getID(), null, event.getMessage());
            }
        } else {
            System.out.println("Event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChooseCardsSetupEvent event) {
        if (view.inGame()) {
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
        boolean oldDLC;
        synchronized (modelLock) {
            oldDLC = model.isDetectedLC();
            model.getSelfPlayer().getPlayArea().setHand(event.getMyDrawCardData().getHand());
            drawCardUpdateModel(event.getMyDrawCardData());
        }
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, event.getMessage());
            handleDetectedLastCircle(oldDLC);
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(SelfPlaceCardEvent event) {
        boolean oldDLC;
        synchronized (modelLock) {
            oldDLC = model.isDetectedLC();
            placeCardUpdateModel(event.getMyPlaceCardData());
            model.getSelfPlayer().setPlayArea(event.getMyPlaceCardData().getPlayArea());
        }
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, event.getMessage());
            handleDetectedLastCircle(oldDLC);
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

        if (view.inGame()) {
            String message = "New turn!";
            if (view.inChat())
                if (hasTurn()) message += " It's your turn!";
                else message += " It's " + model.getPlayerUsernames().get(model.getTurnPlayerIndex()) + "'s turn!";
            view.handleResponse(event.getID(), null, message);
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(OtherDrawCardEvent event) {
        String player = event.getOtherDrawCardData().getOpponent();
        boolean oldDLC;
        synchronized (modelLock) {
            oldDLC = model.isDetectedLC();
            model.getOpponent(player).getPlayArea().setHand(event.getOtherDrawCardData().getHand());
            drawCardUpdateModel(event.getOtherDrawCardData());
        }
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, event.getMessage());
            handleDetectedLastCircle(oldDLC);
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(OtherPlaceCardEvent event) {
        String player = event.getOtherPlaceCardData().getOpponent();
        boolean oldDLC;
        synchronized (modelLock) {
            oldDLC = model.isDetectedLC();
            placeCardUpdateModel(event.getOtherPlaceCardData());
            model.getOpponent(player).setPlayArea(event.getOtherPlaceCardData().getOpponentPlayArea());
        }
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, event.getMessage());
            handleDetectedLastCircle(oldDLC);
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(EndedGameEvent event) {
        endedGameData = event.getEndedGameData();
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, "Game ended!");
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(UpdateLocalModelEvent event) {
        inSetup = new Pair<>(false, false);
        synchronized (modelLock) {
            model = event.getModel();
        }
        if (view.inGame()) {
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
        if (view.inGame()) {
            view.handleResponse(event.getID(), null, avPosMessage);
        } else {
            System.out.println("Game state: event in the wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChosenCardsSetupEvent event) {
        if (view.inGame()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Game state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(ChosenTokenSetupEvent event) {
        if (view.inGame()) {
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
        if (view.inLobby()) {
            view.handleResponse(event.getID(), null, lobbyInfoMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(GetChatMessagesEvent event) {
        if (view.inChat()) {
            ChatMessagesList<ChatMessage> messages = new ChatMessagesList<>(chatMessages.size() + privateChatMessages.size());
            messages.addAll(chatMessages);
            messages.addAll(privateChatMessages);
            view.handleResponse(event.getID(), null, messages.toString());
        }
    }

    @Override
    public void evaluateEvent(KickFromLobbyEvent event) {
        if (view.inLobby()) {
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(PlayerReadyEvent event) {
        if (view.inLobby()) {
            if (event.getFeedback().equals(Feedback.SUCCESS))
                getPlayersStatus().put(username, true);
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(PlayerUnreadyEvent event) {
        if (view.inLobby()) {
            if (event.getFeedback().equals(Feedback.SUCCESS))
                getPlayersStatus().put(username, false);
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Lobby state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(QuitLobbyEvent event) {
        if (view.inLobby()) {
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
        if (view.inMenu()) {
            lobbies = event.getLobbies();
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < lobbies.size(); i++) {
                message.append("\u001B[1m").append(i + 1).append("- ").append("\u001B[0m").append(lobbies.get(i)).append("\n");
            }
            view.handleResponse(event.getID(), event.getFeedback(), message.toString());
        } else {
            System.out.println("Menu state: event in wrong state!!");
        }
    }

    @Override
    public void evaluateEvent(CreateLobbyEvent event) {
        if (view.inMenu()) {
            if (event.getFeedback().equals(Feedback.SUCCESS)){
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
        if (view.inMenu()) {
            if (event.getFeedback().equals(Feedback.SUCCESS)) {
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
        if (view.inMenu()) {
            offlineGames = event.getGames();
            StringBuilder message = new StringBuilder(event.getMessage() + "\n");
            for (int i = 0; i < offlineGames.size(); i++) {
                message.append("  ").append(AnsiCodes.BOLD).append(i + 1).append("- ").append(AnsiCodes.RESET).append(offlineGames.get(i)).append("\n");
            }
            view.handleResponse(event.getID(), event.getFeedback(), message.toString());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(JoinLobbyEvent event) {
        if (view.inMenu()) {
            if (event.getFeedback().equals(Feedback.SUCCESS)) {
                this.lobbyId = event.getLobbyID();
                this.playersStatus = event.getReadyStatusPlayers();
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(LoginEvent event) {
        if (view.inMenu()) {
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
        if (view.inMenu()) {
            if (event.getFeedback().equals(Feedback.SUCCESS)) {
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
        if (view.inMenu()) {
            if (event.getFeedback().equals(Feedback.SUCCESS)) {
                lobbyId = event.getGameID();
                model = event.getViewModel();
                playersStatus = event.getPlayers();
                inSetup = new Pair<>(event.getGameStatus().equals(GameStatus.SETUP), event.isAutoSetup());
            }
            view.handleResponse(event.getID(), event.getFeedback(), event.getMessage());
        } else {
            System.out.println("Menu state: event in wrong state");
        }
    }

    @Override
    public void evaluateEvent(RegisterEvent event) {
        if (view.inMenu()) {
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


    /**
     * Clears the view data, including model, players' status, lobby ID, setup, available tokens, chat messages,
     * private chat messages, and previous game status.
     */
    private void clearViewData() {
        synchronized (modelLock) {
            model = null;
            playersStatus = null;
            lobbyId = null;
            setup = null;
            availableTokens = null;
            inSetup = new Pair<>(false, false);
            chatMessages.clear();
            privateChatMessages.clear();
            previousGameStatus = null;
        }
    }

    /**
     * Retrieves the username of the player.
     *
     * @return {@link ViewController#username}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the lobby ID of the player.
     *
     * @return {@link ViewController#lobbyId}.
     */
    public String getLobbyId() {
        return lobbyId;
    }

    /**
     * Retrieves the lobbies available to the player.
     *
     * @return {@link ViewController#lobbies}.
     */
    public List<LobbyState> getLobbies() {
        return lobbies;
    }

    /**
     * Retrieves a copy of the game's model.
     *
     * @return {@link ViewController#model}.
     */
    public ViewModel getModel() {
        return model != null ? new ViewModel(model) : null;
    }

    /**
     * Retrieves a copy of the list of player usernames from the model.
     *
     * @return The list of player usernames.
     */
    public List<String> getPlayerUsernames() {
        List<String> usernames;
        synchronized (modelLock) {
            usernames = new ArrayList<>(model.getPlayerUsernames());
        }
        return usernames;
    }

    /**
     * Retrieves the {@link SelfViewPlayer self player}'s card from the given index of its hand.
     * @param index The index of the card in the hand.
     * @return The card at the given index of the hand.
     */
    public ImmPlayableCard getSelfHandCard(int index) {
        ImmPlayableCard card;
        synchronized (modelLock) {
            card = model.getSelfPlayer().getPlayArea().getHand().get(index);
        }
        return card;
    }

    /**
     * Retrieves the {@link SelfViewPlayer self player}'s hand cards' ids.
     * @return The list of ids of the cards in the hand.
     */
    public List<String> getSelfHandCardIds() {
        synchronized (modelLock) {
            return model.getSelfPlayer().getPlayArea().getHand().stream()
                    .map(c -> Item.itemToColor(c.getPermanentResource(), c.getId()))
                    .toList();
        }
    }

    /**
     * Retrieves the given deck's visible cards' colored ids.
     * @param cardType The type of deck to retrieve the visible cards from.
     * @return The list of colored ids of the visible cards in the deck.
     */
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

    /**
     * Retrieves the index of the chosen deck's visible card with the given colored id.
     * @param cardType The type of deck to retrieve the visible card from.
     * @param idToString The colored id of the card to find.
     * @return The index of the visible card in the deck.
     */
    public int getVisibleDeckCardIndex(CardType cardType, String idToString) {
        synchronized (modelLock) {
            if (cardType.equals(CardType.GOLD)) {
                ImmPlayableCard[] visibleGoldCards = model.getVisibleGoldCards();
                return IntStream.range(0, visibleGoldCards.length)
                        .filter(i -> idToString.equals(visibleGoldCards[i].toString()))
                        .findFirst()
                        .orElse(-1);
            } else {
                ImmPlayableCard[] visibleResourceCards = model.getVisibleResourceCards();
                return IntStream.range(0, visibleResourceCards.length)
                        .filter(i -> idToString.equals(visibleResourceCards[i].toString()))
                        .findFirst()
                        .orElse(-1);
            }
        }
    }

    /**
     * Retrieves the offline games available to the player.
     * @return {@link ViewController#offlineGames}.
     */
    public List<LobbyState> getOfflineGames() {
        return offlineGames;
    }

    /**
     * Retrieves the lobby leader of the player's lobby and its status.
     * @return The lobby leader's username and ready status.
     */
    public Map.Entry<String, Boolean> getLobbyLeader() {
        return playersStatus.entrySet().iterator().next();
    }

    /**
     * Retrieves the players' status in the lobby.
     * @return {@link ViewController#playersStatus}.
     */
    public Map<String, Boolean> getPlayersStatus() {
        return playersStatus;
    }

    /**
     * Retrieves the game status of the player's game.
     * @return The game status.
     */
    public GameStatus getGameStatus() {
        GameStatus gameStatus;
        synchronized (modelLock) {
            gameStatus = model.getGameStatus();
        }
        return gameStatus;
    }

    /**
     * Retrieves the available tokens for the player to choose from.
     * @return {@link ViewController#availableTokens}.
     */
    public List<Token> getAvailableTokens() {
        return availableTokens;
    }

    /**
     * Whether the player is in the setup phase or not.
     * @return {@link ViewController#inSetup}.
     */
    public boolean isInSetup() {
        return inSetup.key();
    }

    /**
     * Whether the player is in the token setup phase or not.
     * @return {@link ViewController#inSetup}.
     */
    public boolean isInTokenSetup() {
        return inSetup.value();
    }

    /**
     * Sets the inSetup pair.
     * @param inSetup The pair of booleans representing if the player is in the setup phase.
     */
    public void setInSetup(Pair<Boolean, Boolean> inSetup) {
        this.inSetup = inSetup;
    }

    /**
     * Retrieves the {@link EndedGameData} for the current game.
     * @return {@link ViewController#endedGameData}.
     */
    public EndedGameData getEndedGameData() {
        return endedGameData;
    }

    /**
     * Whether the player is the lobby leader or not.
     * @return True if the player is the lobby leader, false otherwise.
     */
    public boolean isLobbyLeader(){
        return playersStatus.entrySet().iterator().next().getKey().equals(username);
    }

    /**
     * A string representation of the lobby's information.
     * @return The lobby's information in a String format.
     */
    private String lobbyInfoMessage() {
        StringBuilder m = new StringBuilder();
        m.append("Lobby '").append(lobbyId).append("':\n");
        playersStatus.forEach((key, value) -> {
            String readyStatusString = value ? "Ready" : "Not ready";
            m.append("  ").append(key).append(" / ").append(readyStatusString).append("\n");
        });
        return m.toString();
    }

    /**
     * A function for view to choose their cards setup and communicate it to the server.
     * @param chosenObj The chosen objective card.
     * @param showingFace Whether the start card will be placed face up or face down.
     */
    public void chosenSetup(int chosenObj, boolean showingFace) {
        assert chosenObj < 3 && chosenObj > 0;
        String chosenObjective = setup.getSecretObjectiveCards()[chosenObj - 1].getId();
        ChosenCardsSetupEvent chosenCardsSetupEvent = new ChosenCardsSetupEvent(chosenObjective, showingFace);
        newViewEvent(chosenCardsSetupEvent);
    }

    /**
     * A string representation of the AvailableTokensMessage.
     * @return The AvailableTokensMessage in a String format.
     */
    public String availableTokensMessage() {
        StringBuilder m = new StringBuilder();
        m.append("Choose your colored token from the following available ones:\n");
        for(int i = 0; i < availableTokens.size(); i++) {
            m.append("  ").append(i + 1).append("- ").append(availableTokens.get(i)).append("\n");
        }
        return m.toString();
    }

    /**
     * Whether the player has the turn or not.
     * @return True if the player has the turn, false otherwise.
     */
    public boolean hasTurn() {
        synchronized (modelLock) {
            return model.getTurnPlayerIndex() == model.getPlayerUsernames().indexOf(username);
        }
    }

    /**
     * Calculates the number of online players in the current game.
     * @return The number of players online in the game.
     */
    private int playersOnline() {
        return playersStatus.entrySet().stream().filter(Map.Entry::getValue).toList().size();
    }

    /**
     * A string representation of the players' list.
     * Used to represent the next turns in the game.
     * @return The players' list in a String format.
     */
    public String playersListToString() {
        StringBuilder m = new StringBuilder();
        int turnPlayerIndex;
        List<String> playerUsernames;
        synchronized (modelLock) {
            turnPlayerIndex = model.getTurnPlayerIndex();
            playerUsernames = model.getPlayerUsernames();
        }
        List<String> onlinePlayers = getInMatchPlayerUsernames();

        int lastPlayerIndex;
        if (getGameStatus().equals(GameStatus.LAST_CIRCLE))
            lastPlayerIndex = playerUsernames.indexOf(onlinePlayers.getLast());
        else {
            int onlineTurnPlayerIndex = onlinePlayers.indexOf(playerUsernames.get(turnPlayerIndex));
            String lastPlayer = onlinePlayers.get(((onlineTurnPlayerIndex - 1) + onlinePlayers.size()) % onlinePlayers.size());
            lastPlayerIndex = playerUsernames.indexOf(lastPlayer);
        }
        for (int i = turnPlayerIndex; i != lastPlayerIndex; i = (i + 1) % playerUsernames.size()) {
            String usr = playerUsernames.get(i);
            if(onlinePlayers.contains(usr)) {
                if (i == playerUsernames.indexOf(username)) {
                    SelfViewPlayer p = model.getSelfPlayer();
                    m.append(AnsiCodes.UNDERLINE).append(AnsiCodes.BOLD).append(p.getToken().getColorCode())
                            .append(usr)
                            .append("(");
                } else {
                    m.append(model.getOpponent(usr).getToken().getColorCode())
                            .append(usr)
                            .append("(");
                }
                if (!playersStatus.get(usr))
                    m.append("DC ");
                m.append(getModel().getScoreboard().get(usr)).append("p)")
                        .append(AnsiCodes.RESET)
                        .append(" -> ");
            }
        }
        if (playerUsernames.get(lastPlayerIndex).equals(username))
            m.append(AnsiCodes.UNDERLINE).append(AnsiCodes.BOLD).append(model.getSelfPlayer().getToken().getColorCode())
                    .append(username)
                    .append("(");
        else
            m.append(model.getOpponent(playerUsernames.get(lastPlayerIndex)).getToken().getColorCode())
                    .append(playerUsernames.get(lastPlayerIndex))
                    .append("(");
        if (!playersStatus.get(playerUsernames.get(lastPlayerIndex)))
            m.append("DC ");
        m.append(getModel().getScoreboard().get(playerUsernames.get(lastPlayerIndex))).append("p)")
                .append(AnsiCodes.RESET);
        return m.toString();
    }

    /**
     * A string representation of the game's decks.
     * @return The game's decks in a String format.
     */
    public String decksToString() {
        ViewModel vm = getModel();
        return vm.decksToString();
    }

    /**
     * A string representation of the player's common and secret objective cards.
     * @return The game's objective cards in a String format.
     */
    public String objectiveCardsToString() {
        ViewModel vm = getModel();
        return vm.objectiveCardsToString();
    }

    /**
     * A string representation of the self-player's {@link it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayArea SelfViewPlayArea}.
     * @return The player's play area in a String format.
     */
    public String selfPlayAreaToString() {
        ViewModel vm = getModel();
        return vm.selfPlayAreaToString();
    }

    /**
     * A string representation of the given opponent's {@link it.polimi.ingsw.viewModel.viewPlayer.ViewPlayArea ViewPlayArea}.
     * @return The opponent's play area in a String format.
     */
    public String opponentPlayAreaToString(String player) {
        ViewModel vm = getModel();
        return vm.opponentPlayAreaToString(player);
    }

    /**
     * Updates the model with the given {@link PlaceCardData}.
     * @param data The data to update the model with.
     */
    private void placeCardUpdateModel(PlaceCardData data) {
        synchronized (modelLock) {
            model.setGameStatus(data.getGameStatus());
            model.setScoreboard(data.getScoreboard());
            model.setDetectedLC(data.isDetectedLastCircle());
        }
    }

    /**
     * Updates the model with the given {@link DrawCardData}.
     * @param data The data to update the model with.
     */
    private void drawCardUpdateModel(DrawCardData data) {
        synchronized (modelLock) {
            model.setGameStatus(data.getGameStatus());
            model.setTopGoldDeck(data.getTopGoldDeck());
            model.setTopResourceDeck(data.getTopResourceDeck());
            model.setVisibleGoldCards(data.getVisibleGoldCards());
            model.setVisibleResourceCards(data.getVisibleResourceCards());
            model.setDetectedLC(data.isDetectedLastCircle());
        }
    }

    /**
     * Checks if the given {@link Coordinate} is available for placement or not.
     * @param c The coordinate to check.
     * @return True if the coordinate is available, false otherwise.
     */
    public boolean isAvailablePos(Coordinate c) {
        synchronized (modelLock) {
            return model.getSelfPlayer().getPlayArea().getAvailablePos().contains(c);
        }
    }

    /**
     * Whether the game is in the last circle phase or not.
     * @return True if the game is in the last circle phase, false otherwise.
     */
    public boolean isLastCircle() {
        synchronized (modelLock) {
            return model.getGameStatus().equals(GameStatus.LAST_CIRCLE);
        }
    }

    /**
     * Handles the detected last circle event.
     * @param old Whether the last circle was detected before or not.
     */
    private void handleDetectedLastCircle(boolean old) {
        if (!old && model.isDetectedLC()) {
            String p = getBlackTokenPlayer();
            String token;
            if (p.equals(username))
                token = model.getSelfPlayer().getToken().getColorCode();
            else
                token = model.getOpponent(p).getToken().getColorCode();
            view.handleResponse(EventID.NEW_GAME_STATUS.getID(), null, "Last circle will start with " + token + p + AnsiCodes.RESET + "'s turn!");
        }
    }

    /**
     * Retrieves the {@link ViewStartSetup} for the current game.
     * @return The game's start setup.
     */
    public ViewStartSetup getSetup(){
        return this.setup;
    }

    /**
     * Retrieves the previous {@link ViewState} for the current game.
     * @return The game's previous view state.
     */
    public ViewState getPreviousViewState() {
        return previousGameStatus.value();
    }

    /**
     * Sets the previous game status pair.
     * @param previousGameStatus The game status pair to set.
     */
    public void setPreviousGameStatus(Pair<GameStatus, ViewState> previousGameStatus) {
        this.previousGameStatus = previousGameStatus;
    }

    /**
     * Retrieves the usernames of the players in the current match.
     * Excludes players that abandoned the game voluntarily.
     * @return The list of player usernames in the match.
     */
    public List<String> getInMatchPlayerUsernames() {
        List<String> usernames = getPlayerUsernames();
        return usernames.stream()
                .filter(player -> getPlayersStatus().containsKey(player))
                .toList();
    }

    /**
     * Retrieves the username of the player with the black token.
     * @return The username of the player with the black token.
     */
    private String getBlackTokenPlayer() {
        List<String> usernames = getPlayerUsernames();
        return usernames.stream()
                .filter(player -> getPlayersStatus().get(player))
                .findFirst()
                .orElse(null);
    }

    /**
     * A function for view to communicate that the game has ended and the player went back to the main menu.
     */
    public void gameEnded() {
        endedGameData = null;
        clearViewData();
    }

    /**
     * Retrieves the {@link Token} of the player with the given username.
     * @param username The username of the player to retrieve the token of.
     * @return The player's token.
     */
    public Token getPlayerToken(String username) {
        synchronized (modelLock) {
            if (model.getSelfPlayer().getUsername().equals(username))
                return model.getSelfPlayer().getToken();
            else
                return model.getOpponent(username).getToken();
        }
    }
}