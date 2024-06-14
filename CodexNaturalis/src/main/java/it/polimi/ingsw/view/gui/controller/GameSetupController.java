package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetChatMessagesEvent;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.viewModel.immutableCard.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for setting up the game. It handles the user interactions during the game setup phase.
 * It extends the FXMLController class and overrides its methods to provide the specific functionality needed for the game setup.
 */
public class GameSetupController extends FXMLController {

    @FXML
    public BorderPane mainAnchor;

    @FXML
    public Text text0;

    @FXML
    public Text text1;

    @FXML
    public Text text2;

    @FXML
    public Text text3;

    @FXML
    public Text text4;

    @FXML
    public VBox vboxChat;

    @FXML
    public VBox deckBox;

    @FXML
    private Label lobbyName;

    @FXML
    private Button readyButton;


    @FXML
    private ImageView resourceDeck;

    @FXML
    private ImageView visibleResourceCard0;

    @FXML
    private ImageView visibleResourceCard1;


    @FXML
    private ImageView goldDeck;

    @FXML
    private ImageView visibleGoldCard0;

    @FXML
    private ImageView visibleGoldCard1;


    //Hand cards.
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
    private ImageView commonObjectiveCard0;

    @FXML
    private ImageView commonObjectiveCard1;

    @FXML
    private HBox secretObjectiveHBox;

    @FXML
    private ImageView secretObjectiveCard0;

    @FXML
    private ImageView secretObjectiveCard1;

    @FXML
    private HBox startCardHBox;

    @FXML
    private ImageView startCardFront;

    @FXML
    private ImageView startCardBack;

    //Chat option buttons.
    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    @FXML
    private RadioButton generalRadioButton;

    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;



    @FXML
    private Button yourSetupButton;

    @FXML
    private Button setupButton1;

    @FXML
    private Button setupButton2;

    @FXML
    private Button setupButton3;


    private ViewStartSetup setup;

    private ImmObjectiveCard objectiveChoice0;

    private ImmObjectiveCard objectiveChoice1;

    private ImmObjectiveCard objectiveChosen;

    private Boolean flippedChoice;

    private List<Button> setupButtons;

    private List<RadioButton> radioButtons;

    private List<ImageView> handCardImages;

    private List<ImageView> opponentsHandCardImages;

    private List<ImmPlayableCard> myHand;


    /**
     * Default constructor for the GameSetupController class.
     */
    public GameSetupController(){
        super();
    }

    public void initialize(){
        double relativePercentage = 0.12;
        double relativePercentage1 = 0.10;
        double relativePercentageChoose = 0.50;
        double relativePercentageText = 0.20;

        List<ImageView> imageViews = Arrays.asList(handCard0, handCard1, handCard2,
                                                   opponentHandCard0, opponentHandCard1, opponentHandCard2,
                                                   commonObjectiveCard0, commonObjectiveCard1,
                                                   secretObjectiveCard0, secretObjectiveCard1,
                                                   startCardFront, startCardBack);

        for (ImageView imageView : imageViews) {
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() ->
                    mainAnchor.getWidth() * relativePercentage, mainAnchor.widthProperty()
            ));

            imageView.fitHeightProperty().bind(Bindings.createDoubleBinding(() ->
                    mainAnchor.getHeight() * relativePercentage, mainAnchor.heightProperty()
            ));
        }

        List<ImageView> imageViews1 = Arrays.asList(resourceDeck, visibleResourceCard0, visibleResourceCard1,
                goldDeck, visibleGoldCard0, visibleGoldCard1);

        for (ImageView imageView : imageViews1) {
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() ->
                    mainAnchor.getWidth() * relativePercentage1, mainAnchor.widthProperty()
            ));

            imageView.fitHeightProperty().bind(Bindings.createDoubleBinding(() ->
                    mainAnchor.getHeight() * relativePercentage1, mainAnchor.heightProperty()
            ));
        }

        text0.wrappingWidthProperty().bind(Bindings.createDoubleBinding(() ->
                mainAnchor.getWidth() * relativePercentageChoose, mainAnchor.widthProperty()
        ));
        List<Text> textArray = Arrays.asList(text1, text2, text3, text4);
        for (Text t : textArray) {
            t.wrappingWidthProperty().bind(Bindings.createDoubleBinding(() ->
                    mainAnchor.getWidth() * relativePercentageText, mainAnchor.widthProperty()
            ));
        }

        double vboxChatPercentage = 0.25;
        vboxChat.minHeightProperty().bind(Bindings.createDoubleBinding(() ->
                mainAnchor.getHeight() * vboxChatPercentage, mainAnchor.heightProperty()
        ));

    }

    /**
     * This method is called when the controller is initialized. It sets up the view, stage and ViewController for the class.
     * The initialization includes setting up the chat, the buttons for viewing other players' setup, the radio buttons in chat, one for each
     * specific player in the game, the images for showing other player's back cards in the hand, the images for showing this player's hand,
     * the cards in the resource deck and in the gold deck, the common objectives and the secret objectives.
     * @param view The view associated with this controller.
     * @param stage The stage that this controller controls.
     */
    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        this.setup = controller.getSetup();
        controller.setInSetup(new Pair<>(true, false));

        setLobbyName(controller.getLobbyId());
        setupButtons = Arrays.asList(setupButton1, setupButton2, setupButton3);
        radioButtons = Arrays.asList(radioButton1, radioButton2, radioButton3);
        opponentsHandCardImages = Arrays.asList(opponentHandCard0, opponentHandCard1, opponentHandCard2);
        handCardImages = Arrays.asList(handCard0, handCard1, handCard2);

        myHand = setup.getHand();

        showResourceDeck();
        showGoldDeck();
        showMyHand();
        showCommonObjectives();
        showSecretObjectives();
        showStartCard();
        update();
        addHoverEffect();
        addChoiceSelection();

        //To load the chat from the lobby
        controller.newViewEvent(new GetChatMessagesEvent());
        waitForResponse();
        chatArea.appendText("You're playing in the lobby: " + controller.getLobbyId() + "\n");
        chatInput.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                submitMessage();
            }
        });
    }

    /**
     * This method processes server responses based on the event ID. The events encapsulate various stages of the game setup and progression.
     * These stages include the following scenarios:
     * - Player finalizing their game setup choices.
     * - Receipt of a new chat message.
     * - A player exiting the game.
     * - Transition to the subsequent game phase, specifically the selection of colored tokens.
     * @param feedback The feedback from the server.
     * @param message The message from the server.
     * @param eventID The event ID of the response.
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch(EventID.getByID(eventID)) {
            case GET_CHAT_MESSAGES:
                String getChatFormattedMessages = message.replace("[1m", " ").replace("[0m", "");
                chatArea.appendText(getChatFormattedMessages + "\n");
                break;
            case CHOSEN_CARDS_SETUP:
                chatArea.appendText("\n" + message + "\n\n");
                readyButton.setVisible(false);
                break;
            case UPDATE_GAME_PLAYERS:
                Platform.runLater(() -> {
                    chatArea.appendText("\n" + message + "\n\n");
                    update();
                });
                break;
            case CHOOSE_TOKEN_SETUP:
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/TokenSetup.fxml"));
                        Parent root = loader.load();
                        TokenSetupController nextController = loader.getController();

                        Scene scene = stage.getScene();
                        scene.setRoot(root);
                        transition(nextController);
                    }
                    catch (IOException exception){
                        exception.printStackTrace();
                    }
                });
                break;
            case QUIT_GAME:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/EnterLobbiesMenu.fxml"));
                            Parent root = loader.load();
                            EnterLobbiesController nextController = loader.getController();

                            Scene scene = stage.getScene();
                            scene.setRoot(root);
                            transition(nextController);
                        }
                        catch (IOException exception){
                            exception.printStackTrace();
                        }
                    });
                }
                else{
                    System.out.println("Something bad happened, you can't quit!");
                }
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
     * This method is responsible for handling the selection of cards during the game setup phase.
     * It adds a mouse click event to each card. When a card is clicked, it is highlighted and set as the chosen card.
     * It also handles when the player wants to play the start card flipped or not flipped by adding the same effect.
     * The method uses a DropShadow effect to highlight the chosen card.
     */
    private void addChoiceSelection(){
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.WHITE);
        highlight.setRadius(10.0);
        highlight.setOffsetX(0);
        highlight.setOffsetY(0);

        secretObjectiveCard0.setOnMouseClicked(event -> {
            secretObjectiveCard0.setEffect(highlight);
            secretObjectiveCard1.setEffect(null);
            objectiveChosen = objectiveChoice0;
            //System.out.println("Chosen objective card " + objectiveChosen.getId());
        });

        secretObjectiveCard1.setOnMouseClicked(event -> {
            secretObjectiveCard1.setEffect(highlight);
            secretObjectiveCard0.setEffect(null);
            objectiveChosen = objectiveChoice1;
            //System.out.println("Chosen objective card " + objectiveChosen.getId());
        });

        startCardFront.setOnMouseClicked(event -> {
            startCardFront.setEffect(highlight);
            startCardBack.setEffect(null);
            flippedChoice = false;
            //System.out.println("Chosen start card on front");
        });

        startCardBack.setOnMouseClicked(event -> {
            startCardBack.setEffect(highlight);
            startCardFront.setEffect(null);
            flippedChoice = true;
            //System.out.println("Chosen start card on back");
        });
    }

    /**
     * This method adds a hover effect to the cards in the game setup phase.
     * It creates a DropShadow effect and applies it to the cards when the mouse enters their area.
     * When the mouse exits the area of a card, the effect is removed unless the cards are the chosen ones.
     * For the cards in the player's hand, the method changes the image of the card to its back side when the mouse enters its area,
     * and changes it back to the front side when the mouse exits its area.
     */
    private void addHoverEffect(){
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.WHITE);
        highlight.setRadius(10.0);
        highlight.setOffsetX(0);
        highlight.setOffsetY(0);

        secretObjectiveCard0.setOnMouseEntered(event ->
            secretObjectiveCard0.setEffect(highlight));
        secretObjectiveCard0.setOnMouseExited(event -> {
            if(objectiveChosen == null || (!objectiveChosen.equals(objectiveChoice0))) {
                secretObjectiveCard0.setEffect(null);
            }
        });

        secretObjectiveCard1.setOnMouseEntered(event ->
            secretObjectiveCard1.setEffect(highlight));
        secretObjectiveCard1.setOnMouseExited(event -> {
            if(objectiveChosen == null || (!objectiveChosen.equals(objectiveChoice1))) {
                secretObjectiveCard1.setEffect(null);
            }
        });

        startCardFront.setOnMouseEntered(event ->
            startCardFront.setEffect(highlight));
        startCardFront.setOnMouseExited(event -> {
            if(flippedChoice == null || flippedChoice) {
                startCardFront.setEffect(null);
            }
        });

        startCardBack.setOnMouseEntered(event ->
            startCardBack.setEffect(highlight));
        startCardBack.setOnMouseExited(event -> {
            if(flippedChoice == null || !flippedChoice) {
                startCardBack.setEffect(null);
            }
        });

        int i = 0;
        for(ImmPlayableCard card : myHand){
            Image backCardImage = getBackCardImage(card.getPermanentResource(), card.getType());
            Image frontCardImage = handCardImages.get(i).getImage();
            ImageView currHandImage = handCardImages.get(i);
            currHandImage.setOnMouseEntered(event ->
                currHandImage.setImage(backCardImage));
            currHandImage.setOnMouseExited(event ->
                currHandImage.setImage(frontCardImage));
            i++;
        }
    }

    /**
     * This method is responsible for displaying the player's hand in the GUI.
     * It iterates over the cards in the player's hand and creates an Image object for each card.
     * The Image object is created based on the card's ID and type (gold or resource).
     * The image is then set to the corresponding ImageView in the handCardImages list.
     */
    private void showMyHand(){
        int i = 0;
        for(ImmPlayableCard card : myHand){
            handCardImages.get(i).setImage(getFrontCardImage(card.getId(), card.getType()));
            i++;
        }
    }

    /**
     * This method is responsible for displaying the current player's setup in the GUI.
     * It makes the secret objectives and start card visible and manages their visibility.
     * It also iterates over the ImageView objects in the handCardImages list, making them visible and managed.
     * Conversely, it makes the ImageView objects in the {@code opponentsHandCardImages} list invisible and unmanaged.
     */
    @FXML
    public void showYourSetup(){
        secretObjectiveHBox.setManaged(true);
        secretObjectiveHBox.setVisible(true);
        startCardHBox.setManaged(true);
        startCardHBox.setVisible(true);

        for(ImageView imageView : handCardImages){
            imageView.setManaged(true);
            imageView.setVisible(true);
        }
        for(ImageView imageView : opponentsHandCardImages){
            imageView.setManaged(false);
            imageView.setVisible(false);
        }
    }

    /**
     * This method is responsible for displaying the setup of the other players in the GUI.
     * It makes the ImageView objects in the {@code handCardImages} list invisible and unmanaged.
     * Conversely, it makes the ImageView objects in the {@code opponentsHandCardImages} list visible and managed.
     * It then calls the showOthersHand method with the text of setupButton as the argument.
     */
    @FXML
    public void showPlayerSetup(ActionEvent e){
        for(ImageView imageView : handCardImages){
            imageView.setManaged(false);
            imageView.setVisible(false);
        }
        for(ImageView imageView : opponentsHandCardImages){
            imageView.setManaged(true);
            imageView.setVisible(true);
        }
        Button button = (Button) e.getSource();
        String buttonText = button.getText();
        showOthersHand(buttonText);
    }

    /**
     * This method is responsible for displaying the hand of other players in the GUI.
     * It first hides the secret objectives and start card of the current player.
     * Then, it iterates over the back cards of the other players' hands.
     * If the username of the other player matches the provided user parameter, it displays their hand.
     * For each card in the other player's hand, it creates an Image object based on the card's type (gold or resource) and item.
     * The image is then set to the corresponding ImageView in the opponentsHandCardImages list.
     *
     * @param user The username of the player whose hand is to be displayed.
     */
    private void showOthersHand(String user){
        secretObjectiveHBox.setManaged(false);
        secretObjectiveHBox.setVisible(false);
        startCardHBox.setManaged(false);
        startCardHBox.setVisible(false);

        Map<String, List<BackPlayableCard>> opponentsBackHandCards = setup.getOpponentsBackHandCards();
        for(String username : opponentsBackHandCards.keySet()){
            if(username.equals(user)){
                int i = 0;
                for(BackPlayableCard bpc : opponentsBackHandCards.get(user)){
                    Image backCardImage = getBackCardImage(bpc.getItem(), bpc.getCardType());
                    opponentsHandCardImages.get(i).setImage(backCardImage);
                    i++;
                }
            }
        }
    }

    /**
     * This method is responsible for displaying the gold deck in the GUI.
     * It first retrieves the item type of the gold deck and the visible gold cards from the setup.
     * Then, it creates an Image object for the back of the gold deck based on the item type and sets it to the ImageView of the gold deck.
     * It also creates Image objects for the front of the visible gold cards based on their IDs and sets them to the corresponding ImageViews.
     */
    private void showGoldDeck(){
        Item itemGoldDeck = setup.getGoldDeck();
        ImmPlayableCard[] visibleGoldCards  = setup.getVisibleGoldCards();
        Image image = getBackCardImage(itemGoldDeck, CardType.GOLD);
        goldDeck.setImage(image);

        this.visibleGoldCard0.setImage(getFrontCardImage(visibleGoldCards[0].getId(), CardType.GOLD));
        this.visibleGoldCard1.setImage(getFrontCardImage(visibleGoldCards[1].getId(), CardType.GOLD));
    }

    /**
     * This method is responsible for displaying the resource deck in the GUI.
     * It first retrieves the item type of the resource deck and the visible resource cards from the setup.
     * Then, it creates an Image object for the back of the resource deck based on the item type and sets it to the ImageView of the resource deck.
     * It also creates Image objects for the front of the visible resource cards based on their IDs and sets them to the corresponding ImageViews.
     */
    private void showResourceDeck(){
        Item itemResourceDeck = setup.getResourceDeck();
        ImmPlayableCard[] visibleResourceCards = setup.getVisibleResourceCards();
        Image image = getBackCardImage(itemResourceDeck, CardType.RESOURCE);
        resourceDeck.setImage(image);

        this.visibleResourceCard0.setImage(getFrontCardImage(visibleResourceCards[0].getId(), CardType.RESOURCE));
        this.visibleResourceCard1.setImage(getFrontCardImage(visibleResourceCards[1].getId(), CardType.RESOURCE));
    }

    /**
     * This method is responsible for displaying the common objectives in the GUI.
     * It first retrieves the common objectives from the game setup.
     * Then, it creates an Image object for each common objective card based on the card's ID.
     * The image is then set to the corresponding ImageView in the GUI.
     */
    private void showCommonObjectives(){
        ImmObjectiveCard[] commonObjectives = setup.getCommonObjectives();
        ImmObjectiveCard commonObjective0 = commonObjectives[0];
        ImmObjectiveCard commonObjective1 = commonObjectives[1];
        Image commonObjectiveImage0 = new Image("it/polimi/ingsw/images/cards/objective/front/" + commonObjective0.getId() + ".png");
        Image commonObjectiveImage1 = new Image("it/polimi/ingsw/images/cards/objective/front/" + commonObjective1.getId() + ".png");
        this.commonObjectiveCard0.setImage(commonObjectiveImage0);
        this.commonObjectiveCard1.setImage(commonObjectiveImage1);
    }

    /**
     * This method is responsible for displaying the secret objectives in the GUI.
     * It first retrieves the secret objectives from the game setup.
     * Then, it creates an Image object for each secret objective card based on the card's ID.
     * The image is then set to the corresponding ImageView in the GUI.
     */
    private void showSecretObjectives(){
        ImmObjectiveCard[] secretObjectives = setup.getSecretObjectiveCards();
        objectiveChoice0 = secretObjectives[0];
        objectiveChoice1 = secretObjectives[1];
        Image secretObjectiveImage0 = new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveChoice0.getId() + ".png");
        Image secretObjectiveImage1 = new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveChoice1.getId() + ".png");
        this.secretObjectiveCard0.setImage(secretObjectiveImage0);
        this.secretObjectiveCard1.setImage(secretObjectiveImage1);
    }

    /**
     * This method is responsible for displaying the start card in the GUI.
     * It first retrieves the start card from the game setup.
     * Then, it creates two Image objects for the front and back of the start card based on the card's ID.
     * The images are then set to the corresponding ImageViews in the GUI.
     */
    private void showStartCard(){
        ImmStartCard startCard = setup.getStartCard();
        System.out.println(startCard.getId());
        Image startCardFront = new Image("it/polimi/ingsw/images/cards/playable/start/front/" + startCard.getId() + ".png");
        Image startCardBack = new Image("it/polimi/ingsw/images/cards/playable/start/back/" + startCard.getId() + ".png");
        this.startCardFront.setImage(startCardFront);
        this.startCardBack.setImage(startCardBack);
    }

    /**
     * Method to set the name of the lobby so that the player can see the lobby's name.
     * @param lobbyName the name of the lobby
     */
    private void setLobbyName(String lobbyName) {
        this.lobbyName.setText("GAME: " + lobbyName);
    }

    /**
     * This method is used to finalize the game setup choices.
     * If the player has chosen an objective and decided whether to flip the start card, it sends the choices to the controller.
     * If the player has not made their choices, it prints a message to the console.
     */
    @FXML
    public void readySetup(){
        if(objectiveChosen != null && flippedChoice != null) {
            if (objectiveChosen.equals(objectiveChoice0)) {
                controller.chosenSetup(1, flippedChoice);
            }
            if (objectiveChosen.equals(objectiveChoice1)) {
                controller.chosenSetup(2, flippedChoice);
            }
        }
        else{
            System.out.println("You didn't choose your setup!");
        }
    }

    /**
     * This method is used to update the game setup view when a player leaves the game setup phase.
     * It updates the players'  buttons, chat options, and shows the current player's setup.
     */
    private void update(){
        updatePlayers();
        updateChatOptions();
        showYourSetup();
    }

    /**
     * This method is used to update the players' view in the game setup.
     * It iterates over the players' status and updates the setup buttons accordingly.
     */
    private void updatePlayers(){
        int i = 0;
        for(String user : controller.getPlayersStatus().keySet()) {
            if (!controller.getUsername().equals(user)) {
                setupButtons.get(i).setVisible(true);
                setupButtons.get(i).setText(user);
                i++;
            }
        }
        while(i < setupButtons.size()){
            setupButtons.get(i).setVisible(false);
            setupButtons.get(i).setText("");
            i++;
        }
    }

    /**
     * This method is used to update the chat options in the game setup view.
     * It iterates over the players' status and updates the radio buttons accordingly.
     */
    private void updateChatOptions(){
        int i = 0;
        generalRadioButton.setSelected(true);
        for(String user : controller.getPlayersStatus().keySet()){
            if(!controller.getUsername().equals(user)){
                radioButtons.get(i).setVisible(true);
                radioButtons.get(i).setText(user);
                i++;
            }
        }
        while(i < radioButtons.size()){
            radioButtons.get(i).setVisible(false);
            radioButtons.get(i).setText("");
            i++;
        }
    }

    /**
     * This method is used to quit the game.
     * It sends a {@link QuitGameEvent} to the server and waits for a response.
     */
    @FXML
    public void quitGame(){
        Event event = new QuitGameEvent();
        controller.newViewEvent(event);
        waitForResponse();
    }

    /**
     * This method is used to submit a chat message.
     * It sends a {@link ChatGMEvent} or {@link ChatPMEvent} to the server based on the selected radio button.
     */
    @FXML
    public void submitMessage(){
        String message = chatInput.getText();

        if(message.isEmpty()){
            return;
        }
        else {
            if (generalRadioButton.isSelected()) {
                sendPublicMessage(message);
            } else {
                for (RadioButton button : radioButtons) {
                    if (button.isSelected()) {
                        String username = button.getText();
                        sendPrivateMessage(username, message);
                    }
                }
            }
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

    /**
     * This method is used to check if the player is in the game.
     * It returns true as the player is in the game during the game setup phase.
     */
    @Override
    public boolean inGame(){
        return true;
    }

    /**
     * This method is used to check if the player is in the chat.
     * It returns true as the player is in the chat during the game setup phase.
     */
    @Override
    public boolean inChat(){
        return true;
    }
}
