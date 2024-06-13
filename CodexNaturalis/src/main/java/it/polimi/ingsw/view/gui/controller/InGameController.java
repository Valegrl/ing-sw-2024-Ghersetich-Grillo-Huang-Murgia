package it.polimi.ingsw.view.gui.controller;


import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.viewModel.ViewModel;
import it.polimi.ingsw.viewModel.immutableCard.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
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
    private TextArea updatesArea;



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



    private List<ImmPlayableCard> myHand;

    private ImmPlayableCard goldCard0;

    private ImmPlayableCard goldCard1;

    private ImmPlayableCard resourceCard0;

    private ImmPlayableCard resourceCard1;

    private ViewModel viewModel;

    boolean hasTurn;



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



    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        this.viewModel = controller.getModel();

        handCardImages = Arrays.asList(handCard0, handCard1, handCard2);
        uncoveredList = Arrays.asList(uncovered1, uncovered2, uncovered3, uncovered4);
        uncoveredUsernames = Arrays.asList(uncoveredUsername1, uncoveredUsername2, uncoveredUsername3, uncoveredUsername4);

        playerPoints = Arrays.asList(playerPoints1, playerPoints2, playerPoints3, playerPoints4);
        animalOccurrences = Arrays.asList(animalOcc1, animalOcc2, animalOcc3, animalOcc4);
        fungiOccurrences = Arrays.asList(fungiOcc1, fungiOcc2, fungiOcc3, fungiOcc4);
        plantOccurrences = Arrays.asList(plantOcc1, plantOcc2, plantOcc3, plantOcc4);
        insectOccurrences = Arrays.asList(insectOcc1, insectOcc2, insectOcc3, insectOcc4);
        inkwellOccurrences = Arrays.asList(inkwellOcc1, inkwellOcc2, inkwellOcc3, inkwellOcc4);
        quillOccurrences = Arrays.asList(quillOcc1, quillOcc2, quillOcc3, quillOcc4);
        manuscriptOccurrences = Arrays.asList(manuscriptOcc1, manuscriptOcc2, manuscriptOcc3, manuscriptOcc4);

        setGameName();
        updateDecks();
        updateHand();
        updatePoints();
        updateChatOptions();
        updateVisiblePlayAreas();

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
            case EventID.UPDATE_LOCAL_MODEL:

                break;
            case EventID.CHAT_GM, CHAT_PM:
                if (feedback.equals(Feedback.SUCCESS)) {
                    String formattedMessage = message.replace("[1m", " ").replace("[0m","");
                    chatArea.appendText(formattedMessage + "\n");
                } else {
                    System.out.println("Message unable to send!");
                }
                break;
        }
    }


    private void setGameName() {
        this.gameName.setText(controller.getLobbyId());
    }


    private void checkTurn(){
        if(controller.hasTurn()){
            hasTurn = true;
        }
        else{
            hasTurn = false;
        }
    }

    private void updateDecks(){
        ImmPlayableCard[] visibleGoldCards = viewModel.getVisibleGoldCards();
        ImmPlayableCard[] visibleResourceCards = viewModel.getVisibleResourceCards();
        goldCard0 = visibleGoldCards[0];
        goldCard1 = visibleGoldCards[1];
        resourceCard0 = visibleResourceCards[0];
        resourceCard1 = visibleResourceCards[1];

        visibleGoldCard0.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + goldCard0.getId() + ".png"));
        visibleGoldCard1.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + goldCard1.getId() + ".png"));
        visibleResourceCard0.setImage(new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + resourceCard0.getId() + ".png"));
        visibleResourceCard1.setImage(new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + resourceCard1.getId() + ".png"));

        Item itemGoldDeck = viewModel.getTopGoldDeck();
        switch(itemGoldDeck) {
            case FUNGI:
                Image FB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/FB.png");
                goldDeck.setImage(FB);
                break;
            case ANIMAL:
                Image AB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/AB.png");
                goldDeck.setImage(AB);
                break;
            case INSECT:
                Image IB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/IB.png");
                goldDeck.setImage(IB);
                break;
            case PLANT:
                Image PB = new Image("it/polimi/ingsw/images/cards/playable/gold/back/PB.png");
                goldDeck.setImage(PB);
                break;
        }

        Item itemResourceDeck = viewModel.getTopResourceDeck();
        switch(itemResourceDeck) {
            case FUNGI:
                Image FB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/FB.png");
                resourceDeck.setImage(FB);
                break;
            case ANIMAL:
                Image AB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/AB.png");
                resourceDeck.setImage(AB);
                break;
            case INSECT:
                Image IB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/IB.png");
                resourceDeck.setImage(IB);
                break;
            case PLANT:
                Image PB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/PB.png");
                resourceDeck.setImage(PB);
                break;
        }

    }

    private void updateHand(){
        int i = 0;
        myHand = viewModel.getSelfPlayer().getPlayArea().getHand();
        for(ImmPlayableCard card : myHand){
            ImageView currImageView = handCardImages.get(i);
            if(card.getType().equals(CardType.GOLD)){
                currImageView.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + card.getId() + ".png"));
            }
            else{
                currImageView.setImage(new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + card.getId() + ".png"));
            }
            i++;
        }
    }

    private void updatePoints(){
        int i = 0;
        int currPoints;
        String curr;

        Map<Item, Integer> currUncoveredItems;
        Map<String, Integer> scoreboard = viewModel.getScoreboard();

        for(AnchorPane pane : uncoveredList){
            pane.setVisible(false);
        }

        for(String user : controller.getInMatchPlayerUsernames()){
            uncoveredList.get(i).setVisible(true);
            if(controller.isOnline(user)) {
                uncoveredUsernames.get(i).setText(user);
            }
            else{
                uncoveredUsernames.get(i).setText(user + " (offline)");
            }

            currPoints = scoreboard.get(user);
            playerPoints.get(i).setText(String.valueOf(currPoints));

            if(user.equals(controller.getUsername())){
                currUncoveredItems = viewModel.getSelfPlayer().getPlayArea().getUncoveredItems();
            }
            else {
                currUncoveredItems = viewModel.getOpponent(user).getPlayArea().getUncoveredItems();
            }

            for(Item item : currUncoveredItems.keySet()){
                switch(item){
                    case FUNGI:
                        curr = String.valueOf(currUncoveredItems.get(item));
                        fungiOccurrences.get(i).setText(curr);
                        break;
                    case ANIMAL:
                        curr = String.valueOf(currUncoveredItems.get(item));
                        animalOccurrences.get(i).setText(curr);
                        break;
                    case PLANT:
                        curr = String.valueOf(currUncoveredItems.get(item));
                        plantOccurrences.get(i).setText(curr);
                        break;
                    case INSECT:
                        curr = String.valueOf(currUncoveredItems.get(item));
                        insectOccurrences.get(i).setText(curr);
                        break;
                    case INKWELL:
                        curr = String.valueOf(currUncoveredItems.get(item));
                        inkwellOccurrences.get(i).setText(curr);
                        break;
                    case QUILL:
                        curr = String.valueOf(currUncoveredItems.get(item));
                        quillOccurrences.get(i).setText(curr);
                        break;
                    case MANUSCRIPT:
                        curr = String.valueOf(currUncoveredItems.get(item));
                        manuscriptOccurrences.get(i).setText(curr);
                        break;
                }
            }

            i++;
        }
    }

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

    private void updateVisiblePlayAreas(){
        playAreaSelection.getItems().clear();

        playAreaSelection.getItems().add("Your board");
        playAreaSelection.getSelectionModel().select("Your board");

        for(String user : controller.getPlayersStatus().keySet()){
            if(!controller.getUsername().equals(user)){
                playAreaSelection.getItems().add("Opponent: " + user);
            }
        }
    }

    @FXML
    public void showPlayArea() {
        String playAreaChoice = playAreaSelection.getValue();
        if(playAreaChoice.equals("Your board")){
            gridPane.setManaged(true);
            gridPane.setVisible(true);
            opponentGridPane.setVisible(false);
            opponentGridPane.setManaged(false);
            secretObjectiveAnchor.setVisible(true);
            handLabel.setText("MY HAND");
            List<ImageView> handCards = Arrays.asList(handCard0, handCard1, handCard2);
            for(ImageView handCard : handCards){
                handCard.setVisible(true);
                handCard.setManaged(true);
            }
            List<ImageView> opponentHandCards = Arrays.asList(opponentHandCard0, opponentHandCard1, opponentHandCard2);
            for(ImageView handCard : opponentHandCards){
                handCard.setVisible(false);
                handCard.setManaged(false);
            }
            //TODO show the played cards of this player
        }
        else{
            gridPane.setManaged(false);
            gridPane.setVisible(false);
            opponentGridPane.setVisible(true);
            opponentGridPane.setManaged(true);
            secretObjectiveAnchor.setVisible(false);

            String username = playAreaChoice.substring(10);
            handLabel.setText(username + " hand");
            List<ImageView> handCards = Arrays.asList(handCard0, handCard1, handCard2);
            for(ImageView handCard : handCards){
                handCard.setVisible(false);
                handCard.setManaged(false);
            }
            List<ImageView> opponentHandCards = Arrays.asList(opponentHandCard0, opponentHandCard1, opponentHandCard2);
            List<BackPlayableCard> backHand = viewModel.getOpponent(username).getPlayArea().getHand();
            int i = 0;

            for(ImageView handCard : opponentHandCards){
                handCard.setVisible(true);
                handCard.setManaged(true);
                Item item = backHand.get(i).getItem();
                if(backHand.get(i).getCardType() == CardType.GOLD){
                    switch (item){
                        case FUNGI:
                            handCard.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/back/FB.png"));
                            break;
                        case ANIMAL:
                            handCard.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/back/AB.png"));
                            break;
                        case PLANT:
                            handCard.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/back/PB.png"));
                            break;
                        case INSECT:
                            handCard.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/back/IB.png"));
                            break;
                    }
                }
                else{
                    switch (item){
                        case FUNGI:
                            handCard.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/back/FB.png"));
                            break;
                        case ANIMAL:
                            handCard.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/back/AB.png"));
                            break;
                        case PLANT:
                            handCard.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/back/PB.png"));
                            break;
                        case INSECT:
                            handCard.setImage(new Image("it/polimi/ingsw/images/cards/playable/gold/back/IB.png"));
                            break;
                    }
                }
                i++;
            }
            //TODO show the opponent's play area
        }
    }

    private void showCommonObjectives(){
        ImmObjectiveCard[] objectiveCards = viewModel.getCommonObjectives();
        ImmObjectiveCard objectiveCard0 = objectiveCards[0];
        ImmObjectiveCard objectiveCard1 = objectiveCards[1];
        commonObjectiveCard0.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveCard0.getId() + ".png"));
        commonObjectiveCard1.setImage(new Image("it/polimi/ingsw/images/cards/objective/front/" + objectiveCard1.getId() + ".png"));

    }

    private void showSecretObjective(){
        ImmObjectiveCard secretObjCard = viewModel.getSelfPlayer().getSecretObjective();
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


    @Override
    public boolean inGame(){
        return true;
    }

    @Override
    public boolean inChat(){ return true;}

}
