package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Card.CardType;
import it.polimi.ingsw.model.Deck.ResourceDeck;
import it.polimi.ingsw.model.Deck.GoldDeck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.model.Card.ObjectiveCard;
import it.polimi.ingsw.model.Card.PlayableCard;
import it.polimi.ingsw.model.Player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.model.Card.EvaluableCard;
import it.polimi.ingsw.model.Player.Player;

/**
 * A class to represent a Codex Naturalis game.
 */
public class Game {
    /**
     * The Game's identifier.
     */
    private final int id;

    /**
     * The Game's {@link it.polimi.ingsw.model.Card.ResourceCard resource cards} deck.
     */
    private final ResourceDeck resourceDeck;

    /**
     * The Game's {@link it.polimi.ingsw.model.Card.GoldCard gold cards} deck.
     */
    private final GoldDeck goldDeck;

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
     * @param players The list of chosen usernames by players.
     */
    public Game(int id, List<String> players) {
        this.id = id;

        this.resourceDeck = new ResourceDeck(); //TODO when implemented ResourceDeck constructor
        this.goldDeck = new GoldDeck(); // TODO when implemented GoldDeck constructor

        this.turnPlayerIndex = 0;

        this.players = new ArrayList<>();
        for (String user : players)
            this.players.add(new Player(user));

        this.scoreboard = new HashMap<>();
        for (Player p : this.players)
            this.scoreboard.put(p, 0);

        this.commonObjectives = new ObjectiveCard[2];

        this.finalPhase = false; //TODO game status enum?
    }

    /**
     * Retrieves the Game id.
     * @return {@link Game#id gameId}.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves Game's resource deck.
     * @return {@link Game#resourceDeck}.
     */
    public ResourceDeck getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Retrieves Game's gold deck.
     * @return {@link Game#goldDeck}.
     */
    public GoldDeck getGoldDeck() {
        return goldDeck;
    }

    /**
     * Retrieves the list of {@link Player players} in the Game.
     * @return {@link Game#players}.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Retrieves the index of the Player that currently has the turn.
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
     * TODO game status getter
     */
    public boolean isFinalPhase() {
        return finalPhase;
    }

    public void gameSetup() {

    }

    /**
     * Advances the game to the next player's turn.
     * This method increments the turnPlayerIndex to move to the next player in the player list.
     * If the current player is the last player in the list, the turn wraps around to the first player.
     */
    public void newTurn() {
        this.turnPlayerIndex = (this.turnPlayerIndex + 1) % this.players.size();
    }

    public boolean placeCard(PlayableCard c, Coordinate pos, boolean flipped) {
        return false;
    }

    public boolean drawPlayableCard(CardType chosenDeck, int chosenCard) {
        return false;
    }

    public void selectCard(EvaluableCard c, Player p) {

    }

    private void assignPoints(Player p, int points) {

    }

    public void offlinePlayer(String p) {

    }

    public void reconnectPlayer(String p) {

    }

    public void endGame() {

    }

    private int calculateObjectives(Player p) {
        return 0;
    }

    private Player winner() {
        return null;
    }

}
