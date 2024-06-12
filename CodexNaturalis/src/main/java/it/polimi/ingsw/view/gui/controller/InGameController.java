package it.polimi.ingsw.view.gui.controller;


import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class InGameController extends FXMLController {

    @FXML
    private AnchorPane scoreBoard;


    @FXML
    private  BorderPane borderPane;

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
    private ComboBox<String> chatChoice;

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
    private ImageView handCard0;

    @FXML
    private ImageView handCard1;

    @FXML
    private ImageView handCard2;



    @FXML
    private Label gameName;

    @FXML
    private Label aboveLabel;

    @FXML
    private ComboBox<?> playAreaChoice;



    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        
        setGameName();

        chatInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                submitMessage();
            }
        });
    }
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
    }



    private void setGameName() {
        this.gameName.setText(controller.getLobbyId());
    }



    /**
     * This method is used to submit a chat message.
     * It sends a {@link ChatGMEvent} or {@link ChatPMEvent} to the server based on the selected radio button.
     */
    @FXML
    public void submitMessage(){
        String message = chatInput.getText();

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
