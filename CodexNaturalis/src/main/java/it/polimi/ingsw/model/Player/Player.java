package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.Card.ObjectiveCard;
import it.polimi.ingsw.model.Card.StartCard;
import it.polimi.ingsw.utils.Coordinate;

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

    public boolean getStatus(){
        return this.online;
    }

    public PlayArea getPlayArea(){
        return this.playArea;
    }

    public ObjectiveCard getSecretObjective(){
        return this.secretObjective;
    }



    /**
     * Initializes player's playArea
     * @param c the startCard that a player selects upon starting the game
     */
    public PlayArea start(StartCard c) {


        Coordinate coordinate = new Coordinate(0, 0);
        PlayArea playArea = new PlayArea();
        playArea.getPlayedCards().put(coordinate, c);

        if(c.isFlipped()){

        }
        else{

        }


        return playArea;
    }


    /**
     * Shows the untaken tokens and then assigns the chosen token to the player
     * @param t the list of available tokens a player can choose from
     */
    public void chooseToken(List<Token> t) {
        //TODO needs the player's controller
    }

    //*second possible way to implement chooseToken*
    /*il game dovrebbe assegnare il token dei players*/


    /**
     * Assigns a secret objectiveCard to the player which other players can't see
     * @param a one of the two possible objectiveCard the player can choose at the start
     * @param b one of the two possible objectiveCard the player can choose at the start
     */
    public void chooseSecret(ObjectiveCard a, ObjectiveCard b) {
        //TODO needs the player's controller
    }

}
