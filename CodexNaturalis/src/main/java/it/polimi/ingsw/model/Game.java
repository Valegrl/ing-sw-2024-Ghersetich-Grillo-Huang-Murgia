package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.eventUtils.event.fromModel.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.factory.DeckFactory;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.utils.PlayerCardsSetup;
import it.polimi.ingsw.viewModel.EndedGameData;
import it.polimi.ingsw.viewModel.ViewModel;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.viewModel.turnAction.draw.SelfDrawCardData;
import it.polimi.ingsw.viewModel.turnAction.draw.OtherDrawCardData;
import it.polimi.ingsw.viewModel.turnAction.place.SelfPlaceCardData;
import it.polimi.ingsw.viewModel.turnAction.place.OtherPlaceCardData;

/**
 * This class represents the game Codex Naturalis. It manages the game state, including the players,
 * the decks of cards, the visible cards, the turn order, the scoreboard, and the game status.
 * It also handles the game setup, player actions such as placing and drawing cards, and the end of the game.
 *<p></p>
 * The game is identified by a unique ID. Each game has a resource deck and a gold deck,
 * from which cards are drawn and placed in two visible card arrays.
 * The game keeps track of the players and whose turn it is.
 * The scoreboard keeps track of each player's score.
 * The game status indicates the current phase of the game.
 *<p></p>
 * The class provides methods for game setup, player actions, and game end procedures.
 * It also provides methods for retrieving game information such as the game ID,
 * the top card of each deck, the visible cards, the players, the current player index,
 * the scoreboard, and the common objectives.
 *<p></p>
 * The class ensures that the game rules are followed, and throws exceptions when illegal actions are attempted.
 */
public class Game {
    /**
     * The Game's identifier.
     */
    private final String id;

    /**
     * The Game's {@link ResourceCard resource cards} deck.
     */
    private final Deck<ResourceCard> resourceDeck;

    /**
     * The Game's {@link GoldCard gold cards} deck.
     */
    private final Deck<GoldCard> goldDeck;

    /**
     * An array of visible cards that are initially {@link ResourceCard ResourceCards}.
     * If during the game the {@link Game#resourceDeck} is empty, the array could contain {@link GoldCard GoldCards}.
     */
    private final PlayableCard[] visibleResourceCards;

    /**
     * An array of visible cards that are initially {@link GoldCard GoldCards}.
     * If during the game the {@link Game#goldDeck} is empty, the array could contain {@link ResourceCard ResourceCards}.
     */
    private final PlayableCard[] visibleGoldCards;

    /**
     * The list of {@link Player players} in a Game.
     */
    private final List<Player> players;

    /**
     * The index of which {@link Player player} in {@link Game#players players} List has the current turn.
     */
    private int turnPlayerIndex;

    /**
     * The Game's scoreboard.
     */
    private final Map<Player, Integer> scoreboard;

    /**
     * The two common {@link ObjectiveCard objective cards} for this game.
     */
    private final ObjectiveCard[] commonObjectives;

    /**
     * Manages the game listeners for each player in the game.
     * It is responsible for changing, notifying, and notifying all listeners.
     */
    private final GameListenersManager listeners;

    /**
     * Represents the current status of the game.
     */
    private GameStatus gameStatus;

    /**
     * Represents the backup status of the game.
     * It is used to store the previous game status when the game status changes to WAITING due to a player disconnecting.
     */
    private GameStatus backupGameStatus;

    /**
     * A flag indicating if the last circle was detected.
     * It is set to true when both the resource deck and the gold deck are empty or a player has reached 20 points.
     */
    private boolean detectedLC;

    /**
     * Used to check if a circle of turns is ended.
     */
    private int circleChecker;

    /**
     * Constructs a new Game with the given id and the list of players' usernames.
     * @param id The identifier of the Game.
     * @param players The map of usernames chosen by players.
     */
    public Game(String id, Map<String, GameListener> players) {

        assert id != null : "Game id cannot be null";

        assert players != null && !players.isEmpty() : "Listeners map cannot be null or empty";

        assert !players.containsKey(null) : "Listeners map cannot contain null keys";

        assert !players.containsValue(null) : "Listeners map cannot contain null values";

        Set<String> usernames = players.keySet();

        assert usernames.size() >= 2 && usernames.size() <= 4 : "Number of players must be between 2 and 4";

        Set<String> uniqueUsernames = new HashSet<>(usernames);
        assert uniqueUsernames.size() == usernames.size() : "All players must have unique usernames";

        this.id = id;

        this.resourceDeck = new DeckFactory().createDeck(ResourceCard.class);
        this.goldDeck = new DeckFactory().createDeck(GoldCard.class);

        this.visibleResourceCards = new PlayableCard[2];
        this.visibleGoldCards = new PlayableCard[2];

        this.turnPlayerIndex = 0;

        List<String> playerNames = new ArrayList<>(usernames);
        Collections.shuffle(playerNames);

        this.players = new ArrayList<>();
        for (String user : playerNames)
            this.players.add(new Player(user));

        this.scoreboard = new HashMap<>();
        for (Player p : this.players)
            this.scoreboard.put(p, 0);

        this.commonObjectives = new ObjectiveCard[2];

        this.listeners = new GameListenersManager(this, players);
        this.gameStatus = GameStatus.SETUP;
        this.backupGameStatus = GameStatus.RUNNING;
        this.detectedLC = false;
        this.circleChecker = -1;
    }

    /**
     * Sets up the initial game state by creating and initializing decks and distributing cards to players.
     * <br>
     * The 2 {@link Game#commonObjectives} are drawn from the ObjectiveCards deck.
     * <br>
     * The hand is distributed by giving 2 {@link ResourceCard ResourceCards} and 1 {@link GoldCard} to each player.
     * Each player is given a {@link StartCard}.
     * <br>
     * Initializes each player's {@link PlayArea}.
     *
     * @return A list containing arrays of 2 {@link ObjectiveCard ObjectiveCards} for each player to choose their secret objective from.
     */
    public List<PlayerCardsSetup> gameSetup() {
        Deck<StartCard> startDeck = new DeckFactory().createDeck(StartCard.class);
        Deck<ObjectiveCard> objectiveDeck = new DeckFactory().createDeck(ObjectiveCard.class);

        for (int i = 0; i < visibleGoldCards.length; i++) {
            visibleGoldCards[i] = goldDeck.drawTop();
            visibleResourceCards[i] = resourceDeck.drawTop();
        }

        for (int i = 0; i < commonObjectives.length; i++)
            commonObjectives[i] = objectiveDeck.drawTop();

        List<PlayerCardsSetup> cardsSetup = new ArrayList<>();

        Item topGoldDeck = goldDeck.seeTopCard().getPermanentResource();
        Item topResourceDeck = resourceDeck.seeTopCard().getPermanentResource();

        for (Player p : players) {
            List<PlayableCard> hand = new ArrayList<>();
            for (int i = 0; i < JsonConfig.getInstance().getHandResourceCards(); i++)
                hand.add(resourceDeck.drawTop());
            for (int i = 0; i < JsonConfig.getInstance().getHandGoldCards(); i++)
                hand.add(goldDeck.drawTop());
            Collections.shuffle(hand);

            StartCard start = startDeck.drawTop();
            p.initPlayArea(hand, start);

            ObjectiveCard[] secretObjectiveChoices = new ObjectiveCard[2];

            for (int i = 0; i < 2; i++)
                secretObjectiveChoices[i] = objectiveDeck.drawTop();

            cardsSetup.add(new PlayerCardsSetup(p.getUsername(), secretObjectiveChoices, start));
        }

        Map<String, List<PlayableCard>> playersHands = new HashMap<>();
        for (Player player : players)
            playersHands.put(player.getUsername(), player.getPlayArea().getHand());

        for (PlayerCardsSetup pcs : cardsSetup){
            String username = pcs.getUsername();
            Map<String, List<PlayableCard>> hands = new HashMap<>(playersHands);
            hands.remove(username);
            ViewStartSetup setup = new ViewStartSetup(pcs.getObjectiveCards(), pcs.getStartCard(), playersHands.get(username),
                                   visibleGoldCards, topGoldDeck, visibleResourceCards, topResourceDeck, commonObjectives, hands);
            listeners.notifyListener(username, new ChooseCardsSetupEvent(setup, "Choose your setup!"));
        }

        return cardsSetup;
    }

    /**
     * Starts the game by setting the turn to the first online player.
     * If there is only one online player, the game status is set to WAITING.
     * Otherwise, the game status is set to RUNNING.
     * After setting the game status, it updates the local model for all players.
     *
     * @throws IllegalStateException If there are no online players when the method is called.
     */
    public void startTurn(){
        this.turnPlayerIndex = 0;
        if (onlinePlayersNumber() < 1)
            throw new IllegalStateException("At least one player must be online to start.");
        while (!players.get(turnPlayerIndex).isOnline())
            turnPlayerIndex = (turnPlayerIndex + 1) % players.size();
        if (onlinePlayersNumber() < 2)
            this.gameStatus = GameStatus.WAITING;
        else
            this.gameStatus = GameStatus.RUNNING;

        for (Player p : players) {
            String username = p.getUsername();
            listeners.notifyListener(username, new UpdateLocalModelEvent(new ViewModel(this, username)));
        }
    }

    /**
     * Advances the game to the next player's turn.
     * This method increments the turnPlayerIndex to move to the next player in the player list.
     * If the current player is the last player in the list, the turn wraps around to the first player.
     */
    public void newTurn(boolean notify) {
        this.circleChecker = turnPlayerIndex;

        do {
            turnPlayerIndex = (turnPlayerIndex + 1) % players.size();
        } while (!players.get(turnPlayerIndex).isOnline());

        if (detectedLC && this.gameStatus == GameStatus.RUNNING){
            if (circleChecker > turnPlayerIndex)
                this.gameStatus = GameStatus.LAST_CIRCLE;
        }
        else if (this.gameStatus == GameStatus.LAST_CIRCLE){
            if (circleChecker > turnPlayerIndex) {
                endGame();
                return;
            }
        }

        if(notify && gameStatus!=GameStatus.ENDED)
            listeners.notifyAllListeners(new NewTurnEvent(turnPlayerIndex, gameStatus));
    }

    /**
     * Places a {@link PlayableCard} on the current player's {@link PlayArea} at the specified position.
     * <br>
     * If the card is flipped, it flips the card before placing it on the play area.
     * <br>
     * After determining if the card's {@link GoldCard constraint}, if present, is satisfied,
     * it places the card on the play area at the specified position.
     * <br>
     * Additionally, calculates the points earned by placing the card
     * and assigns them to the current {@link Player}.
     *
     * @param playableCardID    The {@link PlayableCard} to be placed.
     * @param pos               The {@link Coordinate} where the card should be placed on the {@link PlayArea}.
     * @param flipped           A boolean indicating whether the card should be flipped before placement.
     * @throws Exception        If there are errors during the placement action for the current player.
     */
    public void placeCard(String playableCardID, Coordinate pos, boolean flipped) throws Exception {

        Player currPlayer = players.get(turnPlayerIndex);

        PlayableCard c = currPlayer.getPlayArea().getCardById(playableCardID);
        if (c == null)
            throw new Exception("You do not have a card with the provided ID.");

        if (!currPlayer.getPlayArea().getAvailablePos().contains(pos))
            throw new Exception("The provided position is not valid.");

        selectCard(c, pos, currPlayer.getUsername());

        boolean placeable;
        if (c.getCardType().equals(CardType.GOLD) && !flipped) {
            placeable = currPlayer.getPlayArea().checkConstraint(c);
            if (!placeable)
                throw new Exception("The selected card does not satisfy its constraint.");
        } else if (flipped)
            c.flipCard();

        currPlayer.getPlayArea().placeCard(c, pos);

        int points = 0;
        if (!flipped) points = c.getEvaluator().calculatePoints(currPlayer.getPlayArea());

        if (points != 0)
            assignPoints(currPlayer, points);

        String currPlayerUsername = currPlayer.getUsername();
        SelfPlaceCardData selfPlaceCardData = new SelfPlaceCardData(this, currPlayerUsername);
        OtherPlaceCardData otherPlaceCardData = new OtherPlaceCardData(this, currPlayerUsername);
        String selfMessage = "Card placed!";
        String otherMessage = "Player " + currPlayerUsername + " has placed a card.";
        listeners.notifyListener(currPlayerUsername, new SelfPlaceCardEvent(selfPlaceCardData, selfMessage));
        listeners.notifyAllExceptOne(currPlayerUsername, new OtherPlaceCardEvent(otherPlaceCardData, otherMessage));
    }

    /**
     * Draws a card from the chosen deck or from the visible cards array.
     * If a card is drawn from the visible cards array, it is replaced with the top card from the corresponding deck.
     * If the corresponding deck is empty, it is replaced with the top card from the other deck.
     * If both decks are empty, the {@link #detectedLC} is updated to true.
     * The drawn card is added to the current player's hand.
     *
     * @param chosenDeck The deck from which the card should be drawn.
     * @param chosenCard The index of the card to be drawn from the visible cards array.
     *                   If the value is not in the range [0, 2), a card is drawn from the top of the deck.
     */
    public void drawCard(CardType chosenDeck, int chosenCard) throws Exception {

        PlayableCard drawnCard, newVisible;

        if (chosenDeck.equals(CardType.GOLD)) {
            if (chosenCard >= 0 && chosenCard < 2) {
                drawnCard = visibleGoldCards[chosenCard];
                newVisible = goldDeck.drawTop();
                if (newVisible != null)
                    visibleGoldCards[chosenCard] = newVisible;
                else
                    visibleGoldCards[chosenCard] = resourceDeck.drawTop();
            } else
                drawnCard = goldDeck.drawTop();
        } else {
            if (chosenCard >= 0 && chosenCard < 2) {
                drawnCard = visibleResourceCards[chosenCard];
                newVisible = resourceDeck.drawTop();
                if (newVisible != null)
                    visibleResourceCards[chosenCard] = newVisible;
                else
                    visibleResourceCards[chosenCard] = goldDeck.drawTop();
            } else
                drawnCard = resourceDeck.drawTop();
        }

        if (drawnCard == null) throw new Exception("Can't draw a card from this position.");

        Player currPlayer = players.get(turnPlayerIndex);
        currPlayer.getPlayArea().addToHand(drawnCard);

        if (resourceDeck.getSize() == 0 && goldDeck.getSize() == 0) this.detectedLC = true;

        String currPlayerUsername = currPlayer.getUsername();
        SelfDrawCardData selfDrawCardData = new SelfDrawCardData(this, currPlayerUsername);
        OtherDrawCardData otherDrawCardData = new OtherDrawCardData(this, currPlayerUsername);
        String selfMessage = "Card drawn!";
        String otherMessage = "Player " + currPlayerUsername + " has drawn a card.";
        listeners.notifyListener(currPlayerUsername, new SelfDrawCardEvent(selfDrawCardData, selfMessage));
        listeners.notifyAllExceptOne(currPlayerUsername, new OtherDrawCardEvent(otherDrawCardData, otherMessage));
    }

    /**
     * Selects the given {@link EvaluableCard} in the {@link PlayArea} of the specified {@link Player}.
     *
     * @param c    The {@link EvaluableCard} that needs to be selected.
     * @param pos  The {@link Coordinate} of the card that needs to be selected.
     *             Can be null if the card to be selected is a {@link ObjectiveCard}.
     * @param user The username of the {@link Player} that selected the card.
     */
    private void selectCard(EvaluableCard c, Coordinate pos, String user) {
        Player p = getPlayerFromUsername(user);
        if (p == null) throw new PlayerNotFoundException();

        p.getPlayArea().setSelectedCard(pos, c);
    }

    /**
     * Assigns the given points to the selected {@link Player} in the {@link Game#scoreboard}.
     * If the updated score is at least 20, the {@link #detectedLC} is updated to true.
     * The maximum score of the {@link Game#scoreboard} is 29.
     *
     * @param p      The player to assign the points to.
     * @param points The number of points to assign.
     */
    private void assignPoints(Player p, int points) {

        int currScore = scoreboard.get(p);
        if (currScore == 29) return;

        currScore += points;

        if (currScore >= 20 && !gameStatus.equals(GameStatus.LAST_CIRCLE))
            this.detectedLC = true;
        if (currScore > 29) currScore = 29;

        scoreboard.put(p, currScore);
    }

    /**
     * @return The number of online players.
     */
    private int onlinePlayersNumber() {
        int i = 0;
        for (Player p: players) {
            if (p.isOnline()) i++;
        }
        return i;
    }

    /**
     * Sets the {@link Player}'s online status as false.
     * If the number of online players drops to one and the game status is not SETUP,
     * the game status is changed to WAITING.
     *
     * @param user The Player's username.
     */
    public void offlinePlayer(String user) {
        Player p = getPlayerFromUsername(user);
        if (p == null) throw new PlayerNotFoundException();
        p.setOnline(false);
        if (onlinePlayersNumber() == 1 && !gameStatus.equals(GameStatus.SETUP)) {
            this.backupGameStatus = this.gameStatus;
            this.gameStatus = GameStatus.WAITING;
        }
    }

    /**
     * Sets the {@link Player}'s online status as true.
     * If the game status is not SETUP and there are at least two online players, the game status is restored to
     * its backup status.
     * If the game status is not SETUP and there is only one online player, the game status remains as WAITING.
     * If the game status is not ENDED, it sends a ReconnectToGameEvent to the reconnected player.
     *
     * @param user The username of the player to reconnect.
     * @param gl The game listener of the player to reconnect.
     * @throws PlayerNotFoundException If there is no player with the provided username.
     * @throws IllegalStateException If there are no online players when the method is called.
     */
    public ViewModel reconnectPlayer(String user, GameListener gl) {
        Player p = getPlayerFromUsername(user);
        if (p == null) throw new PlayerNotFoundException();

        Player onlinePlayer = players.stream()
                .filter(Player::isOnline)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("At least one player must be online."));

        p.setOnline(true);
        listeners.changeListener(user, gl);
        if (onlinePlayersNumber() == 2 && gameStatus != GameStatus.SETUP) {
            this.gameStatus = backupGameStatus;
            if (!onlinePlayer.equals(players.get(turnPlayerIndex))) {
                newTurn(false);
                if (gameStatus != GameStatus.ENDED)
                    listeners.notifyListener(onlinePlayer.getUsername(), new NewTurnEvent(turnPlayerIndex, gameStatus));
            }
        }

        ViewModel model = null;
        if (gameStatus != GameStatus.SETUP && gameStatus != GameStatus.ENDED)
            model = new ViewModel(this, user);
        return model;
    }

    /**
     * Ends the game and determines the ordered Leaderboard based on objective points earned by {@link Player Players}.
     * <br>
     * This method calculates and assigns the total objective points earned by each player
     * by evaluating the {@link ObjectiveCard ObjectiveCards}.
     */
    public void endGame() {

        Map<Player, Integer> objPoints = new HashMap<>();
        int playerObjPoints;

        for (Player p : players) {
            playerObjPoints = calculateObjectives(p);
            assignPoints(p, playerObjPoints);

            objPoints.put(p, playerObjPoints);
        }

        this.gameStatus = GameStatus.ENDED;

        List<Player> results =  getFinalLeaderboard(objPoints);
        EndedGameData endedGameData = new EndedGameData(this, results);
        listeners.notifyAllListeners(new EndedGameEvent(endedGameData));
    }

    /**
     * Calculates the total points earned by a {@link Player} from both {@link Game#commonObjectives} and {@link Player  secret objective} cards.
     *
     * @param p The player for whom the total objective points are to be calculated.
     * @return  The total points earned by the player.
     */
    private int calculateObjectives(Player p) {
        int objPoints = 0;
        String username = p.getUsername();
        for (ObjectiveCard c: commonObjectives){
            selectCard(c, null, username);
            objPoints += c.getEvaluator().calculatePoints(p.getPlayArea());
        }
        selectCard(p.getSecretObjective(), null, username);
        objPoints += p.getSecretObjective().getEvaluator().calculatePoints(p.getPlayArea());
        return objPoints;
    }

    /**
     * Resolves ties within a group of {@link Player Players} based on their points scored from {@link ObjectiveCard objective cards}.
     *
     * @param players   The list of {@link Player Players} to resolve ties for,
     *                  these players must be included as a key in the objPoints Map.
     * @param objPoints A map containing the objective points scored by each player in the Game, including the ones not considered in the draw.
     * @return The list of {@link Player Players} with ties resolved based on objective points.
     */
    private List<Player> resolveTie(List<Player> players, Map<Player, Integer> objPoints) {
        return players.stream()
                .sorted(Comparator.comparingInt(objPoints::get).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Generates the final leaderboard based on {@link Player Player} scores.
     * Ties are resolved based on their points scored from {@link ObjectiveCard objective cards}.
     *
     * @param objPoints A map containing the objective points scored by each player.
     * @return The final leaderboard as a list of {@link Player} objects, ordered by points and resolved ties.
     */
    private List<Player> getFinalLeaderboard(Map<Player, Integer> objPoints) {

        return scoreboard.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(

                        Collectors.groupingBy(
                                Map.Entry::getValue,
                                LinkedHashMap::new,
                                Collectors.mapping(
                                        Map.Entry::getKey,
                                        Collectors.toList()
                                )
                        )
                )
                .values().stream()
                .flatMap(players -> players.size() > 1 ? resolveTie(players, objPoints).stream() : players.stream())
                .toList();
    }

    /**
     * Updates the player's objective card and start card.
     * This method should only be called once for each player and after the {@link Game#gameSetup}.
     *
     * @param username the username of the player
     * @param objectiveCard the new objective card for the player
     * @param flipStartCard whether to flip the start card
     * @return true if the player was found and the cards were updated, false otherwise
     */
    public boolean setupPlayerCards(String username, ObjectiveCard objectiveCard, boolean flipStartCard) {
        Player player = getPlayerFromUsername(username);
        if (player == null)
            return false;

        player.setSecretObjective(objectiveCard);
        player.getPlayArea().flipStartCard(flipStartCard);

        return true;
    }

    /**
     * Sets the token for the player with the given username.
     * This method should only be called once for each player and after the {@link Game#setupPlayerCards}.
     *
     * @param username the username of the player
     * @param token the token to be set for the player
     * @return true if the player was found and the token was set, false otherwise
     */
    public boolean setupPlayerToken(String username, Token token) {
        Player player = getPlayerFromUsername(username);
        if (player == null)
            return false;

        player.setToken(token);
        return true;
    }

    /**
     * Retrieves the Game id.
     * @return {@link Game#id gameId}.
     */
    public String getId() {
        return id;
    }

    /**
     * Shows the top Card of the Game's resource deck without removing it from the Deck.
     * @return {@link ResourceCard}.
     */
    public ResourceCard seeResourceTopCard() {
        return resourceDeck.seeTopCard();
    }

    /**
     * Shows the top Card of the Game's gold deck without removing it from the Deck.
     * @return {@link GoldCard}.
     */
    public GoldCard seeGoldTopCard() {
        return goldDeck.seeTopCard();
    }

    /**
     * Retrieves the visibleResourceCards array.
     * @return {@link Game#visibleResourceCards}.
     */
    public PlayableCard[] getVisibleResourceCards() {
        return Arrays.copyOf(visibleResourceCards, visibleResourceCards.length);
    }

    /**
     * Retrieves the visibleGoldCards array.
     * @return {@link Game#visibleGoldCards}.
     */
    public PlayableCard[] getVisibleGoldCards() {
        return Arrays.copyOf(visibleGoldCards, visibleGoldCards.length);
    }

    /**
     * Retrieves the list of {@link Player players} in the Game.
     * @return {@link Game#players}.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return A list of usernames of all players in the game.
     */
    public List<String> getPlayerUsernames() {
        return players.stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a player object based on the provided username.
     *
     * @param user The username of the player to retrieve.
     * @return The {@link Player} associated with the given username.
     */
    public Player getPlayerFromUsername(String user) {
        return players.stream()
                .filter(player -> player.getUsername().equals(user))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the index of the player that currently has the turn.
     * @return {@link Game#turnPlayerIndex}.
     */
    public int getTurnPlayerIndex() {
        return turnPlayerIndex;
    }

    /**
     * Retrieves Game's scoreboard.
     * @return {@link Game#scoreboard}.
     */
    public Map<Player, Integer> getScoreboard() {
        return scoreboard;
    }

    /**
     * Retrieves the array of common objective cards for this Game.
     * @return {@link Game#commonObjectives}.
     */
    public ObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    /**
     * Retrieves the current status of the game.
     * @return {@link Game#gameStatus}.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Retrieves the backup status of the game.
     * The backup status is used to store the previous game status when the game status changes to WAITING due to a player disconnecting.
     *
     * @return The backup status of the game.
     */
    public GameStatus getBackupGameStatus() {
        return backupGameStatus;
    }

    /**
     * @return true if it is the player's turn.
     */
    public boolean isTurnPlayer(String player) {
        return players.get(turnPlayerIndex).getUsername().equals(player);
    }

    /**
     * @return The username of the player who currently has the turn.
     */
    public String getCurrentPlayerUsername() {
        return players.get(turnPlayerIndex).getUsername();
    }

    /**
     * @return A set of usernames of all online players in the game.
     */
    public Set<String> getOnlinePlayers() {
        return players.stream()
                .filter(Player::isOnline)
                .map(Player::getUsername)
                .collect(Collectors.toSet());
    }

    /**
     * Checks if the last circle was detected.
     * The last circle is detected when both the resource deck and the gold deck are empty or a player has reached
     * 20 points.
     *
     * @return True if the last circle was detected, false otherwise.
     */
    public boolean isDetectedLC() {
        return detectedLC;
    }
}