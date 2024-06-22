package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ServerDisconnectionController extends FXMLController {

    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }

    /**
     * Method to reset the GUI.
     */
    @FXML
    public void resetGui() {
        view.resetUI();
    }

}
