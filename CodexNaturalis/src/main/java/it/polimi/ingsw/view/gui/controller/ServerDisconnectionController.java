package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
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
     * Method to close the GUI.
     * Calls Platform.exit() and System.exit(0) to close the application.
     */
    @FXML
    public void closeGui() {
        Platform.exit();
        System.exit(0);
    }

}
