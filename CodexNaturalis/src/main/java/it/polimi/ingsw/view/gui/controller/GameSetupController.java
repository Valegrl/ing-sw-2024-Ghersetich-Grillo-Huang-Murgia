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
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.tui.state.MenuState;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.viewModel.immutableCard.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameSetupController extends FXMLController {

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

    private final double desiredWidth = 180;

    private final double desiredHeight = 120;

    private ImmObjectiveCard objectiveChoice0;

    private ImmObjectiveCard objectiveChoice1;

    private ImmObjectiveCard objectiveChosen;

    private Boolean flippedChoice;

    private ImmStartCard startCard;

    private List<Button> setupButtons;

    private List<RadioButton> radioButtons;

    private List<ImageView> handCardImages;

    private List<ImageView> opponentsHandCardImages;

    private List<ImmPlayableCard> myHand;



    public GameSetupController(){
        super();
    }

    @Override
    public void run(View view, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/GameSetup.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = stage.getScene();
            scene.setRoot(root);

            setLobbyName(controller.getLobbyId());
            chatArea.appendText("You're playing in the lobby: " + controller.getLobbyId() + "\n");
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
        }
        catch(IOException exception){
            exception.printStackTrace();
        }

        //To load the chat from the lobby
        controller.newViewEvent(new GetChatMessagesEvent());
        waitForResponse();

        addHooverEffect();
        addChoiceSelection();


    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch(EventID.getByID(eventID)){
            case CHOOSE_CARDS_SETUP:
                this.setup = controller.getSetup();
                if(setup != null) {
                    Platform.runLater(() -> run(view, stage));
                }
                else{
                    System.out.println("Error, setup not initialized !");
                }
                break;

            case GET_CHAT_MESSAGES:
                Platform.runLater(() -> chatArea.appendText(message));
                break;

            case CHOSEN_CARDS_SETUP:
                System.out.println("You have chosen your cards!");
                break;
            case UPDATE_GAME_PLAYERS:
                Platform.runLater(this::update);
                break;
            case CHOOSE_TOKEN_SETUP:
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
                    chatArea.appendText(message + "\n");
                } else {
                    System.out.println("Message unable to send!");
                }
                break;
        }
        notifyResponse();
    }

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
            System.out.println("Chosen objective card objective card " + objectiveChosen.getId());
        });

        secretObjectiveCard1.setOnMouseClicked(event -> {
            secretObjectiveCard1.setEffect(highlight);
            secretObjectiveCard0.setEffect(null);
            objectiveChosen = objectiveChoice1;
            System.out.println("Chosen objective card objective card " + objectiveChosen.getId());
        });

        startCardFront.setOnMouseClicked(event -> {
            startCardFront.setEffect(highlight);
            startCardBack.setEffect(null);
            flippedChoice = false;
            System.out.println("Chosen start card on front");
        });

        startCardBack.setOnMouseClicked(event -> {
            startCardBack.setEffect(highlight);
            startCardFront.setEffect(null);
            flippedChoice = true;
            System.out.println("Chosen start card on back");
        });
    }

    private void addHooverEffect(){
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.WHITE);
        highlight.setRadius(10.0);
        highlight.setOffsetX(0);
        highlight.setOffsetY(0);

        secretObjectiveCard0.setOnMouseEntered(event -> {
            secretObjectiveCard0.setEffect(highlight);
        });

        secretObjectiveCard0.setOnMouseExited(event -> {
            if(objectiveChosen == null || (!objectiveChosen.equals(objectiveChoice0))) {
                secretObjectiveCard0.setEffect(null);
            }
        });

        secretObjectiveCard1.setOnMouseEntered(event -> {
            secretObjectiveCard1.setEffect(highlight);
        });

        secretObjectiveCard1.setOnMouseExited(event -> {
            if(objectiveChosen == null || (!objectiveChosen.equals(objectiveChoice1))) {
                secretObjectiveCard1.setEffect(null);
            }
        });

        startCardFront.setOnMouseEntered(event -> {
            startCardFront.setEffect(highlight);
        });

        startCardFront.setOnMouseExited(event -> {
            if(flippedChoice == null || flippedChoice) {
                startCardFront.setEffect(null);
            }
        });

        startCardBack.setOnMouseEntered(event -> {
            startCardBack.setEffect(highlight);
        });
        startCardBack.setOnMouseExited(event -> {
            if(flippedChoice == null || !flippedChoice) {
                startCardBack.setEffect(null);
            }
        });

        int i = 0;
        for(ImmPlayableCard card : myHand){
            if(card.getType() ==  CardType.GOLD){
                ImageView currHandImage = handCardImages.get(i);
                Image frontCardImage = handCardImages.get(i).getImage();

                switch(card.getPermanentResource()){
                    case FUNGI:
                        Image FB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/FB.png", desiredHeight, desiredWidth, true, false);
                        currHandImage.setOnMouseEntered(event -> {
                            currHandImage.setImage(FB);
                        });
                        currHandImage.setOnMouseExited(event -> {
                            currHandImage.setImage(frontCardImage);
                        });
                        break;
                    case ANIMAL:
                        Image AB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/AB.png", desiredHeight, desiredWidth, true, false);
                        currHandImage.setOnMouseEntered(event -> {
                            currHandImage.setImage(AB);
                        });
                        currHandImage.setOnMouseExited(event -> {
                            currHandImage.setImage(frontCardImage);
                        });
                        break;
                    case INSECT:
                        Image IB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/IB.png", desiredHeight, desiredWidth, true, false);
                        currHandImage.setOnMouseEntered(event -> {
                            currHandImage.setImage(IB);
                        });
                        currHandImage.setOnMouseExited(event -> {
                            currHandImage.setImage(frontCardImage);
                        });
                        break;
                    case PLANT:
                        Image PB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/PB.png", desiredHeight, desiredWidth, true, false);
                        currHandImage.setOnMouseEntered(event -> {
                            currHandImage.setImage(PB);
                        });
                        currHandImage.setOnMouseExited(event -> {
                            currHandImage.setImage(frontCardImage);
                        });
                        break;
                }

            }
            else{
                ImageView currHandImage = handCardImages.get(i);
                Image frontCardImage = handCardImages.get(i).getImage();

                switch(card.getPermanentResource()){
                    case FUNGI:
                        Image FB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/FB.png", desiredHeight, desiredWidth, true, false);
                        currHandImage.setOnMouseEntered(event -> {
                            currHandImage.setImage(FB);
                        });
                        currHandImage.setOnMouseExited(event -> {
                            currHandImage.setImage(frontCardImage);
                        });
                        break;
                    case ANIMAL:
                        Image AB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/AB.png", desiredHeight, desiredWidth, true, false);
                        currHandImage.setOnMouseEntered(event -> {
                            currHandImage.setImage(AB);
                        });
                        currHandImage.setOnMouseExited(event -> {
                            currHandImage.setImage(frontCardImage);
                        });
                        break;
                    case INSECT:
                        Image IB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/IB.png", desiredHeight, desiredWidth, true, false);
                        currHandImage.setOnMouseEntered(event -> {
                            currHandImage.setImage(IB);
                        });
                        currHandImage.setOnMouseExited(event -> {
                            currHandImage.setImage(frontCardImage);
                        });
                        break;
                    case PLANT:
                        Image PB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/PB.png", desiredHeight, desiredWidth, true, false);
                        currHandImage.setOnMouseEntered(event -> {
                            currHandImage.setImage(PB);
                        });
                        currHandImage.setOnMouseExited(event -> {
                            currHandImage.setImage(frontCardImage);
                        });
                        break;
                }
            }
            i++;
        }
    }

    private void showMyHand(){
        int i = 0;
        for(ImmPlayableCard card : myHand){
            if(card.getType() == CardType.GOLD) {
                Image cardImage = new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + card.getId() + ".png", desiredHeight, desiredWidth, true, false);
                handCardImages.get(i).setImage(cardImage);
            }
            else{
                Image cardImage = new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + card.getId() + ".png", desiredHeight, desiredWidth, true, false);
                handCardImages.get(i).setImage(cardImage);
            }
            i++;
        }
    }

    @FXML
    public void showYourSetup(){
        /*
        secretObjectiveCard0.setManaged(true);
        secretObjectiveCard0.setVisible(true);
        secretObjectiveCard1.setManaged(true);
        secretObjectiveCard1.setVisible(true);
        startCardFront.setManaged(true);
        startCardFront.setVisible(true);
        startCardBack.setManaged(true);
        startCardBack.setVisible(true);
        */
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

    @FXML
    public void showPlayerSetup1(){
        for(ImageView imageView : handCardImages){
            imageView.setManaged(false);
            imageView.setVisible(false);
        }
        for(ImageView imageView : opponentsHandCardImages){
            imageView.setManaged(true);
            imageView.setVisible(true);
        }
        showOthersHand(setupButton1.getText());
    }
    @FXML
    public void showPlayerSetup2(){
        for(ImageView imageView : handCardImages){
            imageView.setManaged(false);
            imageView.setVisible(false);
        }
        for(ImageView imageView : opponentsHandCardImages){
            imageView.setManaged(true);
            imageView.setVisible(true);
        }
        showOthersHand(setupButton2.getText());
    }
    @FXML
    public void showPlayerSetup3(){
        for(ImageView imageView : handCardImages){
            imageView.setManaged(false);
            imageView.setVisible(false);
        }
        for(ImageView imageView : opponentsHandCardImages){
            imageView.setManaged(true);
            imageView.setVisible(true);
        }
        showOthersHand(setupButton3.getText());
    }

    private void showOthersHand(String user){
        /*
        secretObjectiveCard0.setManaged(false);
        secretObjectiveCard0.setVisible(false);
        secretObjectiveCard1.setManaged(false);
        secretObjectiveCard1.setVisible(false);
        startCardFront.setManaged(false);
        startCardFront.setVisible(false);
        startCardBack.setManaged(false);
        startCardBack.setVisible(false);
         */

        secretObjectiveHBox.setManaged(false);
        secretObjectiveHBox.setVisible(false);
        startCardHBox.setManaged(false);
        startCardHBox.setVisible(false);

        Map<String, List<BackPlayableCard>> opponentsBackHandCards = setup.getOpponentsBackHandCards();
        for(String username : opponentsBackHandCards.keySet()){
            if(username.equals(user)){
                int i = 0;
                for(BackPlayableCard bpc : opponentsBackHandCards.get(user)){
                    if(bpc.getCardType() == CardType.GOLD){
                        switch(bpc.getItem()) {
                            case FUNGI:
                                Image FB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/FB.png", desiredHeight, desiredWidth, true, false);
                                opponentsHandCardImages.get(i).setImage(FB);
                                break;
                            case ANIMAL:
                                Image AB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/AB.png", desiredHeight, desiredWidth, true, false);
                                opponentsHandCardImages.get(i).setImage(AB);
                                break;
                            case INSECT:
                                Image IB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/IB.png", desiredHeight, desiredWidth, true, false);
                                opponentsHandCardImages.get(i).setImage(IB);
                                break;
                            case PLANT:
                                Image PB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/PB.png", desiredHeight, desiredWidth, true, false);
                                opponentsHandCardImages.get(i).setImage(PB);
                                break;
                        }
                    }
                    else{
                        switch(bpc.getItem()) {
                            case FUNGI:
                                Image FB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/FB.png", desiredHeight, desiredWidth, true, false);
                                opponentsHandCardImages.get(i).setImage(FB);
                                break;
                            case ANIMAL:
                                Image AB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/AB.png", desiredHeight, desiredWidth, true, false);
                                opponentsHandCardImages.get(i).setImage(AB);
                                break;
                            case INSECT:
                                Image IB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/IB.png", desiredHeight, desiredWidth, true, false);
                                opponentsHandCardImages.get(i).setImage(IB);
                                break;
                            case PLANT:
                                Image PB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/PB.png", desiredHeight, desiredWidth, true, false);
                                opponentsHandCardImages.get(i).setImage(PB);
                                break;
                        }
                    }
                    i++;
                }
            }
        }

    }

    private void showGoldDeck(){
        Item itemGoldDeck = setup.getGoldDeck();
        ImmPlayableCard[] visibleGoldCards  = setup.getVisibleGoldCards();
        switch(itemGoldDeck) {
            case FUNGI:
                Image FB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/FB.png", desiredHeight, desiredWidth, true, false);
                goldDeck.setImage(FB);
                break;
            case ANIMAL:
                Image AB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/AB.png", desiredHeight, desiredWidth, true, false);
                goldDeck.setImage(AB);
                break;
            case INSECT:
                Image IB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/IB.png", desiredHeight, desiredWidth, true, false);
                goldDeck.setImage(IB);
                break;
            case PLANT:
                Image PB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/PB.png", desiredHeight, desiredWidth, true, false);
                goldDeck.setImage(PB);
                break;
        }

        ImmPlayableCard visibleGoldCard0 = visibleGoldCards[0];
        ImmPlayableCard visibleGoldCard1 = visibleGoldCards[1];
        Image visibleGoldCardImage0 = new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + visibleGoldCard0.getId() + ".png", desiredHeight, desiredWidth, true, false);
        Image visibleGoldCardImage1 = new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + visibleGoldCard1.getId() + ".png", desiredHeight, desiredWidth, true, false);
        this.visibleGoldCard0.setImage(visibleGoldCardImage0);
        this.visibleGoldCard1.setImage(visibleGoldCardImage1);

    }

    private void showResourceDeck(){
        Item itemResourceDeck = setup.getResourceDeck();
        ImmPlayableCard[] visibleResourceCards = setup.getVisibleResourceCards();
        switch(itemResourceDeck) {
            case FUNGI:
                Image FB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/FB.png", desiredHeight, desiredWidth, true, false);
                resourceDeck.setImage(FB);
                break;
            case ANIMAL:
                Image AB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/AB.png", desiredHeight, desiredWidth, true, false);
                resourceDeck.setImage(AB);
                break;
            case INSECT:
                Image IB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/IB.png", desiredHeight, desiredWidth, true, false);
                resourceDeck.setImage(IB);
                break;
            case PLANT:
                Image PB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/PB.png", desiredHeight, desiredWidth, true, false);
                resourceDeck.setImage(PB);
                break;
        }

        ImmPlayableCard visibleResourceCard0 = visibleResourceCards[0];
        ImmPlayableCard visibleResourceCard1 = visibleResourceCards[1];
        Image visibleResourceCardImage0 = new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + visibleResourceCard0.getId() + ".png", desiredHeight, desiredWidth, true, false);
        Image visibleResourceCardImage1 = new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + visibleResourceCard1.getId() + ".png", desiredHeight, desiredWidth, true, false);
        this.visibleResourceCard0.setImage(visibleResourceCardImage0);
        this.visibleResourceCard1.setImage(visibleResourceCardImage1);

    }

    private void showCommonObjectives(){
        ImmObjectiveCard[] commonObjectives = setup.getCommonObjectives();
        ImmObjectiveCard commonObjective0 = commonObjectives[0];
        ImmObjectiveCard commonObjective1 = commonObjectives[1];
        Image commonObjectiveImage0 = new Image("it/polimi/ingsw/images/cards/objective/front/" + commonObjective0.getId() + ".png", desiredHeight, desiredWidth, true, false);
        Image commonObjectiveImage1 = new Image("it/polimi/ingsw/images/cards/objective/front/" + commonObjective1.getId() + ".png", desiredHeight, desiredWidth, true, false);
        this.commonObjectiveCard0.setImage(commonObjectiveImage0);
        this.commonObjectiveCard1.setImage(commonObjectiveImage1);
    }

    private void showSecretObjectives(){
        ImmObjectiveCard[] secretObjectives = setup.getSecretObjectiveCards();
        objectiveChoice0 = secretObjectives[0];
        objectiveChoice1 = secretObjectives[1];

        Image secretObjectiveImage0 = new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveChoice0.getId() + ".png", desiredHeight, desiredWidth, true, false);
        Image secretObjectiveImage1 = new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveChoice1.getId() + ".png", desiredHeight, desiredWidth, true, false);
        this.secretObjectiveCard0.setImage(secretObjectiveImage0);
        this.secretObjectiveCard1.setImage(secretObjectiveImage1);
    }

    private void showStartCard(){
        startCard = setup.getStartCard();
        System.out.println(startCard.getId());
        Image startCardFront = new Image("it/polimi/ingsw/images/cards/playable/start/front/" + startCard.getId() + ".png", desiredHeight, desiredWidth, true, false);
        Image startCardBack = new Image("it/polimi/ingsw/images/cards/playable/start/back/" + startCard.getId() + ".png", desiredHeight, desiredWidth, true, false);
        this.startCardFront.setImage(startCardFront);
        this.startCardBack.setImage(startCardBack);
    }

    /**
     * Method to set the name of the lobby so that the player can see the lobby's name.
     * @param lobbyName the name of the lobby
     */
    private void setLobbyName(String lobbyName) {
        this.lobbyName.setText("LOBBY: " + lobbyName);
    }

    public void setupController(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
    }

    @FXML
    public void readySetup(){
        if(objectiveChosen != null && flippedChoice != null) {
            if (objectiveChosen.equals(objectiveChoice0)) {
                controller.chosenSetup(1, flippedChoice);
            }
            if (objectiveChosen.equals(objectiveChoice1)) {
                controller.chosenSetup(2, flippedChoice);
            }
            readyButton.setVisible(false);
        }
        else{
            System.out.println("You didn't choose your setup!");
        }
    }

    private void update(){
        updatePlayers();
        updateChatOptions();
    }

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

    @Override
    public boolean inGame(){
        return true;
    }

    @Override
    public boolean inChat(){
        return true;
    }
}
