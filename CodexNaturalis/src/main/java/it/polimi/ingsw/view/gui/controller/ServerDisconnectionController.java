package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * The ServerDisconnectionController class is responsible for managing the server disconnection view in the GUI.
 * It extends the FXMLController class and overrides several of its methods.
 * This class is responsible for handling the server disconnection event and resetting the GUI.
 */
public class ServerDisconnectionController extends FXMLController {

    /**
     * This method is used to initialize the ServerDisconnectionController with the given View and Stage.
     * It sets the view, stage, and controller properties of this class.
     *
     * @param view  The View object to be associated with this controller.
     * @param stage The Stage object to be associated with this controller.
     */
    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
    }

    /**
     * This method is used to handle the response from the server.
     * Currently, it is not used in this controller.
     *
     * @param feedback The feedback from the server.
     * @param message  The message from the server.
     * @param eventID  The ID of the event.
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        //Not used
    }

    /**
     * This method is used to reset the GUI.
     * It calls the resetUI method of the view.
     */
    @FXML
    public void resetGui() {
        view.resetUI();
    }
}
