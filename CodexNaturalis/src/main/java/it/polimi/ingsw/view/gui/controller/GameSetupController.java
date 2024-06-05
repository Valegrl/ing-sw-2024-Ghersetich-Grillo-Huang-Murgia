package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GameSetupController extends FXMLController {

    @FXML
    private Label lobbyName;

    @FXML
    private ImageView resourceCardTop;

    @FXML
    private ImageView resourceCard1;

    @FXML
    private ImageView resourceCard2;


    @FXML
    private ImageView goldCardTop;

    @FXML
    private ImageView goldCard1;

    @FXML
    private ImageView goldCard2;


    //Hand cards.
    @FXML
    private ImageView handCard1;

    @FXML
    private ImageView handCard2;

    @FXML
    private ImageView handCard3;


    @FXML
    private ImageView commonObjectiveCard1;

    @FXML
    private ImageView commonObjectiveCard2;

    @FXML
    private ImageView secretObjectiveCard1;

    @FXML
    private ImageView secretObjectiveCard2;

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

    }
}
