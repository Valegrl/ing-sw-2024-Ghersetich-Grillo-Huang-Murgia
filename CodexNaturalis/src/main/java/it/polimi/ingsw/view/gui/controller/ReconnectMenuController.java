package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ReconnectMenuController extends FXMLController {

    @FXML
    private Label gameLabel;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private Button quitButton;

    @FXML
    private Label reconnectLabel;

    @FXML
    private AnchorPane upperAnchor;

    public ReconnectMenuController(){
        super();
    }

    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();


    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }

}


