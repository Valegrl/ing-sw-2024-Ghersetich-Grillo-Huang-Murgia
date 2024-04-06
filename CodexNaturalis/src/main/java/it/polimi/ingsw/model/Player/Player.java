package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.Card.*;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.model.exceptions.IllegalFirstHandException;

import java.util.Iterator;
import java.util.List;


/**
 * A class to represent a player in-game.
 */
public class Player {

    /**
     * The player's username.
     */
    private final String username;

    /**
     * The player's colored token which can be either red, blue, green or yellow.
     */
    private Token token;

    /**
     * The player's own {@link PlayArea} which contains all the data and methods regarding the game's execution.
     */
    private PlayArea playArea;

    /**
     * The player's personal objective.
     */
    private ObjectiveCard secretObjective;

    /**
     * The player's connection status.
     */
    private boolean online;

    /**
     * Constructs a new instance of Player given the username the player chose.
     *
     * @param username The string given to set the player's username.
     */
    public Player(String username){
        this.username = username;
        this.online = true;
    }

    /**
     * Retrieves the player's chosen username.
     * @return {@link Player#username}.
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Retrieves the player's colored token.
     * @return {@link Player#token}.
     */
    public Token getToken(){
        return this.token;
    }

    /**
     * Checks if the player is online or offline.
     * @return {@code true} if the player is online, {@code false} if offline.
     */
    public boolean isOnline(){
        return this.online;
    }

    /**
     * Retrieves the player's own play area.
     * @return {@link Player#playArea}.
     */
    public PlayArea getPlayArea(){
        return this.playArea;
    }

    /**
     * Retrieves the player's secret objective card.
     * @return {@link Player#secretObjective}
     */
    public ObjectiveCard getSecretObjective(){
        return this.secretObjective;
    }

    /**
     * Sets the {@link Token} to this player.
     * @param token The {@link Token} to set.
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * Sets the {@link ObjectiveCard secret objective} to this player.
     * @param secretObjective The {@link ObjectiveCard secret objective} which this player chose at the game's start.
     */
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    /**
     * Sets this player's connection status.
     * @param online It indicates whether this player has to be set online {@code online = true} or not {@code online = false}.
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * Initializes player's playArea, which contains all the methods and data regarding the game's execution.
     *
     * @param hand The cards a player has at the game's start. It has to be 1 {@link GoldCard gold card} and 2 {@link ResourceCard resource cards}.
     * @param c The {@link StartCard start card} that a player selects upon starting the game, it gets placed on the play area at (0, 0).
     * @throws IllegalFirstHandException If the player's starting hand isn't composed of 1 {@link GoldCard gold card} and 2 {@link ResourceCard resource cards}.
     */
    public void initPlayArea(List<PlayableCard> hand, StartCard c)throws IllegalFirstHandException{
        if(hand.size() != 3){ // TODO constants ?
            throw new IllegalFirstHandException();
        }

        int goldCounter = 0;
        int resourceCounter = 0;

        for(PlayableCard currCard : hand){
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
