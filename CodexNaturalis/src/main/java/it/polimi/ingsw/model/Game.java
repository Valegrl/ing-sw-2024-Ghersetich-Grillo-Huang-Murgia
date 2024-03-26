package it.polimi.ingsw.model;

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

public class Game {

    private final int id;

    private final ResourceDeck resourceDeck;

    private final GoldDeck goldDeck;

    private final List<Player> players;

    private int turnPlayerIndex;

    private final Map<Player, Integer> scoreboard;

    private final ObjectiveCard[] commonObjectives;

    private boolean finalPhase; // TODO GameStatus?

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

        this.finalPhase = false; //TODO
    }

    public void gameSetup() {

    }

    public void newTurn() {

    }

    public boolean placeCard(PlayableCard c, Coordinate pos, boolean flipped) {
        return false;
    }

    public boolean drawPlayableCard(boolean chosenDeck, int chosenCard) {
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
