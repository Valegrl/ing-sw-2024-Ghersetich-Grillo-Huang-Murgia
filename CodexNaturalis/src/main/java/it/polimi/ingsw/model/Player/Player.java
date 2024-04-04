package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.Card.*;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.model.exceptions.IllegalFirstHandException;

import java.util.Iterator;
import java.util.List;


/**
 * A class to represent a player in-game
 */
public class Player {

    /**
     * The player's username
     */
    private final String username;

    /**
     * The player's colored token which can be either red, blue, green or yellow
     */
    private Token token;

    /**
     * The player's own playArea which contains all the data and methods regarding the game's execution
     */
    private PlayArea playArea;

    /**
     * The player's personal objective
     */
    private ObjectiveCard secretObjective;

    /**
     * The player's connection status
     */
    private boolean online;

    /**
     * Player constructor
     * @param username string given to set the player's username
     */
    public Player(String username){
        this.username = username;
        this.online = true;
    }

    public String getUsername(){
        return this.username;
    }

    public Token getToken(){
        return this.token;
    }

    public boolean isOnline(){
        return this.online;
    }

    public PlayArea getPlayArea(){
        return this.playArea;
    }

    public ObjectiveCard getSecretObjective(){
        return this.secretObjective;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * Initializes player's playArea, which contains all the methods and data regarding the game's execution.
     * @param hand the cards a player selects at the game's start.
     * @param c the startCard that a player selects upon starting the game, it gets placed on the PlayArea at (0, 0).
     */
    public void initPlayArea(List<PlayableCard> hand, StartCard c) throws IllegalFirstHandException {
        if(hand.size() != 3){ // TODO constants ?
            throw new IllegalFirstHandException();
        }

        int goldCounter = 0;
        int resourceCounter = 0;

        for(PlayableCard currCard : this.getPlayArea().getHand()){
            if(currCard.getCardType() == CardType.GOLD) {
                goldCounter++;
            }
            if(currCard.getCardType() == CardType.RESOURCE) {
                resourceCounter++;
            }
        }

        if(goldCounter != 1 || resourceCounter != 2){
            throw new IllegalFirstHandException();
        }

        this.playArea = new PlayArea(hand, c);
    }
    /*Removed the methods: chooseToken() and chooseSecret()*/
}
