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
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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


    public GameSetupController(){
        super();
    }

    @Override
    public void run(View view, Stage stage) {

        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        setLobbyName(controller.getLobbyId());
        chatArea.appendText("You're playing in the lobby: " + controller.getLobbyId() + "\n");

    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch(EventID.getByID(eventID)){
            case CHOOSE_CARDS_SETUP:
                double desiredWidth = 180;
                double desiredHeight = 120;

                ViewStartSetup setup = controller.getSetup();
                Item itemResourceDeck = setup.getResourceDeck();
                ImmPlayableCard[] visibleResourceCards = setup.getVisibleResourceCards();
                ImmPlayableCard[] visibleGoldCards  = setup.getVisibleGoldCards();
                System.out.println("SEI fuori dallo switch");
                switch(itemResourceDeck) {
                    case FUNGI:
                        Image FB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/FB.png", desiredHeight, desiredWidth, true, false);
                        resourceDeck.setImage(FB);
                        System.out.println("SEI nello switch");
                        break;
                    case ANIMAL:
                        Image AB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/AB.png", desiredHeight, desiredWidth, true, false);
                        resourceDeck.setImage(AB);
                        System.out.println("SEI nello switch");
                        break;
                    case INSECT:
                        Image IB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/IB.png", desiredHeight, desiredWidth, true, false);
                        resourceDeck.setImage(IB);
                        System.out.println("SEI nello switch");
                        break;
                    case PLANT:
                        Image PB = new Image("it/polimi/ingsw/images/cards/playable/resource/back/PB.png", desiredHeight, desiredWidth, true, false);
                        resourceDeck.setImage(PB);
                        System.out.println("SEI nello switch");
                    break;
                }


                break;
            case UPDATE_GAME_PLAYERS:
                break;
            case CHOSEN_CARDS_SETUP:
                break;
            case CHOOSE_TOKEN_SETUP:
                break;
            case QUIT_GAME:
                break;
        }
    }

    /**
     * Method to set the name of the lobby so that the player can see the lobby's name.
     *
     * @param lobbyName the name of the lobby
     */
    public void setLobbyName(String lobbyName) {
        this.lobbyName.setText("LOBBY: " + lobbyName);
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

    @FXML
    public void quit(){
        Platform.exit();
        System.exit(0);
    }

    //TODO thread problem, viewcontroller sees lobbycontroller sometimes and gamesetupcontroller other times
    @Override
    public boolean inGame(){
        System.out.println("Using the game setup in game!");
        return true;
    }

    @Override
    public boolean inChat(){
        return true;
    }
}
