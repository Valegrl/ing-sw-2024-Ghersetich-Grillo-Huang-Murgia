package it.polimi.ingsw.view.gui.controller;


import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.DrawCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.PlaceCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.immutableCard.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for managing the in-game GUI in the Codex Naturalis application.
 * It extends the FXMLController abstract class and implements its abstract methods.
 * The class provides methods for updating the game state, handling user interactions, and rendering the game state on the GUI.
 * It interacts with the model through the controller to get the game state and send user actions.
 * The class uses JavaFX for the GUI and uses FXML for the layout of the GUI.
 * The GUI includes a grid for the player's play area and the opponent's play area, a hand of cards, a deck of cards, and a chat area.
 * The class also provides methods for drag and drop functionality for the cards and for submitting chat messages.
 */
public class InGameController extends FXMLController {

    @FXML
    private GridPane gridPane;

    @FXML
    private GridPane opponentGridPane;

    @FXML
    private ScrollPane gameScrollPane;

    @FXML
    private StackPane waitingPane;

    @FXML
    private HBox uncovered1;

    @FXML
    private HBox uncovered2;

    @FXML
    private HBox uncovered3;

    @FXML
    private HBox uncovered4;

    @FXML
    private Text uncoveredUsername1;

    @FXML
    private Text uncoveredUsername2;

    @FXML
    private Text uncoveredUsername3;

    @FXML
    private Text uncoveredUsername4;

    @FXML
    private Label playerPoints1;

    @FXML
    private Label playerPoints2;

    @FXML
    private Label playerPoints3;

    @FXML
    private Label playerPoints4;

    @FXML
    private Label animalOcc1;

    @FXML
    private Label animalOcc2;

    @FXML
    private Label animalOcc3;

    @FXML
    private Label animalOcc4;

    @FXML
    private Label fungiOcc1;

    @FXML
    private Label fungiOcc2;

    @FXML
    private Label fungiOcc3;

    @FXML
    private Label fungiOcc4;

    @FXML
    private Label plantOcc1;

    @FXML
    private Label plantOcc2;

    @FXML
    private Label plantOcc3;

    @FXML
    private Label plantOcc4;

    @FXML
    private Label insectOcc1;

    @FXML
    private Label insectOcc2;

    @FXML
    private Label insectOcc3;

    @FXML
    private Label insectOcc4;

    @FXML
    private Label manuscriptOcc1;

    @FXML
    private Label manuscriptOcc2;

    @FXML
    private Label manuscriptOcc3;

    @FXML
    private Label manuscriptOcc4;

    @FXML
    private Label inkwellOcc1;

    @FXML
    private Label inkwellOcc2;

    @FXML
    private Label inkwellOcc3;

    @FXML
    private Label inkwellOcc4;

    @FXML
    private Label quillOcc1;

    @FXML
    private Label quillOcc2;

    @FXML
    private Label quillOcc3;

    @FXML
    private Label quillOcc4;

    @FXML
    private ImageView token1;

    @FXML
    private ImageView token2;

    @FXML
    private ImageView token3;

    @FXML
    private ImageView token4;



    @FXML
    private ComboBox<String> chatSelection;

    @FXML
    private TextField chatInput;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextArea gameUpdatesArea;



    @FXML
    private ImageView commonObjectiveCard0;

    @FXML
    private ImageView commonObjectiveCard1;

    @FXML
    private ImageView secretObjectiveCard;

    @FXML
    private VBox secretObjectiveVBox;


    @FXML
    private ImageView visibleGoldCard0;

    @FXML
    private ImageView visibleGoldCard1;

    @FXML
    private ImageView goldDeck;

    @FXML
    private ImageView visibleResourceCard0;

    @FXML
    private ImageView visibleResourceCard1;

    @FXML
    private ImageView resourceDeck;


    @FXML
    private Label handLabel;

    @FXML
    private Button flipButton;

    @FXML
    private ImageView handCard0;

    @FXML
    private ImageView handCard1;

    @FXML
    private ImageView handCard2;

    @FXML
    private ImageView opponentHandCard0;

    @FXML
    private ImageView opponentHandCard1;

    @FXML
    private ImageView opponentHandCard2;



    @FXML
    private Label gameName;

    @FXML
    private Label aboveLabel;

    @FXML
    private ComboBox<String> playAreaSelection;



    private Map<Coordinate, Node> emptyPanesMap;

    private ImmPlayableCard selectedPlayableCard;

    private boolean playFlipped;

    private boolean hasToDraw;

    //Collection of JavaFX objects for ease of use
    private List<ImageView> visibleGoldCards;

    private List<ImageView> tokens;

    private List<ImageView> visibleResourceCards;

    private List<Label> playerPoints;

    private List<Label> animalOccurrences;

    private List<Label> fungiOccurrences;

    private List<Label> plantOccurrences;

    private List<Label> insectOccurrences;

    private List<Label> manuscriptOccurrences;

    private List<Label> inkwellOccurrences;

    private List<Label> quillOccurrences;

    private List<HBox> uncoveredList;

    private List<Text> uncoveredUsernames;

    private List<ImageView> handCardImages;

    private List<ImageView> opponentCardImages;



    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        this.hasToDraw = false;
        emptyPanesMap = new HashMap<>();

        handCardImages = Arrays.asList(handCard0, handCard1, handCard2);
        opponentCardImages = Arrays.asList(opponentHandCard0, opponentHandCard1,opponentHandCard2);
        tokens = Arrays.asList(token1, token2, token3, token4);
        uncoveredList = Arrays.asList(uncovered1, uncovered2, uncovered3, uncovered4);
        uncoveredUsernames = Arrays.asList(uncoveredUsername1, uncoveredUsername2, uncoveredUsername3, uncoveredUsername4);
        visibleGoldCards = Arrays.asList(visibleGoldCard0, visibleGoldCard1);
        visibleResourceCards = Arrays.asList(visibleResourceCard0, visibleResourceCard1);

        playerPoints = Arrays.asList(playerPoints1, playerPoints2, playerPoints3, playerPoints4);
        animalOccurrences = Arrays.asList(animalOcc1, animalOcc2, animalOcc3, animalOcc4);
        fungiOccurrences = Arrays.asList(fungiOcc1, fungiOcc2, fungiOcc3, fungiOcc4);
        plantOccurrences = Arrays.asList(plantOcc1, plantOcc2, plantOcc3, plantOcc4);
        insectOccurrences = Arrays.asList(insectOcc1, insectOcc2, insectOcc3, insectOcc4);
        inkwellOccurrences = Arrays.asList(inkwellOcc1, inkwellOcc2, inkwellOcc3, inkwellOcc4);
        quillOccurrences = Arrays.asList(quillOcc1, quillOcc2, quillOcc3, quillOcc4);
        manuscriptOccurrences = Arrays.asList(manuscriptOcc1, manuscriptOcc2, manuscriptOcc3, manuscriptOcc4);

        setGameName();
        updateAboveLabel();
        updateDecks();
        updatePoints();
        updateChatOptions();
        updateVisiblePlayAreasOptions();

        showCommonObjectives();
        showSecretObjective();

        showPlayArea();

        chatInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                submitMessage();
            }
        });
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch(EventID.getByID(eventID)){
            case SELF_PLACE_CARD:
                Platform.runLater(() -> {
                    hasToDraw = true;
                    updateAboveLabel();
                    showPlayArea();
                    //needed for adding listeners for choosing the card to draw
                    updateDecks();
                    updatePoints();
                    gameUpdatesArea.appendText(message +"\n");
                });
                break;
            case PLACE_CARD:
                if(feedback == Feedback.FAILURE) {
                    Platform.runLater(() -> {
                        showPlayArea();
                        gameUpdatesArea.appendText(message + "\n");
                    });
                }
                break;
            case OTHER_PLACE_CARD:
                Platform.runLater(() ->{
                    if(!playAreaSelection.getValue().equals("Your board")) {
                        showPlayArea();
                    }
                    updatePoints();
                    gameUpdatesArea.appendText(message + "\n");
                });
                break;
            case SELF_DRAW_CARD:
                Platform.runLater(() -> {
                    hasToDraw = false;
                    updateDecks();
                    updateHand();
                    gameUpdatesArea.appendText(message + "\n");
                });
                break;
            case OTHER_DRAW_CARD:
                Platform.runLater(() -> {
                    if(!playAreaSelection.getValue().equals("Your board")) {
                        showPlayArea();
                    }
                    updateDecks();
                    gameUpdatesArea.appendText(message + "\n");
                });
                break;
            case SELF_TURN_TIMER_EXPIRED:
                Platform.runLater(() -> {
                    gameUpdatesArea.appendText("Your turn-timer has expired.\nYour turn has been skipped.\n");
                });
                break;
            case NEW_TURN:
                Platform.runLater(() -> {
                    hasToDraw = false;
                    updateAboveLabel();
                    showPlayArea();
                    gameUpdatesArea.appendText(message + "\n");
                });
                break;
            case ENDED_GAME:
                Platform.runLater(() ->{
                    try {
                        switchScreen("EndedGamev2");
                    }
                    catch (IOException exception){
                        exception.printStackTrace();
                    }
                });
                break;
            case NEW_GAME_STATUS:
                Platform.runLater(() -> updateAboveLabel());
                break;
            case UPDATE_GAME_PLAYERS:
                Platform.runLater(() -> {
                    gameUpdatesArea.appendText(message + "\n");
                    updateAboveLabel();
                    updatePoints();
                    updateVisiblePlayAreasOptions();
                    updateDecks(); //Check if player disconnecting and other guy waiting can draw
                    updateChatOptions();
                    showPlayArea();
                });
                break;
            case CHAT_GM, CHAT_PM:
                if (feedback == Feedback.SUCCESS) {
                    String formattedMessage = message.replace("[1m", " ").replace("[0m","");
                    chatArea.appendText(formattedMessage + "\n");
                } else {
                    chatArea.appendText("Message unable to send!");
                }
                break;
            case QUIT_GAME:
                if(feedback == Feedback.SUCCESS){
                    Platform.runLater(() ->{
                        try {
                            switchScreen("Menu");
                        }
                        catch (IOException exception){
                            exception.printStackTrace();
                        }
                    });
                }
                break;
        }
        notifyResponse();
    }

    /**
     * This method flips the {@code playFlipped} variable and updates the hand shown.
     */
    @FXML
    public void setPlayFlipped(){
        playFlipped = !playFlipped;
        updateHand();
    }

    /**
     * This method is used to quit the current game.
     * It creates a new QuitGameEvent and sends it to the server through the controller.
     * After sending the event, it waits for a response from the server.
     */
    @FXML
    public void quit(){
        Event event = new QuitGameEvent();
        controller.newViewEvent(event);
        waitForResponse();
    }

    /**
     * This method sets up drag and drop functionality for each card ImageView in the player's hand and each card in the Decks.
     * When a card is dragged, it sets the selectedPlayableCard to the current card,
     * creates a new Dragboard and ClipboardContent, and adds the card's image and ID to the ClipboardContent.
     * The Dragboard then consumes the drag event.
     */
    private void addHandChoiceSelection(){
        int i = 0;
        // Setups drag and select for each card ImageView in the hand
        for(ImageView cardImageView : this.handCardImages) {
            if(i < controller.getModel().getSelfPlayer().getPlayArea().getHand().size()) {
                ImmPlayableCard currCard = controller.getModel().getSelfPlayer().getPlayArea().getHand().get(i);
                //Drag and drop (start drag)
                cardImageView.setOnDragDetected(event -> {
                    selectedPlayableCard = currCard;
                    Dragboard db = cardImageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();

                    //This is to set the Width and Height of the ImageView original
                    ImageView dragImageView = new ImageView(cardImageView.getImage());
                    dragImageView.setFitWidth(cardImageView.getFitWidth());
                    dragImageView.setFitHeight(cardImageView.getFitHeight());

                    SnapshotParameters parameters = new SnapshotParameters();
                    parameters.setFill(Color.TRANSPARENT);

                    content.putString(currCard.getId());
                    db.setContent(content);
                    db.setDragView(dragImageView.snapshot(parameters, null), dragImageView.getFitWidth()/2, dragImageView.getFitHeight()/2);
                    event.consume();
                });
            }
            i++;
        }
    }

    /**
     * This method adds click event listeners to the deck and visible cards.
     * If the player has a turn and needs to draw a card, it sets up the click event listeners.
     * When a deck or a visible card is clicked, it creates a new DrawCardEvent and sends it to the server through the controller.
     * After sending the event, it waits for a response from the server.
     * The method iterates over the visible gold and resource cards and sets up the click event listeners for each card.
     */
    private void addDeckChoiceSelection(){
        if(controller.hasTurn() && hasToDraw && controller.getModel().getGameStatus().equals(GameStatus.RUNNING)) {
            //These can't be interacted with if there is no card
            if(controller.getModel().getTopResourceDeck() != null){
                resourceDeck.setOnMouseClicked(event -> {
                    controller.newViewEvent(new DrawCardEvent(CardType.RESOURCE, 2));
                    waitForResponse();
                });
            }
            if(controller.getModel().getTopGoldDeck() != null){
                goldDeck.setOnMouseClicked(event -> {
                controller.newViewEvent(new DrawCardEvent(CardType.GOLD, 2));
                waitForResponse();
                });
            }

            ImmPlayableCard[] visibleGoldCards = controller.getModel().getVisibleGoldCards();
            ImmPlayableCard[] visibleResourceCards = controller.getModel().getVisibleResourceCards();

            for(int i = 0; i < visibleGoldCards.length; i ++){
                if(visibleGoldCards[i] != null) {
                    int finalI = i;
                    this.visibleGoldCards.get(i).setOnMouseClicked(event -> {
                        controller.newViewEvent(new DrawCardEvent(CardType.GOLD, finalI));
                    });
                }
            }

            for(int i = 0; i < visibleResourceCards.length; i ++){
                if(visibleResourceCards[i] != null) {
                    int finalI = i;
                    this.visibleResourceCards.get(i).setOnMouseClicked(event -> {
                        controller.newViewEvent(new DrawCardEvent(CardType.RESOURCE, finalI));
                    });
                }
            }
        }
    }

    /**
     * This method updates the decks by getting the visible gold and resource cards from the viewModel.
     * It sets the images of the visible cards and the top cards of the decks.
     * If there are less than 2 visible cards in either deck, it sets the remaining card slots to invisible.
     * It also checks if there are cards left in the gold and resource decks.
     * If there are, it sets the image of the top card of the deck.
     * If there aren't, it sets the deck to invisible.
     * After updating the decks, it adds the deck choice selection to the GUI.
     */
    private void updateDecks(){
        ImmPlayableCard[] visibleGoldCards = controller.getModel().getVisibleGoldCards();
        ImmPlayableCard[] visibleResourceCards = controller.getModel().getVisibleResourceCards();
        int i;
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.WHITE);
        highlight.setRadius(10.0);
        highlight.setOffsetX(0);
        highlight.setOffsetY(0);

        for(i = 0; i < visibleGoldCards.length; i++){
            //Updating the ImageViews of the gold deck
            if(visibleGoldCards[i] != null) {
                this.visibleGoldCards.get(i).setImage(getFrontCardImage(visibleGoldCards[i].getId(), visibleGoldCards[i].getType()));
                if(controller.hasTurn() && hasToDraw && controller.getGameStatus() == GameStatus.RUNNING){
                    this.visibleGoldCards.get(i).setEffect(highlight);
                }
                else{
                    this.visibleGoldCards.get(i).setEffect(null);
                }
            }
            else{
                this.visibleGoldCards.get(i).setImage(new Image("it/polimi/ingsw/images/cards/playable/NoCard.png"));
            }
        }

        for(i = 0; i < visibleResourceCards.length; i++){
            //Updating the ImageViews of the resource deck
            if(visibleResourceCards[i] != null) {
                this.visibleResourceCards.get(i).setImage(getFrontCardImage(visibleResourceCards[i].getId(), visibleResourceCards[i].getType()));
                if(controller.hasTurn() && hasToDraw && controller.getGameStatus() == GameStatus.RUNNING){
                    this.visibleResourceCards.get(i).setEffect(highlight);
                }
                else{
                    this.visibleResourceCards.get(i).setEffect(null);
                }
            }
            else{
                this.visibleResourceCards.get(i).setImage(new Image("it/polimi/ingsw/images/cards/playable/NoCard.png"));
            }
        }

        Item itemGoldDeck = controller.getModel().getTopGoldDeck();
        if(itemGoldDeck != null) {
            Image backGoldCardImage = getBackCardImage(itemGoldDeck, CardType.GOLD);
            goldDeck.setImage(backGoldCardImage);
            if(controller.hasTurn() && hasToDraw && controller.getGameStatus() == GameStatus.RUNNING){
                goldDeck.setEffect(highlight);
            }
            else{
                goldDeck.setEffect(null);
            }
        }
        else{
            goldDeck.setImage(new Image("it/polimi/ingsw/images/cards/playable/NoCard.png"));
        }

        Item itemResourceDeck = controller.getModel().getTopResourceDeck();
        if(itemResourceDeck != null) {
            Image backResourceCardImage = getBackCardImage(itemResourceDeck, CardType.RESOURCE);
            resourceDeck.setImage(backResourceCardImage);
            if(controller.hasTurn() && hasToDraw && controller.getGameStatus() == GameStatus.RUNNING){
                resourceDeck.setEffect(highlight);
            }
            else{
                resourceDeck.setEffect(null);
            }
        }
        else{
            resourceDeck.setImage(new Image("it/polimi/ingsw/images/cards/playable/NoCard.png"));
        }
        addDeckChoiceSelection();
    }

    /**
     * This method updates the hand by getting the hand from the viewModel.
     * It sets the images of the hand cards based on the playFlipped variable.
     */
    private void updateHand(){
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.WHITE);
        highlight.setRadius(10.0);
        highlight.setOffsetX(0);
        highlight.setOffsetY(0);

        //The fxml has 6 imageView, 3 for the current player's hand and 3 for the opponents
        for(ImageView handCard : opponentCardImages){
            handCard.setVisible(false);
            handCard.setManaged(false);
        }

        //Updates the imageView of this user based on the hand from the viewModel
        int i = 0;
        for(ImmPlayableCard card : controller.getModel().getSelfPlayer().getPlayArea().getHand()){
            ImageView currImageView = handCardImages.get(i);
            currImageView.setManaged(true);
            currImageView.setVisible(true);
            if (!playFlipped) {
                currImageView.setImage(getFrontCardImage(card.getId(), card.getType()));
            }
            else {
                currImageView.setImage(getBackCardImage(card.getPermanentResource(), card.getType()));
            }
            if(controller.hasTurn() && !hasToDraw && (controller.getGameStatus() == GameStatus.RUNNING || controller.getGameStatus() == GameStatus.LAST_CIRCLE)){
                currImageView.setEffect(highlight);
            }
            else{
                currImageView.setEffect(null);
            }
            i++;
        }
        //Hides the rest of the cards if the hand has less than 3 cards
        while(i < handCardImages.size()){
            handCardImages.get(i).setImage(null);
            handCardImages.get(i).setVisible(false);
            i++;
        }
        addHandChoiceSelection();
    }

    /**
     * This method updates the hand of the opponent in the GUI.
     * It first hides all the cards in the current player's hand and then makes the opponent's hand visible.
     * It then gets the back of the opponent's hand from the viewModel and sets the images of the opponent's hand cards.
     * If the opponent has less than 3 cards in their hand, it sets the remaining card slots to invisible.
     *
     * @param opponent The username of the opponent whose hand is to be updated.
     */
    private void updateOpponentHand(String opponent){
        for(ImageView handCard : handCardImages){
            handCard.setVisible(false);
            handCard.setManaged(false);
        }
        for(ImageView handCard : opponentCardImages){
            handCard.setManaged(true);
        }

        List<BackPlayableCard> backHand = controller.getModel().getOpponent(opponent).getPlayArea().getHand();
        int i = 0;
        for(BackPlayableCard card : backHand){
            ImageView currImageView = opponentCardImages.get(i);
            currImageView.setVisible(true);
            currImageView.setImage(getBackCardImage(card.getItem(), card.getCardType()));
            i++;
        }
        while(i < opponentCardImages.size()){
            opponentCardImages.get(i).setImage(null);
            opponentCardImages.get(i).setVisible(false);
            i++;
        }
    }

    /**
     * This method is used to display the play area of the current player or the selected opponent.
     * If the selected play area is "Your board", it makes the secret objective visible, sets the hand label to "MY HAND",
     * makes the flip button visible, updates the hand, and shows the current player's grid.
     * If the selected play area is an opponent's board, it hides the secret objective, sets the hand label to the "opponent's username + hand",
     * hides the flip button, updates the opponent's hand, and shows the opponent's grid.
     */
    @FXML
    public void showPlayArea() {
        String playAreaChoice = playAreaSelection.getValue();

        if(playAreaChoice.equals("Your board")){
            secretObjectiveVBox.setVisible(true);
            handLabel.setText("MY HAND");
            flipButton.setVisible(true);
            updateHand();
            showYourGridPane();
        }
        else{
            secretObjectiveVBox.setVisible(false);
            //Substring of 10 because of the annotation "Opponent: "
            String username = playAreaChoice.substring(10);
            handLabel.setText(username + " hand");
            flipButton.setVisible(false);
            updateOpponentHand(username);
            showOpponentGridPane(username);
        }
    }

    /**
     * This method is used to display the current player's grid on the GUI.
     * It first sets the visibility and management of the gridPane and opponentGridPane.
     * Then it calculates the maximum and minimum X and Y coordinates of the played cards.
     * It adds empty slots to the gridPane based on the calculated grid dimensions.
     * First it adds slots to the gridPane which are empty.
     * It then adds available slots to the gridPane where the player can place a card, subsequently it adds the start card and the played cards.
     * If the player has a turn and doesn't have to draw a card, it sets up drag and drop functionality for the available slots.
     */
    private void showYourGridPane(){
        gridPane.setManaged(true);
        gridPane.setVisible(true);
        opponentGridPane.setVisible(false);
        opponentGridPane.setManaged(false);

        opponentGridPane.getChildren().clear();
        gridPane.getChildren().clear();
        emptyPanesMap.clear();

        int gridLength = calculateGridLength(controller.getModel().getSelfPlayer().getPlayArea().getPlayedCards());
        int gridHeight = calculateGridHeight(controller.getModel().getSelfPlayer().getPlayArea().getPlayedCards());
        int minX = calculateMinX(controller.getModel().getSelfPlayer().getPlayArea().getPlayedCards());
        int maxY = calculateMaxY(controller.getModel().getSelfPlayer().getPlayArea().getPlayedCards());

        //Add empty slots
        for(int j = 0; j <= gridHeight; j++) {
            for (int k = 0; k <= gridLength; k++) {
                Image image = new Image("it/polimi/ingsw/images/cards/playable/Empty2.png");
                StackPane emptyStackPane = stackPaneBuilder(image);
                gridPane.add(emptyStackPane, k, j);
                emptyPanesMap.put(new Coordinate(k, j), emptyStackPane);
            }
        }

        //Add available slots
        for(Coordinate coordinate : controller.getModel().getSelfPlayer().getPlayArea().getAvailablePos()){
            int k = coordinate.getX() - minX + 1;
            int j = maxY - coordinate.getY() + 1;

            Image image = new Image("it/polimi/ingsw/images/cards/playable/Available.png");
            StackPane availableStackPane = stackPaneBuilder(image);
            Node nodeToRemove = emptyPanesMap.get(new Coordinate(k, j));
            gridPane.getChildren().remove(nodeToRemove);
            gridPane.add(availableStackPane, k, j);

            //For drag and drop of the card, this needed to be updated when the user has drawn or if it's not his turn.
            if(controller.hasTurn() && !hasToDraw && (controller.getModel().getGameStatus().equals(GameStatus.RUNNING) || controller.getModel().getGameStatus().equals(GameStatus.LAST_CIRCLE))) {
                availableStackPane.setOnDragOver(event -> {
                    if (event.getGestureSource() != availableStackPane) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                });
                availableStackPane.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()){
                        success = true;
                        controller.newViewEvent(new PlaceCardEvent(selectedPlayableCard.getId(), coordinate, playFlipped));
                        //For debugging purposes
                        System.out.println("Waiting for server response on card " + selectedPlayableCard.getId() + " " + coordinate.getX() + " " + coordinate.getY() );
                        waitForResponse();
                        return;
                    }
                    event.setDropCompleted(success);
                    event.consume();
                });
            }
        }

        //Add start card
        ImmStartCard immStartCard = controller.getModel().getSelfPlayer().getPlayArea().getStartCard();
        StackPane startStackPane;
        Image image;
        if (!immStartCard.isFlipped()) {
            image = new Image("it/polimi/ingsw/images/cards/playable/start/front/" + immStartCard.getId() + ".png");
        } else {
            image = new Image("it/polimi/ingsw/images/cards/playable/start/back/" + immStartCard.getId() + ".png");
        }
        startStackPane = stackPaneBuilder(image);
        //Placing in the middle of the grid pane
        Node startNodeToRemove = emptyPanesMap.get(new Coordinate(-minX + 1, maxY + 1));
        gridPane.getChildren().remove(startNodeToRemove);
        gridPane.add(startStackPane, -minX + 1, maxY + 1);

        //Adds the played cards in the grid
        for(Coordinate coordinate : controller.getModel().getSelfPlayer().getPlayArea().getPlayedCards().keySet()) {
            int k = coordinate.getX() - minX + 1;
            int j = maxY - coordinate.getY() + 1;

            ImmPlayableCard immPlayableCard = controller.getModel().getSelfPlayer().getPlayArea().getPlayedCards().get(coordinate);
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
     * This method is used to display the opponent's grid on the GUI.
     * It first sets the visibility and management of the gridPane and opponentGridPane.
     * Then it calculates the maximum and minimum X and Y coordinates of the played cards.
     * It adds empty slots to the opponentGridPane based on the calculated grid dimensions.
     * It then adds the start card and the played cards to the opponentGridPane.
     * The method uses the opponent's username to get the opponent's play area from the viewModel.
     *
     * @param opponent The username of the opponent whose grid is to be displayed.
     */
    private void showOpponentGridPane(String opponent){
        gridPane.setManaged(false);
        gridPane.setVisible(false);
        opponentGridPane.setVisible(true);
        opponentGridPane.setManaged(true);

        opponentGridPane.getChildren().clear();
        gridPane.getChildren().clear();
        emptyPanesMap.clear();

        int gridLength = calculateGridLength(controller.getModel().getOpponent(opponent).getPlayArea().getPlayedCards());
        int gridHeight = calculateGridHeight(controller.getModel().getOpponent(opponent).getPlayArea().getPlayedCards());
        int minX = calculateMinX(controller.getModel().getOpponent(opponent).getPlayArea().getPlayedCards());
        int maxY = calculateMaxY(controller.getModel().getOpponent(opponent).getPlayArea().getPlayedCards());

        //Add empty slots
        for(int j = 0; j <= gridHeight; j++) {
            for (int k = 0; k <= gridLength; k++) {
                Image image = new Image("it/polimi/ingsw/images/cards/playable/Empty2.png");
                StackPane emptyStackPane = stackPaneBuilder(image);
                opponentGridPane.add(emptyStackPane, k, j);
                emptyPanesMap.put(new Coordinate(k, j), emptyStackPane);
            }
        }

        //Add start card
        ImmStartCard immStartCard = controller.getModel().getOpponent(opponent).getPlayArea().getStartCard();
        StackPane startStackPane;
        Image image;
        if (!immStartCard.isFlipped()) {
            image = new Image("it/polimi/ingsw/images/cards/playable/start/front/" + immStartCard.getId() + ".png");
        } else {
            image = new Image("it/polimi/ingsw/images/cards/playable/start/back/" + immStartCard.getId() + ".png");
        }
        startStackPane = stackPaneBuilder(image);
        Node startNodeToRemove = emptyPanesMap.get(new Coordinate(-minX + 1, maxY + 1));
        opponentGridPane.getChildren().remove(startNodeToRemove);
        opponentGridPane.add(startStackPane, -minX + 1, maxY + 1);

        //Adds the opponent played cards in the grid
        for(Coordinate coordinate : controller.getModel().getOpponent(opponent).getPlayArea().getPlayedCards().keySet()) {
            int k = coordinate.getX() - minX + 1;
            int j = maxY - coordinate.getY() + 1;

            ImmPlayableCard immPlayableCard = controller.getModel().getOpponent(opponent).getPlayArea().getPlayedCards().get(coordinate);
            StackPane playedStackPane;
            if (!immPlayableCard.isFlipped()) {
                image = getFrontCardImage(immPlayableCard.getId(), immPlayableCard.getType());
            } else {
                image = getBackCardImage(immPlayableCard.getPermanentResource(), immPlayableCard.getType());
            }
            playedStackPane = stackPaneBuilder(image);
            Node nodeToRemove = emptyPanesMap.get(new Coordinate(k, j));
            opponentGridPane.getChildren().remove(nodeToRemove);
            opponentGridPane.add(playedStackPane, k, j);
        }
    }

    /**
     * This method updates the text of the {@code aboveLabel} based on the current game state.
     * If it's the player's turn, and they don't have to draw a card, it sets the text to "It's your turn, play a card".
     * If it's the player's turn, and they have to draw a card, it sets the text to "Now draw a card to your hand".
     * If it's not the player's turn, it sets the text to "Waiting for your turn".
     */
    private void updateAboveLabel(){
        if(controller.getModel().isDetectedLC()){
            gameUpdatesArea.appendText("The last circle will start with " + controller.getBlackTokenPlayer() + "'s turn\n");
        }

        if(controller.getGameStatus() == GameStatus.WAITING){
            aboveLabel.setText("Waiting for a player to join back or for the timer to run out");
            gameScrollPane.setManaged(false);
            gameScrollPane.setVisible(false);
            waitingPane.setManaged(true);
            waitingPane.setVisible(true);
        }
        else if(controller.getGameStatus() == GameStatus.RUNNING) {
            if (controller.hasTurn() && !hasToDraw) {
                aboveLabel.setText("It's your turn, play a card");
            } else if (controller.hasTurn() && hasToDraw) {
                aboveLabel.setText("Now draw a card to your hand");
            } else {
                aboveLabel.setText("Waiting for your turn");
            }
        }
        if(controller.isLastCircle()){
            if(controller.hasTurn()){
                aboveLabel.setText("This is the last circle of the game, you can only play a card");
            }
            else{
                aboveLabel.setText("This is the last circle of the game, you'll be only able to place a card");
            }
        }
    }

    /**
     * This method sets the game name label with the lobby ID from the controller.
     */
    private void setGameName() {
        this.gameName.setText("GAME:" + controller.getLobbyId());
    }

    /**
     * This method updates the chat options by adding the general chat and the private chats with the other players.
     */
    private void updateChatOptions(){
        chatSelection.getItems().clear();

        chatSelection.getItems().add("General");
        chatSelection.getSelectionModel().select("General");

        for(String user : controller.getPlayersStatus().keySet()){
            if(!controller.getUsername().equals(user)){
                chatSelection.getItems().add("User: " + user);
            }
        }
    }

    /**
     * This method updates the visible play areas by adding the current player's board and the opponents' boards.
     */
    private void updateVisiblePlayAreasOptions(){
        playAreaSelection.getItems().clear();

        playAreaSelection.getItems().add("Your board");
        playAreaSelection.getSelectionModel().select("Your board");

        for(String user : controller.getPlayersStatus().keySet()){
            if(!controller.getUsername().equals(user)){
                playAreaSelection.getItems().add("Opponent: " + user);
            }
        }
    }

    /**
     * This method updates the points and item occurrences for each player in the game.
     * It first hides all the uncovered items, then for each player in the game, it makes the player's uncovered items visible.
     * If the player is online, it sets the player's username, otherwise it adds an offline tag to the username.
     * It then gets the points of the current player from the scoreboard and sets the player's points.
     * It checks whether the current user is this user and then gets the right map of uncovered items accordingly.
     * It also gets the current token of the player and sets the image of the token.
     * Using the lists of occurrences, it updates the current player's item occurrences.
     * It does this for each item in the game: fungi, animal, plant, insect, inkwell, quill, and manuscript.
     */
    private void updatePoints(){
        int i = 0;
        int currPoints;

        Map<Item, Integer> currUncoveredItems;
        Map<String, Integer> scoreboard = controller.getModel().getScoreboard();
        //Hides everything so it can show the new scoreboard
        for(HBox uncovered : uncoveredList){
            uncovered.setVisible(false);
        }
        //Cycles through the users in game, even the ones who got disconnected
        for(String user : controller.getInMatchPlayerUsernames()){
            uncoveredList.get(i).setVisible(true);
            //Disconnected users have an offline tag
            if(controller.isOnline(user)) {
                uncoveredUsernames.get(i).setText(user);
            }
            else{
                uncoveredUsernames.get(i).setText(user + " (offline)");
            }

            //Takes the points of the current user
            currPoints = scoreboard.get(user);
            playerPoints.get(i).setText(String.valueOf(currPoints));

            //Checks whether the current user is this user and then currUncoveredItems gets the right map accordingly
            if(user.equals(controller.getUsername())){
                currUncoveredItems = controller.getModel().getSelfPlayer().getPlayArea().getUncoveredItems();
                Token currToken = controller.getModel().getSelfPlayer().getToken();
                Image imageToken = getTokenImage(currToken);
                tokens.get(i).setImage(imageToken);
            }
            else {
                currUncoveredItems = controller.getModel().getOpponent(user).getPlayArea().getUncoveredItems();
                Token currToken = controller.getModel().getOpponent(user).getToken();
                Image imageToken = getTokenImage(currToken);
                tokens.get(i).setImage(imageToken);
            }

            //Using the lists of occurrences, updates current player items' number
            for(Item item : currUncoveredItems.keySet()){
                switch(item){
                    case FUNGI:
                        fungiOccurrences.get(i).setText(String.valueOf(currUncoveredItems.get(item)));
                        break;
                    case ANIMAL:
                        animalOccurrences.get(i).setText(String.valueOf(currUncoveredItems.get(item)));
                        break;
                    case PLANT:
                        plantOccurrences.get(i).setText(String.valueOf(currUncoveredItems.get(item)));
                        break;
                    case INSECT:
                        insectOccurrences.get(i).setText(String.valueOf(currUncoveredItems.get(item)));
                        break;
                    case INKWELL:
                        inkwellOccurrences.get(i).setText(String.valueOf(currUncoveredItems.get(item)));
                        break;
                    case QUILL:
                        quillOccurrences.get(i).setText(String.valueOf(currUncoveredItems.get(item)));
                        break;
                    case MANUSCRIPT:
                        manuscriptOccurrences.get(i).setText(String.valueOf(currUncoveredItems.get(item)));
                        break;
                }
            }
            i++;
        }
    }

    /**
     * This method is used to display the common objectives.
     * It gets the common objectives from the viewModel and sets the images of the commonObjectiveCard0 and commonObjectiveCard1.
     */
    private void showCommonObjectives(){
        ImmObjectiveCard[] objectiveCards = controller.getModel().getCommonObjectives();
        ImmObjectiveCard objectiveCard0 = objectiveCards[0];
        ImmObjectiveCard objectiveCard1 = objectiveCards[1];
        commonObjectiveCard0.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveCard0.getId() + ".png"));
        commonObjectiveCard1.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveCard1.getId() + ".png"));
    }

    /**
     * This method is used to display the secret objective of the current player.
     * It gets the secret objective from the viewModel and sets the image of the secretObjectiveCard.
     */
    private void showSecretObjective(){
        ImmObjectiveCard secretObjCard = controller.getModel().getSelfPlayer().getSecretObjective();
        secretObjectiveCard.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + secretObjCard.getId() + ".png"));
    }

    /**
     * This method is used to submit a chat message.
     * It sends a {@link ChatGMEvent} or {@link ChatPMEvent} to the server based on the selected radio button.
     */
    @FXML
    public void submitMessage(){
        String message = chatInput.getText();
        String chatChoice = chatSelection.getValue();

        if(message.isEmpty()){
            return;
        }

        if(chatChoice.equals("General")) {
            sendPublicMessage(message);
        } else {
            //Substring of 6 because of the annotation "User: "
            String username = chatChoice.substring(6);
            sendPrivateMessage(username, message);
        }
        chatInput.clear();
    }

    /**
     * This method is used to submit a private message if the selected radio button for chat is a specific user.
     */
    private void sendPrivateMessage(String username, String message){
        if(controller.getPlayersStatus().containsKey(username)) {
            controller.newViewEvent(new ChatPMEvent(new PrivateChatMessage(username, message)));
        }
        else{
            chatArea.appendText("Error: player not in match");
        }
    }

    /**
     * This method is used to submit a private message if the selected radio button for chat is general.
     */
    private void sendPublicMessage(String message){
        controller.newViewEvent(new ChatGMEvent(new ChatMessage(message)));
    }

    @FXML
    public void hideWaitingPane(){
        this.gameScrollPane.setVisible(true);
        this.gameScrollPane.setManaged(true);
        this.waitingPane.setVisible(false);
        this.waitingPane.setManaged(false);
    }
    /**
     * This method is used to check if the user is in a game.
     * It always returns true, indicating that the user is always in a game.
     *
     * @return True, indicating that the user is in a game.
     */
    @Override
    public boolean inGame(){
        return true;
    }

    /**
     * This method is used to check if the user is in a chat.
     * It always returns true, indicating that the user is always in a chat.
     *
     * @return True, indicating that the user is in a chat.
     */
    @Override
    public boolean inChat(){ return true;}

}
