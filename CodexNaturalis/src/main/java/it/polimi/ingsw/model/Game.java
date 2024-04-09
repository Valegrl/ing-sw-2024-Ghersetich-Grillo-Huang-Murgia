package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.Deck;

import java.util.*;

import it.polimi.ingsw.model.deck.factory.DeckFactory;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.InvalidConstraintException;
import it.polimi.ingsw.model.exceptions.NoWinnerException;
import it.polimi.ingsw.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.model.player.Player;

/**
 * A class to represent a Codex Naturalis game.
 */
public class Game {
    /**
     * The Game's identifier.
     */
    private final int id;

    /**
     * The Game's {@link it.polimi.ingsw.model.card.ResourceCard resource cards} deck.
     */
    private final Deck<ResourceCard> resourceDeck;

    /**
     * The Game's {@link it.polimi.ingsw.model.card.GoldCard gold cards} deck.
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
     *
     */
    private boolean finalPhase; // TODO GameStatus?

    /**
     * Constructs a new Game with the given id and the list of players' usernames.
     * @param id The identifier of the Game.
     * @param usernames The list of usernames chosen by players.
     */
    public Game(int id, List<String> usernames) {
        this.id = id;

        this.resourceDeck = new DeckFactory().createDeck(ResourceCard.class);
        this.goldDeck = new DeckFactory().createDeck(GoldCard.class);

        this.visibleResourceCards = new PlayableCard[2];
        this.visibleGoldCards = new PlayableCard[2];

        this.turnPlayerIndex = 0;

        this.players = new ArrayList<>();
        for (String user : usernames)
            this.players.add(new Player(user));

        this.scoreboard = new HashMap<>();
        for (Player p : this.players)
            this.scoreboard.put(p, 0);

        this.commonObjectives = new ObjectiveCard[2];

        this.finalPhase = false; //TODO game status enum?
    }

    /**
     * Sets up the initial game state by creating and initializing decks and distributing cards to players.
     *
     * The 2 {@link Game#commonObjectives} are drawn from the ObjectiveCards deck.
     *
     * The hand is distributed by giving 2 {@link ResourceCard ResourceCards} and 1 {@link GoldCard} to each player.
     * Each player is given a {@link StartCard}.
     *
     * Initializes each player's {@link PlayArea}.
     *
     * @return A list containing arrays of 2 {@link ObjectiveCard ObjectiveCards} for each player to choose their secret objective from.
     */
    public List<ObjectiveCard[]> gameSetup() {
        Deck<StartCard> startDeck = new DeckFactory().createDeck(StartCard.class);
        Deck<ObjectiveCard> objectiveDeck = new DeckFactory().createDeck(ObjectiveCard.class);

        for (int i = 0; i < commonObjectives.length; i++)
            commonObjectives[i] = objectiveDeck.drawTop();

        List<ObjectiveCard[]> secretObjectiveCards = new ArrayList<>();

        List<PlayableCard> hand = new ArrayList<>();

        for (Player p : players) {
            hand.add(resourceDeck.drawTop());
            hand.add(resourceDeck.drawTop());
            hand.add(goldDeck.drawTop());

            p.initPlayArea(hand, startDeck.drawTop());

            ObjectiveCard[] secretObjectiveChoices = new ObjectiveCard[2];

            for (int i = 0; i < 2; i++) {
                secretObjectiveChoices[i] = objectiveDeck.drawTop();
            }
            secretObjectiveCards.add(secretObjectiveChoices);
        }
        return secretObjectiveCards;
    }

    /**
     * Advances the game to the next player's turn.
     * This method increments the turnPlayerIndex to move to the next player in the player list.
     * If the current player is the last player in the list, the turn wraps around to the first player.
     */
    public void newTurn() {
        turnPlayerIndex = (turnPlayerIndex + 1) % players.size();
    }

    /**
     * Places a {@link PlayableCard} on the current player's {@link PlayArea} at the specified position.
     *
     * If the card is flipped, it flips the card before placing it on the play area.
     *
     * After determining if the card's {@link GoldCard constraint}, if present, is satisfied,
     * it places the card on the play area at the specified position.
     *
     * Additionally, calculates the points earned by placing the card
     * and assigns them to the current {@link Player}.
     *
     * @param c       The {@link PlayableCard} to be placed.
     * @param pos     The {@link Coordinate} where the card should be placed on the {@link PlayArea}.
     * @param flipped A boolean indicating whether the card should be flipped before placement.
     * @throws InvalidConstraintException If the selected card does not satisfy its constraint for the current player.
     */
    public void placeCard(PlayableCard c, Coordinate pos, boolean flipped) throws InvalidConstraintException {

        Player currPlayer = players.get(turnPlayerIndex);
        selectCard(c, pos, currPlayer.getUsername());

        boolean placeable;
        if (c.getCardType().equals(CardType.GOLD) && !flipped) {
            placeable = currPlayer.getPlayArea().checkConstraint(c);
            if (!placeable)
                throw new InvalidConstraintException("The selected card does not satisfy its constraint for the current Player");
        } else if (flipped) {
            c.flipCard();
        }

        currPlayer.getPlayArea().placeCard(c, pos, flipped);

        int points = c.getEvaluator().calculatePoints(currPlayer.getPlayArea());

        if (points != 0)
            assignPoints(currPlayer, points);

    }

    /**
     * TODO JavaDoc
     * @param chosenDeck
     * @param chosenCard
     */
    public void drawPlayableCard(CardType chosenDeck, int chosenCard) {

        PlayableCard drawnCard, newVisible;

        if (chosenDeck.equals(CardType.GOLD)) {
            if (chosenCard >= 0 && chosenCard < 2) { // TODO constants?
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

        if (drawnCard == null) throw new EmptyDeckException(); // TODO when controller is implemented, make non runtimeException?

        Player currPlayer = players.get(turnPlayerIndex);
        currPlayer.getPlayArea().addToHand(drawnCard);

        if (resourceDeck.getSize() == 0 && goldDeck.getSize() == 0) finalPhase = true;

    }

    /**
     * Selects the given {@link EvaluableCard} in the {@link PlayArea} of the specified {@link Player}.
     *
     * @param c    The {@link EvaluableCard} that needs to be selected.
     * @param pos  The {@link Coordinate} of the card that needs to be selected.
     *             Can be null if the card to be selected is a {@link ObjectiveCard}.
     * @param user The username of the {@link Player} that selected the card.
     */
    public void selectCard(EvaluableCard c, Coordinate pos, String user) {
        Player p = getPlayerFromUsername(user);
        if (p == null) throw new PlayerNotFoundException();

        p.getPlayArea().setSelectedCard(pos, c);
    }

    /**
     * Assigns the given points to the selected {@link Player} in the {@link Game#scoreboard}.
     * If the updated score is at least 20, the game updates his {@link Game#finalPhase} status.
     * The maximum score of the {@link Game#scoreboard} is 29.
     *
     * @param p      The player to assign the points to.
     * @param points The number of points to assign.
     */
    private void assignPoints(Player p, int points) {

        int currScore = scoreboard.get(p);
        if (currScore == 29) return;

        currScore += points;

        if (currScore >= 20 && !finalPhase) finalPhase = true; // TODO modify if changed GameStatus
        if (currScore > 29) currScore = 29;

        scoreboard.put(p, currScore);
    }

    /**
     * Sets the {@link Player}'s online status as false.
     * @param user The Player's username.
     */
    public void offlinePlayer(String user) {
        Player p = getPlayerFromUsername(user);
        if (p == null) throw new PlayerNotFoundException();
        p.setOnline(false);
    }

    /**
     * Sets the {@link Player}'s online status as true.
     * @param user The Player's username.
     */
    public void reconnectPlayer(String user) {
        Player p = getPlayerFromUsername(user);
        if (p == null) throw new PlayerNotFoundException();
        p.setOnline(true);
    }

    /**
     * Ends the game and determines the winner based on objective points earned by {@link Player Players}.
     *
     * This method calculates and assigns the total objective points earned by each player
     * by evaluating the {@link ObjectiveCard ObjectiveCards}.
     *
     * @return The {@link Player} who has the highest total objective points and is declared as the winner of the game.
     */
    public Player endGame() { // TODO string instead of Player?
        // TODO ENDED gameStatus?
        Map<Player, Integer> objPoints = new HashMap<>();
        int playerObjPoints;

        for (Player p : players) {
            playerObjPoints = calculateObjectives(p);
            assignPoints(p, playerObjPoints);

            objPoints.put(p, playerObjPoints);
        }

        return getWinner(objPoints);
    }

    /**
     * Calculates the total points earned by a {@link Player} from both {@link Game#commonObjectives} and {@link Player  secret objective} cards.
     * @param p The player for whom the total objective points are to be calculated.
     * @return  The total points earned by the player.
     */
    private int calculateObjectives(Player p) {
        int objPoints = 0;
        for (ObjectiveCard c: commonObjectives)
            objPoints += c.getEvaluator().calculatePoints(p.getPlayArea());
        objPoints += p.getSecretObjective().getEvaluator().calculatePoints(p.getPlayArea());
        return objPoints;
    }

    /**
     * Determines the game's winner based on the points scored by players and their points scored from objective cards.
     *
     * @param objPoints A map representing the points scored by players from objective cards.
     * @return The player with the highest score, the game winner.
     */
    private Player getWinner(Map<Player, Integer> objPoints) {
        List<Player> currWinners = new ArrayList<>();
        int currMaxPoints = 0;

        // getting Players with the most points
        for (Map.Entry<Player, Integer> entry : scoreboard.entrySet()) {
            if (entry.getValue() > currMaxPoints) {
                currWinners.clear();
                currWinners.add(entry.getKey());
            } else if (entry.getValue() == currMaxPoints) {
                currWinners.add(entry.getKey());
            }
            currMaxPoints = scoreboard.get(currWinners.getFirst());
        }

        // checking which player, from the ones with the same points, scored the most points from ObjectiveCards
        if (currWinners.size() != 1) {
            for (Player p : objPoints.keySet()) {
                if (!currWinners.contains(p)) {
                    objPoints.remove(p);
                }
            }
            currWinners.clear();
            currWinners.add(objPoints.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null));
        }
        if (currWinners.getFirst() == null) throw new NoWinnerException("No winner could be found");

        return currWinners.getFirst();
    }

    /**
     * Retrieves the Game id.
     * @return {@link Game#id gameId}.
     */
    public int getId() {
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
     * Retrieves a player object based on the provided username.
     *
     * @param user The username of the player to retrieve.
     * @return The {@link Player} associated with the given username.
     */
    private Player getPlayerFromUsername(String user) {
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
    public Map<Player, Integer> getScoreboard() { // TODO String instead of Player?
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
     * TODO game status getter
     */
    public boolean isFinalPhase() {
        return finalPhase;
    }

}