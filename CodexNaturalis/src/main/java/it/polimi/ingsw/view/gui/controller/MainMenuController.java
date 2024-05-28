package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The MainMenuController class is responsible for handling the user interface of the main menu.
 * It extends the FXMLController class.
 */
public class MainMenuController extends FXMLController {

    @FXML private AnchorPane mainMenuFX;

    @FXML private AnchorPane optionsMenuFX;

    @FXML private AnchorPane creditsMenuFX;

    /**
     * Default constructor for MainMenuController.
     */
    public MainMenuController(){
        super();
    }

    /**
     * Initializes the controller with the given view and stage.
     * @param view The view associated with this controller
     * @param stage The stage in which the FXML view is shown
     */
    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
    }

    /**
     * Handles the action of going to the connection choice menu.
     * @throws Exception If there is an error loading the FXML file
     */
    @FXML
    public void goChooseConnection() throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/ChooseConnectionMenu.fxml"));
        Parent root = loader.load();
        ChooseConnectionController nextController = loader.getController();

        Scene scene = stage.getScene();
        scene.setRoot(root);
        transition(nextController);
    }

    /**
     * Handles the action of exiting the application.
     */
    @FXML
    public void exit(){
        Platform.exit();
        System.exit(0);
    }

    /**
     * Handles the action of going to the options' menu.
     */
    @FXML
    public void goOptions(){

        mainMenuFX.setVisible(false);
        mainMenuFX.setManaged(false);
        optionsMenuFX.setVisible(true);
        optionsMenuFX.setManaged(true);

    }

    /**
     * Handles the action of setting the application to fullscreen.
     */
    @FXML
    public void setFullscreen(){
        if(!stage.isFullScreen()){
            //stage.setResizable(false);
            stage.setFullScreen(true);
        }
        else{
            //stage.setResizable(false);
            stage.setFullScreen(false);
        }
    }

    /**
     * Handles the action of going back to the main menu.
     */
    @FXML
    public void goBack(){

        mainMenuFX.setVisible(true);
        mainMenuFX.setManaged(true);
        optionsMenuFX.setVisible(false);
        optionsMenuFX.setManaged(false);
        creditsMenuFX.setVisible(false);
        creditsMenuFX.setManaged(false);

    }

    /**
     * Handles the action of going to the credits' menu.
     */
    @FXML
    public void goCredits(){
        mainMenuFX.setVisible(false);
        mainMenuFX.setManaged(false);
        creditsMenuFX.setManaged(true);
        creditsMenuFX.setVisible(true);
    }

    /**
     * Handles the action of going to the rules' menu.
     * @throws Exception If there is an error loading the FXML file
     */
    @FXML
    public void goRules() throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/Rules.fxml"));
        Parent root = loader.load();
        RulesController controller = loader.getController();

        Scene scene = stage.getScene();
        scene.setRoot(root);

        transition(controller);
    }

    /**
     * Handles the response from the server.
     * This method is not used in this controller.
     * @param feedback The feedback from the server
     * @param message The message associated with the feedback
     * @param eventID The ID of the event
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
    }

    /**
     * Indicates whether the user is in the menu.
     * @return true since the user is in the menu when this controller is active
     */
    @Override
    public boolean inMenu(){
        return true;
    }



}
