package it.polimi.ingsw.view.gui.controller;


import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromModel.OtherDrawCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.DrawCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.PlaceCardEvent;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.ViewModel;
import it.polimi.ingsw.viewModel.immutableCard.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InGameController extends FXMLController {

    @FXML
    private AnchorPane scoreBoard;


    @FXML
    private  BorderPane borderPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private GridPane opponentGridPane;

    @FXML
    private AnchorPane uncovered1;

    @FXML
    private AnchorPane uncovered2;

    @FXML
    private AnchorPane uncovered3;

    @FXML
    private AnchorPane uncovered4;

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
    private AnchorPane secretObjectiveAnchor;


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

    private List<ImageView> visibleResourceCards;

    private List<Label> playerPoints;

    private List<Label> animalOccurrences;

    private List<Label> fungiOccurrences;

    private List<Label> plantOccurrences;

    private List<Label> insectOccurrences;

    private List<Label> manuscriptOccurrences;

    private List<Label> inkwellOccurrences;

    private List<Label> quillOccurrences;

    private List<AnchorPane> uncoveredList;

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
                    //Update decks needed for adding listeners for choosing the card to draw
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
                    //showPlayArea();
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
                    //showPlayArea();
                    updateDecks();
                    gameUpdatesArea.appendText(message + "\n");
                });
                break;
            case NEW_TURN:
                Platform.runLater(() -> {
                    hasToDraw = false;
                    updateAboveLabel();
                    showPlayArea();
                    //updateDecks();
                    //updatePoints();
                    gameUpdatesArea.appendText(message + "\n");
                });
                break;
            case ENDED_GAME:
                break;
            case CHAT_GM, CHAT_PM:
                if (feedback.equals(Feedback.SUCCESS)) {
                    String formattedMessage = message.replace("[1m", " ").replace("[0m","");
                    chatArea.appendText(formattedMessage + "\n");
                } else {
                    System.out.println("Message unable to send!");
                }
                break;
        }
        notifyResponse();
    }

    /**
     * This method flips the playFlipped variable and updates the hand.
     */
    @FXML
    public void setPlayFlipped(){
        playFlipped = !playFlipped;
        updateHand();
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
        for(ImageView cardImageView : handCardImages) {
            if(i < controller.getModel().getSelfPlayer().getPlayArea().getHand().size()) {
                ImmPlayableCard currCard = controller.getModel().getSelfPlayer().getPlayArea().getHand().get(i);
                //Drag and drop (start drag)
                cardImageView.setOnDragDetected(event -> {
                    selectedPlayableCard = currCard;
                    Dragboard db = cardImageView.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(cardImageView.getImage());
                    content.putString(currCard.getId());
                    db.setContent(content);
                    event.consume();
                });
            }
            else{
                cardImageView.setOnDragDetected(event -> {});
            }
            i++;
        }
    }

    private void addDeckChoiceSelection(){
        if(controller.hasTurn() && hasToDraw && controller.getGameStatus().equals(GameStatus.RUNNING)) {
            if (controller.getModel().getTopResourceDeck() != null) {
                resourceDeck.setOnMouseClicked(event -> {
                    controller.newViewEvent(new DrawCardEvent(CardType.RESOURCE, 2));
                    waitForResponse();
                });
            }
            if (controller.getModel().getTopGoldDeck() != null) {
                goldDeck.setOnMouseClicked(event -> {
                    controller.newViewEvent(new DrawCardEvent(CardType.GOLD, 2));
                    waitForResponse();
                });
            }

            int i = 0;
            for (ImageView cardImageView : visibleGoldCards) {
                if (i < controller.getModel().getVisibleGoldCards().length) {
                    ImmPlayableCard currCard = controller.getModel().getVisibleGoldCards()[i];
                    int selectI = i;
                    cardImageView.setOnMouseClicked(event ->{
                        controller.newViewEvent(new DrawCardEvent(currCard.getType(), selectI));
                        waitForResponse();
                    });
                }
                i++;
            }

            i = 0;
            for (ImageView cardImageView : visibleResourceCards) {
                if (i < controller.getModel().getVisibleResourceCards().length) {
                    ImmPlayableCard currCard = controller.getModel().getVisibleResourceCards()[i];
                    int selectI = i;
                    cardImageView.setOnMouseClicked(event ->{
                        controller.newViewEvent(new DrawCardEvent(currCard.getType(), selectI));
                        waitForResponse();
                    });
                }
                i++;
            }
        }
    }

    /**
     * This method updates the decks by getting the visible gold and resource cards from the viewModel.
     * It sets the images of the visible cards and the top cards of the decks.
     */
    private void updateDecks(){
        ImmPlayableCard[] visibleGoldCards = controller.getModel().getVisibleGoldCards();
        ImmPlayableCard[] visibleResourceCards = controller.getModel().getVisibleResourceCards();
        int i;
        for(i = 0; i < visibleGoldCards.length; i++){
            //Updating the ImageViews of the gold deck
            this.visibleGoldCards.get(i).setImage(getFrontCardImage(visibleGoldCards[i].getId(), visibleGoldCards[i].getType()));
        }
        while(i < 2){
            this.visibleGoldCards.get(i).setImage(null);
            this.visibleGoldCards.get(i).setVisible(false);
        }

        for(i = 0; i < visibleResourceCards.length; i++){
            //Updating the ImageViews of the resource deck
            this.visibleResourceCards.get(i).setImage(getFrontCardImage(visibleResourceCards[i].getId(), visibleResourceCards[i].getType()));
        }
        while(i < 2){
            this.visibleResourceCards.get(i).setImage(null);
            this.visibleResourceCards.get(i).setVisible(false);
        }

        Item itemGoldDeck = controller.getModel().getTopGoldDeck();
        if(itemGoldDeck != null) {
            Image backGoldCardImage = getBackCardImage(itemGoldDeck, CardType.GOLD);
            goldDeck.setImage(backGoldCardImage);
        }
        else{
            goldDeck.setImage(null);
            goldDeck.setVisible(false);
        }

        Item itemResourceDeck = controller.getModel().getTopResourceDeck();
        if(itemResourceDeck != null) {
            Image backResourceCardImage = getBackCardImage(itemResourceDeck, CardType.RESOURCE);
            resourceDeck.setImage(backResourceCardImage);
        }
        else{
            resourceDeck.setImage(null);
            goldDeck.setVisible(false);
        }
        addDeckChoiceSelection();
    }

    /**
     * This method updates the hand by getting the hand from the viewModel.
     * It sets the images of the hand cards based on the playFlipped variable.
     */
    private void updateHand(){
        //The fxml has 6 imageView, 3 for the current player's hand and 3 for the opponents
        for(ImageView handCard : handCardImages){
            handCard.setVisible(true);
            handCard.setManaged(true);
        }
        for(ImageView handCard : opponentCardImages){
            handCard.setVisible(false);
            handCard.setManaged(false);
        }

        int i = 0;
        for(ImmPlayableCard card : controller.getModel().getSelfPlayer().getPlayArea().getHand()){
            ImageView currImageView = handCardImages.get(i);
            if (!playFlipped) {
                currImageView.setImage(getFrontCardImage(card.getId(), card.getType()));
            }
            else {
                currImageView.setImage(getBackCardImage(card.getPermanentResource(), card.getType()));
            }
            i++;
        }
        while(i < handCardImages.size()){
            handCardImages.get(i).setImage(null);
            i++;
        }
        addHandChoiceSelection();
    }

    private void updateOpponentHand(String opponent){
        for(ImageView handCard : handCardImages){
            handCard.setVisible(false);
            handCard.setManaged(false);
        }
        for(ImageView handCard : opponentCardImages) {
            handCard.setVisible(true);
            handCard.setManaged(true);
        }
        List<BackPlayableCard> backHand = controller.getModel().getOpponent(opponent).getPlayArea().getHand();
        int i = 0;
        for(BackPlayableCard card : backHand){
            ImageView currImageView = opponentCardImages.get(i);
            currImageView.setImage(getBackCardImage(card.getItem(), card.getCardType()));
            i++;
        }
        while(i < opponentCardImages.size()){
            opponentCardImages.get(i).setImage(null);
            i++;
        }
    }

    /**
     * This method is used to display the play area of the current player or the selected opponent.
     * If the selected play area is "Your board", it sets the {@code gridPane} to visible and manages it, and sets the {@code opponentGridPane} to invisible and unmanaged.
     * It also sets the {@code secretObjectiveAnchor} to visible, sets the {@code handLabel} to "MY HAND", and makes the hand cards of the current player visible and managed.
     * If the selected play area is an opponent's board, it sets the {@code gridPane} to invisible and unmanaged, and sets the {@code opponentGridPane} to visible and managed.
     * It also sets the {@code secretObjectiveAnchor} to invisible, sets the {@code handLabel} to the opponent's username + " hand", and makes the hand cards of the opponent visible and managed.
     */
    @FXML
    public void showPlayArea() {
        String playAreaChoice = playAreaSelection.getValue();

        if(playAreaChoice.equals("Your board")){
            secretObjectiveAnchor.setVisible(true);
            handLabel.setText("MY HAND");
            flipButton.setVisible(true);
            updateHand();
            showYourGridPane();
        }
        else{
            secretObjectiveAnchor.setVisible(false);
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
        int maxX, minX, maxY, minY;
        maxX = 0;
        minX = 0;
        maxY = 0;
        minY = 0;
        gridPane.getChildren().clear();
        emptyPanesMap.clear();

        for(Coordinate coordinate : controller.getModel().getSelfPlayer().getPlayArea().getPlayedCards().keySet()){
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


        //Add available slots
        for(Coordinate coordinate : controller.getModel().getSelfPlayer().getPlayArea().getAvailablePos()){
            int k = coordinate.getX() - minX + 1;
            int j = maxY - coordinate.getY() + 1;

            Image image = new Image("it/polimi/ingsw/images/cards/playable/Available.png");
            StackPane availableStackPane = stackPaneBuilder(image);
            /*
            Node nodeToRemove = null;
            for (Node node : gridPane.getChildren()) {
                if (GridPane.getColumnIndex(node) == k && GridPane.getRowIndex(node) == j) {
                    nodeToRemove = node;
                    break;
                }
            }
            gridPane.getChildren().remove(nodeToRemove);
            gridPane.add(availableStackPane, k, j);
             */
            Node nodeToRemove = emptyPanesMap.get(new Coordinate(k, j));
            gridPane.getChildren().remove(nodeToRemove);
            gridPane.add(availableStackPane, k, j);

            //For drag and drop of the card
            if(controller.hasTurn() && !hasToDraw) {
                availableStackPane.setOnDragOver(event -> {
                    if (event.getGestureSource() != availableStackPane && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                });
                availableStackPane.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasImage() && db.hasString()){
                        success = true;
                        controller.newViewEvent(new PlaceCardEvent(selectedPlayableCard.getId(), coordinate, playFlipped));
                        System.out.println("Waiting for server response on card " + selectedPlayableCard.getId() + " " + coordinate.getX() + " " + coordinate.getY() );
                        waitForResponse();
                        //return;
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
        /*
        Node startNodeToRemove = null;
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == -minX + 1 && GridPane.getRowIndex(node) == maxY + 1) {
                startNodeToRemove = node;
                break;
            }
        }
        gridPane.getChildren().remove(startNodeToRemove);
        gridPane.add(startStackPane, -minX + 1, maxY + 1);
         */
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
            /*
            Node nodeToRemove = null;
            for (Node node : gridPane.getChildren()) {
                if (GridPane.getColumnIndex(node) == k && GridPane.getRowIndex(node) == j) {
                    nodeToRemove = node;
                    break;
                }
            }
            gridPane.getChildren().remove(nodeToRemove);
            gridPane.add(playedStackPane, k, j);
             */
            Node nodeToRemove = emptyPanesMap.get(new Coordinate(k, j));
            gridPane.getChildren().remove(nodeToRemove);
            gridPane.add(playedStackPane, k, j);
        }
    }

    private void showOpponentGridPane(String opponent){
        gridPane.setManaged(false);
        gridPane.setVisible(false);
        opponentGridPane.setVisible(true);
        opponentGridPane.setManaged(true);

        int maxX, minX, maxY, minY;
        maxX = 0;
        minX = 0;
        maxY = 0;
        minY = 0;
        opponentGridPane.getChildren().clear();
        emptyPanesMap.clear();

        for(Coordinate coordinate : controller.getModel().getOpponent(opponent).getPlayArea().getPlayedCards().keySet()){
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
        /*
        Node startNodeToRemove = null;
        for (Node node : opponentGridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == -minX + 1 && GridPane.getRowIndex(node) == maxY + 1) {
                startNodeToRemove = node;
            }
        }
        opponentGridPane.getChildren().remove(startNodeToRemove);
        opponentGridPane.add(startStackPane, -minX + 1, maxY + 1);
         */
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
            /*
            Node nodeToRemove = null;
            for (Node node : opponentGridPane.getChildren()) {
                if (GridPane.getColumnIndex(node) == k && GridPane.getRowIndex(node) == j) {
                    nodeToRemove = node;
                    break;
                }
            }
            opponentGridPane.getChildren().remove(nodeToRemove);
            opponentGridPane.add(playedStackPane, k, j);
             */
            Node nodeToRemove = emptyPanesMap.get(new Coordinate(k, j));
            opponentGridPane.getChildren().remove(nodeToRemove);
            opponentGridPane.add(playedStackPane, k, j);
        }
    }


    private void updateAboveLabel(){
        if(controller.hasTurn() && !hasToDraw){
            aboveLabel.setText("It's your turn, play a card");
        }
        else if(controller.hasTurn() && hasToDraw){
            aboveLabel.setText("Now draw a card to your hand");
        }
        else{
            aboveLabel.setText("Waiting for your turn");
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
     * This method updates the points by getting the scoreboard from the viewModel.
     * It sets the points and the occurrences of the items for each player.
     */
    private void updatePoints(){
        int i = 0;
        int currPoints;

        Map<Item, Integer> currUncoveredItems;
        Map<String, Integer> scoreboard = controller.getModel().getScoreboard();
        //Hides everything so it can show the new scoreboard
        for(AnchorPane pane : uncoveredList){
            pane.setVisible(false);
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
            }
            else {
                currUncoveredItems = controller.getModel().getOpponent(user).getPlayArea().getUncoveredItems();
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
            System.out.println("Error: player not in match");
        }
    }

    /**
     * This method is used to submit a private message if the selected radio button for chat is general.
     */
    private void sendPublicMessage(String message){
        controller.newViewEvent(new ChatGMEvent(new ChatMessage(message)));
    }

    private StackPane stackPaneBuilder(Image image){
        //Border border = new Border(new BorderStroke(javafx.scene.paint.Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10)));

        StackPane stackPane = new StackPane();
        stackPane.setMaxWidth(113);
        stackPane.setMinWidth(113);
        stackPane.setMaxHeight(60);
        stackPane.setMinHeight(60);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);
        imageView.setImage(image);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        stackPane.getChildren().add(imageView);

        return stackPane;
    }


    @Override
    public boolean inGame(){
        return true;
    }

    @Override
    public boolean inChat(){ return true;}

}
