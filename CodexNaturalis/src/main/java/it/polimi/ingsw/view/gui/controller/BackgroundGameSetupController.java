package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
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

    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
    }

    /**
     * This method is responsible for handling the only event of {@code ChooseCardsSetup}, it checks if the {@link it.polimi.ingsw.view.controller.ViewController}
     * setup is not null, in this case it creates the screen for GameSetup and creates the controller for it.
     * @param feedback The feedback from the view
     * @param message The message associated with the feedback
     * @param eventID The ID of the event
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)){
            case EventID.CHOOSE_CARDS_SETUP :
                if(controller.getSetup() != null) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("GameSetup");
                        }
                        catch (IOException exception) {
                            throw new RuntimeException("FXML Exception: failed to load GameSetup", exception);
                        }
                    });
                }
                else{
                    System.out.println("Error, setup not initialized !");
                    QuitGameEvent event = new QuitGameEvent();
                    controller.newViewEvent(event);
                    waitForResponse();
                }
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
        notifyResponse();
    }

    @Override
    public boolean inGame(){
        return true;
    }
}
