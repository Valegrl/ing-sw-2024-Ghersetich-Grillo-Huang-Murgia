package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This class is responsible for setting up the {@link GameSetupController}. It handles the setup from the {@link it.polimi.ingsw.view.controller.ViewController}.
 * It extends the FXMLController class and overrides its methods to provide the specific functionality needed for the live game setup.
 * It doesn't have a screen FXML as it's only a background controller needed for setting up the actual screen for GameSetup.
 */
public class BackgroundGameSetupController extends FXMLController {

    /**
     * Initializes the controller with the given view and stage.
     * @param view The view associated with this controller
     * @param stage The stage in which the FXML view is shown
     */
    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
    }

    /**
     * This method is responsible for handling the events, it loads the {@link it.polimi.ingsw.view.controller.ViewController}
     * setup, then it switches to the actual GameSetup screen
     * @param feedback The feedback from the view
     * @param message The message associated with the feedback
     * @param eventID The ID of the event
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)){
            case EventID.CHOOSE_CARDS_SETUP :
                Platform.runLater(() -> {
                    try {
                        switchScreen("GameSetup");
                    }
                    catch (IOException exception) {
                        throw new RuntimeException("FXML Exception: failed to load GameSetup", exception);
                    }
                });
                break;
            case EventID.QUIT_GAME:
                Platform.runLater(() -> {
                    try {
                        switchScreen("Menu");
                    } catch (IOException exception) {
                        throw new RuntimeException("FXML Exception: failed to load Menu",exception);
                    }
                });
                break;
        }
    }

    /**
     * Indicates whether the user is in the game.
     * @return true since the user is in the game when this controller is active.
     */
    @Override
    public boolean inGame(){
        return true;
    }
}
