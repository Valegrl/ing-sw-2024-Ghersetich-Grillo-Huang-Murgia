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
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for controlling the GUI during the end game phase.
 * It extends the FXMLController class and overrides its methods to provide the specific functionality needed for the end game phase.
 * It handles the display of the game board, the players' scores, the decks of cards, and the players' hands.
 * It also provides options to view different play areas.
 */
public class EndedGameController extends FXMLController {

    @FXML
    private BorderPane borderPane;

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

    /**
     * The data for the ended game. It includes information
     * such as the players' scores, the state of the play areas, the visible and top cards of the decks,
     * and the common and secret objectives. This data is used to update the GUI during the end game phase.
     */
    private EndedGameData endedGameData;

    /**
     * A map that associates each empty pane in the grid with its corresponding coordinate.
     * This is used to keep track of the empty panes in the grid, allowing for efficient updates
     * when cards are played or moved.
     */
    private Map<Coordinate, Node> emptyPanesMap;

    /**
     * Initializes the controller. Sets up the bindings for the image views.
     */
    public void initialize() {
        double relativePercentageW = 0.07;

        List<ImageView> imageViews = Arrays.asList(topResourceCard, visibleResourceCard0, visibleResourceCard1,
                topGoldCard, visibleGoldCard0, visibleGoldCard1,
                handCard0, handCard1, handCard2,
                commonObjective0, commonObjective1, secretObjective);

        for (ImageView imageView : imageViews) {
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() ->
                    borderPane.getWidth() * relativePercentageW, borderPane.widthProperty()
            ));
        }
    }

    public EndedGameController() {
        super();
    }

    /**
     * Runs the controller. Sets up the view, stage, and controller. Retrieves the ended game data and calls the methods to update the play areas.
     *
     * @param view  The view associated with this controller
     * @param stage The stage in which the FXML view is shown
     */
    @Override
    public void run(View view, Stage stage) {
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

        updateVisiblePlayAreasOptions();

        showDecks();
        showPlayers();
        showPlayArea();
    }

    /**
     * Handles the response from the server. This method is not used in this controller.
     *
     * @param feedback The feedback from the server
     * @param message  The message associated with the feedback
     * @param eventID  The ID of the event
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }

    /**
     * Shows the play area of the selected player.
     */
    @FXML
    public void showPlayArea() {
        String playAreaChoice = playAreaSelection.getValue();
        if (playAreaChoice.equals("Your board")) {
            showHand(controller.getUsername());
            showGridPane(controller.getUsername());
        } else {
            String username = playAreaChoice.substring(10);
            showHand(username);
            showGridPane(username);
        }
    }

    /**
     * Displays the grid pane for a given user's play area. This includes the empty slots, the start card,
     * the user's token on the start card, a black token if the turn starts from the user, and the played cards.
     *
     * @param username The username of the player whose play area is to be displayed.
     */
    private void showGridPane(String username) {
        gridPane.getChildren().clear();
        emptyPanesMap.clear();

        int gridLength = calculateGridLength(endedGameData.getPlayAreas().get(username).getPlayedCards());
        int gridHeight = calculateGridHeight(endedGameData.getPlayAreas().get(username).getPlayedCards());
        int minX = calculateMinX(endedGameData.getPlayAreas().get(username).getPlayedCards());
        int maxY = calculateMaxY(endedGameData.getPlayAreas().get(username).getPlayedCards());

        //Add empty slots
        for (int j = 0; j <= gridHeight; j++) {
            for (int k = 0; k <= gridLength; k++) {
                Image image = new Image("it/polimi/ingsw/images/cards/playable/Empty2.png", getWCardRes(), getHCardRes(), true, true);
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
            image = new Image("it/polimi/ingsw/images/cards/playable/start/front/" + immStartCard.getId() + ".png", getWCardRes(), getHCardRes(), true, true);
        } else {
            image = new Image("it/polimi/ingsw/images/cards/playable/start/back/" + immStartCard.getId() + ".png", getWCardRes(), getHCardRes(), true, true);
        }
        startStackPane = stackPaneBuilder(image);
        Node startNodeToRemove = emptyPanesMap.get(new Coordinate(-minX + 1, maxY + 1));
        gridPane.getChildren().remove(startNodeToRemove);
        gridPane.add(startStackPane, -minX + 1, maxY + 1);

        //Add token on start card
        Token userToken = controller.getEndedGameData().getPlayerTokens().get(username);
        Image imageToken = getTokenImage(userToken);
        ImageView newImageView = new ImageView(imageToken);
        newImageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() ->
                borderPane.getWidth() * 0.02, borderPane.widthProperty()
        ));
        newImageView.setPreserveRatio(true);
        newImageView.translateXProperty().bind(startStackPane.widthProperty().multiply(0.2));
        startStackPane.getChildren().add(newImageView);

        //Add black token if the turn-circle start from user
        if (username.equals(controller.getPlayerUsernames().getFirst())) {
            Image blackToken = getBlackToken();
            ImageView tokenView = new ImageView(blackToken);
            tokenView.fitWidthProperty().bind(Bindings.createDoubleBinding(() ->
                    borderPane.getWidth() * 0.02, borderPane.widthProperty()
            ));
            tokenView.setPreserveRatio(true);
            tokenView.translateXProperty().bind(startStackPane.widthProperty().multiply(-0.2));
            startStackPane.getChildren().add(tokenView);
        }

        //Adds the played cards in the grid
        for (Coordinate coordinate : endedGameData.getPlayAreas().get(username).getPlayedCards().keySet()) {
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

    /**
     * Shows the decks of cards.
     */
    private void showDecks() {
        ImmPlayableCard[] visibleGoldCards = endedGameData.getVisibleGoldCards();
        ImmPlayableCard[] visibleResourceCards = endedGameData.getVisibleResourceCards();

        for (int i = 0; i < visibleGoldCards.length; i++) {
            if (visibleGoldCards[i] != null) {
                this.visibleGoldCards.get(i).setVisible(true);
                this.visibleGoldCards.get(i).setImage(getFrontCardImage(visibleGoldCards[i].getId(), visibleGoldCards[i].getType()));
            } else this.visibleGoldCards.get(i).setVisible(false);
        }

        for (int i = 0; i < visibleResourceCards.length; i++) {
            if (visibleResourceCards[i] != null) {
                this.visibleResourceCards.get(i).setVisible(true);
                this.visibleResourceCards.get(i).setImage(getFrontCardImage(visibleResourceCards[i].getId(), visibleResourceCards[i].getType()));
            } else this.visibleResourceCards.get(i).setVisible(false);
        }
        if (endedGameData.getTopGoldDeck() != null) {
            topGoldCard.setVisible(true);
            topGoldCard.setImage(getBackCardImage(endedGameData.getTopGoldDeck(), CardType.GOLD));
        }
        if (endedGameData.getTopResourceDeck() != null) {
            topResourceCard.setVisible(true);
            topResourceCard.setImage(getBackCardImage(endedGameData.getTopResourceDeck(), CardType.RESOURCE));
        }

        ImmObjectiveCard objectiveCard0 = endedGameData.getCommonObjectives()[0];
        commonObjective0.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveCard0.getId() + ".png", getWCardRes(), getHCardRes(), true, true));
        ImmObjectiveCard objectiveCard1 = endedGameData.getCommonObjectives()[1];
        commonObjective1.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveCard1.getId() + ".png", getWCardRes(), getHCardRes(), true, true));
    }

    /**
     * Shows the players and their scores.
     */
    private void showPlayers() {
        int i = 0;
        for (String username : endedGameData.getResults()) {
            players.get(i).setVisible(true);
            usernames.get(i).setText(username);
            points.get(i).setText(endedGameData.getScoreboard().get(username).toString());
            Token token = endedGameData.getPlayerTokens().get(username);
            tokens.get(i).setImage(getTokenImage(token));
            i++;
        }
    }

    /**
     * Shows the hand of the selected player.
     *
     * @param username The username of the selected player
     */
    private void showHand(String username) {
        int i = 0;
        for (ImmPlayableCard card : endedGameData.getPlayAreas().get(username).getHand()) {
            this.hand.get(i).setVisible(true);
            this.hand.get(i).setImage(getFrontCardImage(card.getId(), card.getType()));
            i++;
        }
        while (i < this.hand.size()) {
            hand.get(i).setVisible(false);
            i++;
        }
        ImmObjectiveCard secretObjCard = endedGameData.getSecretObjectives().get(username);
        secretObjective.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + secretObjCard.getId() + ".png", getWCardRes(), getHCardRes(), true, true));
    }

    /**
     * Updates the options to view which play area.
     */
    private void updateVisiblePlayAreasOptions() {
        playAreaSelection.getItems().clear();

        playAreaSelection.getItems().add("Your board");
        playAreaSelection.getSelectionModel().select("Your board");

        for (String user : endedGameData.getResults()) {
            if (!controller.getUsername().equals(user)) {
                playAreaSelection.getItems().add("Opponent: " + user);
            }
        }
    }

    /**
     * Handles the action of going back to the main menu. It loads a new FXML scene.
     *
     * @throws RuntimeException if there is an IOException when switching the screen.
     */
    @FXML
    public void goMainMenu() {
        try {
            switchScreen("Menu");
        } catch (IOException exception) {
            throw new RuntimeException("FXML Exception: failed to load Menu", exception);
        }
    }

    /**
     * Indicates whether the user is in the game.
     *
     * @return true since the user is in the ended game when this controller is active.
     */
    @Override
    public boolean inGame() {
        return true;
    }
}
