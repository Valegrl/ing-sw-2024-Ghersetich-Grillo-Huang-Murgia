package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Deck.ResourceDeck;
import it.polimi.ingsw.model.Deck.GoldDeck;
import java.util.List;
import java.util.Map;
import it.polimi.ingsw.model.Card.ObjectiveCard;
import it.polimi.ingsw.model.Card.PlayableCard;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.model.Card.EvaluableCard;
import it.polimi.ingsw.model.Player.Player;

public class Game {

	private int id;

	private ResourceDeck resourceDeck;

	private GoldDeck goldDeck;

	private List players;

	private int currTurn;

	private Map scoreboard;

	private ObjectiveCard[] commonObjectives;

	private boolean finalPhase;

	private ObjectiveCard[] objectiveCard;

	public void gameSetup() {

	}

	public void newTurn() {

	}

	public boolean placeCard(PlayableCard c, Coordinate coord, boolean flipped) {
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
