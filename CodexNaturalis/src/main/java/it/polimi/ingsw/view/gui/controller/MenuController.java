package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.DeleteAccountEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LogoutEvent;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The MenuController class extends the {@link FXMLController} class and is responsible for handling the user interface and user interactions for the main menu.
 * This includes handling button clicks, displaying error messages, and managing the different views (e.g., profile settings, delete account confirmation).
 * The class also communicates with the ViewController to send and receive events.
 */
public class MenuController extends FXMLController {

    private String username;

    @FXML
    private AnchorPane menuFX;

    @FXML
    private Label errorProfileSettings;

    @FXML
    private AnchorPane profileSettingsMenuFX;

    @FXML
    private VBox profileSettingsVBox;

    @FXML
    private VBox confirmDeleteVBox;

    @FXML
    private Text usernameTextFX;

    /**
     * The run method is called to initialize the view and stage for the controller.
     * It also sets the username text.
     *
     * @param view the view to be set up
     * @param stage the stage to be set up
     */
    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        this.username = view.getUsername();

        usernameTextFX.setText("Hello " + username + " !");
    }

    /**
     * The {@code goReconnectGames} method is called when the reconnect games button is clicked.
     * It loads the ReconnectMenu view and transitions to the ReconnectMenuController.
     */
    @FXML
    public void goReconnectGames() throws IOException{
        try {
            switchScreen("ReconnectMenu");
        }
        catch (IOException exception){
            throw new IOException("Error loading the FXML: trying to load ReconnectMenu");
        }
    }

    /**
     * The {@code goEnterLobbies} method is called when the enter lobbies button is clicked.
     * It loads the EnterLobbiesMenu view and transitions to the EnterLobbiesController.
     */
    @FXML
    public void goEnterLobbies() throws  IOException{
        try {
            switchScreen("EnterLobbiesMenu");
        }
        catch (IOException exception){
            throw new IOException("Error loading the FXML: trying to load EnterLobbiesMenu");
        }
    }

    /**
     * The {@code goProfileSettings} method is called when the profile settings button is clicked.
     * It hides the main menu and shows the profile settings view.
     */
    @FXML
    public void goProfileSettings(){
        errorProfileSettings.setText("");
        menuFX.setManaged(false);
        menuFX.setVisible(false);
        profileSettingsMenuFX.setVisible(true);
        profileSettingsMenuFX.setManaged(true);
    }

    /**
     * The {@code notifyDeleteAccount} method is called when the delete account button is clicked.
     * It hides the profile settings view and shows the delete account confirmation view.
     */
    @FXML
    public void notifyDeleteAccount(){
        profileSettingsVBox.setManaged(false);
        profileSettingsVBox.setVisible(false);
        confirmDeleteVBox.setManaged(true);
        confirmDeleteVBox.setVisible(true);
    }

    /**
     * The {@code confirmDeleteAccount} method is called when the confirm delete account button is clicked.
     * It sends a {@code DeleteAccountEvent} to the server.
     */
    @FXML
    public void confirmDeleteAccount(){
        deleteAccount();
    }

    /**
     * The {@code rejectDeleteAccount} method is called when the reject delete account button is clicked.
     * It hides the delete account confirmation view and shows the profile settings view.
     */
    @FXML
    public void rejectDeleteAccount(){
        errorProfileSettings.setText("Your account was not deleted.");
        profileSettingsVBox.setManaged(true);
        profileSettingsVBox.setVisible(true);
        confirmDeleteVBox.setManaged(false);
        confirmDeleteVBox.setVisible(false);
    }

    /**
     * The logout method is called when the logout button is clicked.
     * It sends a LogoutEvent to the server.
     */
    @FXML
    public void logout(){
        Event event = new LogoutEvent();
        controller.newViewEvent(event);

        waitForResponse();
    }

    /**
     * The {@code deleteAccount} method is used to send a {@link DeleteAccountEvent} to the server.
     * It creates a new DeleteAccountEvent and sends it to the server via the controller.
     * After sending the event, it waits for a response from the server.
     */
    private void deleteAccount() {
        Event event = new DeleteAccountEvent();
        controller.newViewEvent(event);

        waitForResponse();
    }

    /**
     * The exit method is called when the exit button is clicked.
     * It exits the application.
     */
    @FXML
    public void exit(){
        Platform.exit();
        System.exit(0);
    }

    /**
     * The handleResponse method is called to handle the response from the server.
     * It updates the user interface based on the feedback received.
     *
     * @param feedback the feedback received from the server
     * @param message the message received from the server
     * @param eventID the ID of the event that triggered the response
     */
    @FXML
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.DELETE_ACCOUNT:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("LoginMenu");
                        }
                        catch (IOException exception){
                            errorProfileSettings.setText("Account deletion failed " + message);
                            confirmDeleteVBox.setManaged(false);
                            confirmDeleteVBox.setVisible(false);
                            profileSettingsVBox.setManaged(true);
                            profileSettingsVBox.setVisible(true);
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        errorProfileSettings.setText("Account deletion failed " + message);
                        confirmDeleteVBox.setManaged(false);
                        confirmDeleteVBox.setVisible(false);
                        profileSettingsVBox.setManaged(true);
                        profileSettingsVBox.setVisible(true);
                    });
                }
                break;
            case EventID.LOGOUT:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("LoginMenu");
                        }
                        catch (IOException exception){
                            Platform.runLater(() -> errorProfileSettings.setText("Logout failed " + message));
                        }
                    });
                } else {
                    Platform.runLater(() -> errorProfileSettings.setText("Logout failed " + message));
                }
                break;
        }
        notifyResponse();
    }

    /**
     * The {@code goBack} method is called when the back button is clicked on the profile settings view.
     * It hides the profile settings view and shows the main menu.
     */
    @FXML
    public void goBack(){
        menuFX.setManaged(true);
        menuFX.setVisible(true);
        profileSettingsMenuFX.setVisible(false);
        profileSettingsMenuFX.setManaged(false);
    }

    /**
     * The {@code inMenu} method is used to check if the player is in the menu.
     * It returns true as the player is in the menu during the main menu phase.
     *
     * @return true
     */
    @Override
    public boolean inMenu(){
        return  true;
    }

}
