package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.EndedGameData;
import it.polimi.ingsw.viewModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmStartCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndedGameController extends FXMLController {

    @FXML
    private ImageView topGoldCard;

    @FXML
    private ImageView visibleGoldCard0;

    @FXML
    private ImageView visibleGoldCard1;

    @FXML
    private ImageView topResourceCard;

    @FXML
    private ImageView visibleResourceCard0;

    @FXML
    private ImageView visibleResourceCard1;

    @FXML
    private Label gameName;

    @FXML
    private ImageView handCard0;

    @FXML
    private ImageView handCard1;

    @FXML
    private ImageView handCard2;

    @FXML
    private ImageView commonObjective0;

    @FXML
    private ImageView commonObjective1;

    @FXML
    private ImageView secretObjective;

    @FXML
    private HBox player1;

    @FXML
    private HBox player2;

    @FXML
    private HBox player3;

    @FXML
    private HBox player4;

    @FXML
    private Text username1;

    @FXML
    private Text username2;

    @FXML
    private Text username3;

    @FXML
    private Text username4;

    @FXML
    private Text points1;

    @FXML
    private Text points2;

    @FXML
    private Text points3;

    @FXML
    private Text points4;

    @FXML
    private ImageView token1;

    @FXML
    private ImageView token2;

    @FXML
    private ImageView token3;

    @FXML
    private ImageView token4;

    @FXML
    private ComboBox<String> playAreaSelection;


    @FXML
    private GridPane gridPane;



    private List<ImageView> hand;

    private List<ImageView> tokens;

    private List<Text> points;

    private List<Text> usernames;

    private List<HBox> players;

    private List<ImageView> visibleResourceCards;

    private List<ImageView> visibleGoldCards;

    private EndedGameData endedGameData;

    private Map<Coordinate, Node> emptyPanesMap;


    public EndedGameController(){
        super();
    }

    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        endedGameData = controller.getEndedGameData();
        emptyPanesMap = new HashMap<>();

        hand = Arrays.asList(handCard0, handCard1, handCard2);
        visibleResourceCards = Arrays.asList(visibleResourceCard0, visibleResourceCard1);
        visibleGoldCards = Arrays.asList(visibleGoldCard0, visibleGoldCard1);
        players = Arrays.asList(player1, player2, player3, player4);
        usernames = Arrays.asList(username1, username2, username3, username4);
        points = Arrays.asList(points1, points2, points3, points4);
        tokens = Arrays.asList(token1, token2, token3, token4);

        setGameName();
        updateVisiblePlayAreasOptions();

        showDecks();
        showPlayers();
        showPlayArea();
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }

    @FXML
    public void showPlayArea(){
        String playAreaChoice = playAreaSelection.getValue();
        if(playAreaChoice.equals("Your board")){
            showHand(controller.getUsername());
            showGridPane(controller.getUsername());
        }
        else{
            String username = playAreaChoice.substring(10);
            showHand(username);
            showGridPane(username);
        }
    }

    private void showGridPane(String username){
        int maxX, minX, maxY, minY;
        maxX = 0;
        minX = 0;
        maxY = 0;
        minY = 0;
        gridPane.getChildren().clear();
        emptyPanesMap.clear();

        for(Coordinate coordinate : endedGameData.getPlayAreas().get(username).getPlayedCards().keySet()){
            int currX = coordinate.getX();
            int currY = coordinate.getY();
            if(currX > maxX){
                maxX = currX;
            }
            else if(currX < minX){
                minX = currX;
            }
            if(currY > maxY){
                maxY = currY;
            }
            else if(currY < minY){
                minY = currY;
            }
        }
        int gridLength = (maxX + 1) - (minX - 1);
        int gridHeight = (maxY + 1) - (minY - 1);

        //Add empty slots
        for(int j = 0; j <= gridHeight; j++) {
            for (int k = 0; k <= gridLength; k++) {
                Image image = new Image("it/polimi/ingsw/images/cards/playable/Empty.png");
                StackPane emptyStackPane = stackPaneBuilder(image);
                gridPane.add(emptyStackPane, k, j);
                emptyPanesMap.put(new Coordinate(k, j), emptyStackPane);
            }
        }

        //Add start card
        ImmStartCard immStartCard = endedGameData.getPlayAreas().get(username).getStartCard();
        StackPane startStackPane;
        Image image;
        if (!immStartCard.isFlipped()) {
            image = new Image("it/polimi/ingsw/images/cards/playable/start/front/" + immStartCard.getId() + ".png");
        } else {
            image = new Image("it/polimi/ingsw/images/cards/playable/start/back/" + immStartCard.getId() + ".png");
        }
        startStackPane = stackPaneBuilder(image);
        Node startNodeToRemove = emptyPanesMap.get(new Coordinate(-minX + 1, maxY + 1));
        gridPane.getChildren().remove(startNodeToRemove);
        gridPane.add(startStackPane, -minX + 1, maxY + 1);

        //Adds the played cards in the grid
        for(Coordinate coordinate : endedGameData.getPlayAreas().get(username).getPlayedCards().keySet()) {
            int k = coordinate.getX() - minX + 1;
            int j = maxY - coordinate.getY() + 1;

            ImmPlayableCard immPlayableCard = endedGameData.getPlayAreas().get(username).getPlayedCards().get(coordinate);
            StackPane playedStackPane;
            if (!immPlayableCard.isFlipped()) {
                image = getFrontCardImage(immPlayableCard.getId(), immPlayableCard.getType());
            } else {
                image = getBackCardImage(immPlayableCard.getPermanentResource(), immPlayableCard.getType());
            }
            playedStackPane = stackPaneBuilder(image);
            Node nodeToRemove = emptyPanesMap.get(new Coordinate(k, j));
            gridPane.getChildren().remove(nodeToRemove);
            gridPane.add(playedStackPane, k, j);
        }
    }

    private void showDecks(){
        ImmPlayableCard[] visibleGoldCards = endedGameData.getVisibleGoldCards();
        ImmPlayableCard[] visibleResourceCards = endedGameData.getVisibleResourceCards();
        int i = 0;
        for(ImmPlayableCard card : visibleGoldCards){
            this.visibleGoldCards.get(i).setVisible(true);
            this.visibleGoldCards.get(i).setImage(getFrontCardImage(card.getId(), card.getType()));
            i++;
        }
        i = 0;
        for(ImmPlayableCard card : visibleResourceCards){
            this.visibleResourceCards.get(i).setVisible(true);
            this.visibleResourceCards.get(i).setImage(getFrontCardImage(card.getId(), card.getType()));
            i++;
        }
        if(endedGameData.getTopGoldDeck() != null){
            topGoldCard.setVisible(true);
            topGoldCard.setImage(getBackCardImage(endedGameData.getTopGoldDeck(), CardType.GOLD));
        }
        if(endedGameData.getTopResourceDeck() != null){
            topResourceCard.setVisible(true);
            topResourceCard.setImage(getBackCardImage(endedGameData.getTopResourceDeck(), CardType.RESOURCE));
        }

        ImmObjectiveCard objectiveCard0 = endedGameData.getCommonObjectives()[0];
        commonObjective0.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveCard0.getId() + ".png"));
        ImmObjectiveCard objectiveCard1 = endedGameData.getCommonObjectives()[1];
        commonObjective1.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveCard1.getId() + ".png"));
    }

    private void showPlayers(){
        int i = 0;
        for(String username : endedGameData.getResults()){
            players.get(i).setVisible(true);
            usernames.get(i).setText(username);
            points.get(i).setText(endedGameData.getScoreboard().get(username).toString());
            Token token = endedGameData.getPlayerTokens().get(username);
            tokens.get(i).setImage(getTokenImage(token));
            i++;
        }
    }

    private void showHand(String username){
        int i = 0;
        for(ImmPlayableCard card : endedGameData.getPlayAreas().get(username).getHand()){
            this.hand.get(i).setVisible(true);
            this.hand.get(i).setImage(getFrontCardImage(card.getId(), card.getType()));
            i++;
        }
        while(i < this.hand.size()){
            hand.get(i).setVisible(false);
            i++;
        }
        ImmObjectiveCard secretObjCard = endedGameData.getSecretObjectives().get(username);
        secretObjective.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + secretObjCard.getId() + ".png"));
    }

    private void updateVisiblePlayAreasOptions(){
        playAreaSelection.getItems().clear();

        playAreaSelection.getItems().add("Your board");
        playAreaSelection.getSelectionModel().select("Your board");

        for(String user : endedGameData.getResults()){
            if(!controller.getUsername().equals(user)){
                playAreaSelection.getItems().add("Opponent: " + user);
            }
        }
    }

    private void setGameName() {
        this.gameName.setText("GAME:" + controller.getLobbyId());
    }

    @FXML
    public void goMainMenu(ActionEvent e){
        try {
            switchScreen("Menu");
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @Override
    public boolean inGame(){
        return true;
    }
}
