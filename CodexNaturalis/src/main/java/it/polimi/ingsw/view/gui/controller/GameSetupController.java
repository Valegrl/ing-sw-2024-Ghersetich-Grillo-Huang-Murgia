package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GameSetupController extends FXMLController {

    @FXML
    private ImageView resourceCard1;

    @FXML
    private ImageView resourceCard2;

    @FXML
    private ImageView resourceCardTop;

    @FXML
    private ImageView goldCard1;

    @FXML
    private ImageView goldCard2;

    @FXML
    private ImageView goldCardTop;

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
    private Button setupButton2;

    @FXML
    private Button setupButton3;

    @FXML
    private Button setupButton4;


    public GameSetupController(){
        super();
    }

    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

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


}
