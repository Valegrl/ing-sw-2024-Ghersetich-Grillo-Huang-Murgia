package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.viewModel.immutableCard.ImmGoldCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmStartCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameSetupController extends FXMLController {

    @FXML
    private Label lobbyName;

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
    private ImageView commonObjectiveCard0;

    @FXML
    private ImageView commonObjectiveCard1;

    @FXML
    private ImageView secretObjectiveCard0;

    @FXML
    private ImageView secretObjectiveCard1;

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

    private boolean flipped;

    private ImmStartCard startCard;

    private List<Button> setupButtons;

    private List<RadioButton> radioButtons;


    public GameSetupController(){
        super();
    }

    @Override
    public void run(View view, Stage stage) {

        if(setup != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/GameSetup.fxml"));
                loader.setController(this);

                Parent root = loader.load();
                Scene scene = stage.getScene();
                scene.setRoot(root);

                setLobbyName(controller.getLobbyId());
                chatArea.appendText("You're playing in the lobby: " + controller.getLobbyId() + "\n");

                showResourceDeck();
                showGoldDeck();
                showMyHand();
                showCommonObjectives();
                showSecretObjectives();
                showStartCard();

                setupButtons = Arrays.asList(setupButton1, setupButton2, setupButton3);
                radioButtons = Arrays.asList(radioButton1, radioButton2, radioButton3);
                update();

            }
            catch(IOException exception){
                exception.printStackTrace();
            }
        }
        else {
            System.out.println("Something bad happened!");
        }

    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch(EventID.getByID(eventID)){
            case CHOOSE_CARDS_SETUP:
                this.setup = controller.getSetup();
                Platform.runLater(() -> run(view, stage));
                break;

            case CHOSEN_CARDS_SETUP:
                break;
            case CHOOSE_TOKEN_SETUP:
                break;
            case QUIT_GAME:
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

    private void showMyHand(){
        List<ImageView> handCardImage = Arrays.asList(handCard0, handCard1, handCard2);
        List<ImmPlayableCard> myHand = setup.getHand();
        int i = 0;
        for(ImmPlayableCard card : myHand){
            if(card instanceof ImmGoldCard) {
                Image cardImage = new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + card.getId() + ".png", desiredHeight, desiredWidth, true, false);
                handCardImage.get(i).setImage(cardImage);
            }
            else{
                Image cardImage = new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + card.getId() + ".png", desiredHeight, desiredWidth, true, false);
                handCardImage.get(i).setImage(cardImage);
            }
            i++;
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
    public void setupReady(ImmObjectiveCard secretObjective, ImmStartCard startCard){

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
                setupButtons.get(i).setText(user + "setup");
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
    public void quit(){
        //TODO return to enter lobbies...
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
