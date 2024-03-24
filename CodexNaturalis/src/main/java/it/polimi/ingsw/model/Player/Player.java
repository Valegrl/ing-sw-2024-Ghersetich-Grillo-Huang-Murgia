package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.Card.ObjectiveCard;
import it.polimi.ingsw.model.Card.StartCard;
import java.util.List;

public class Player {

	private String username;

	private Token token;

	private PlayArea playArea;

	private ObjectiveCard secretObjective;

	private boolean online;

	private ObjectiveCard objectiveCard;

	public PlayArea start(StartCard c) {
		return null;
	}

	public void chooseToken(List t) {

	}

	public void chooseSecret(ObjectiveCard a, ObjectiveCard b) {

	}

}
