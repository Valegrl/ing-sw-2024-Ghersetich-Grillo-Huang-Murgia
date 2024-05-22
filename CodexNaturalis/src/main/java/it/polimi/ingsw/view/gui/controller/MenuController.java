package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.CreateLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.DeleteAccountEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LogoutEvent;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController extends FXMLController {
    //TODO Add Icon top-right of the GUI, DEBUG!

    private String username;

    @FXML
    private AnchorPane menuFX;

    @FXML
    private TextField lobbyNameField;

    @FXML
    private TextField numPlayersField;

    @FXML
    private Label errorLobbyCreate;

    @FXML
    private Label errorProfileSettings;

    @FXML
    private AnchorPane lobbyCreateMenuFX;

    @FXML
    private AnchorPane reconnectGamesMenuFX;

    @FXML
    private AnchorPane profileSettingsMenuFX;

    @FXML
    private VBox profileSettingsVBox;

    @FXML
    private VBox confirmDeleteVBox;

    @FXML
    private Text usernameTextFX;


    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        this.username = view.getUsername();

        usernameTextFX.setText("Hello " + username + " !");

        numPlayersField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitCreateLobby(new ActionEvent());
            }
        });

    }

    @FXML
    public void goCreateLobby(){
        errorLobbyCreate.setText("");
        menuFX.setManaged(false);
        menuFX.setVisible(false);
        lobbyCreateMenuFX.setVisible(true);
        lobbyCreateMenuFX.setManaged(true);
    }

    @FXML
    public void goReconnectGames(){

    }

    @FXML
    public void goAvailableLobbies(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AvailableLobbiesMenu.fxml"));
            Parent root = loader.load();
            AvailableLobbiesController nextController = loader.getController();

            Scene scene = stage.getScene();
            scene.setRoot(root);
            transition(nextController);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void refreshAvailableLobbies(){

    }

    @FXML
    public void goProfileSettings(){
        errorProfileSettings.setText("");
        menuFX.setManaged(false);
        menuFX.setVisible(false);
        profileSettingsMenuFX.setVisible(true);
        profileSettingsMenuFX.setManaged(true);
    }

    //TODO Implement the deleteAccount in another controller ?

    @FXML
    public void notifyDeleteAccount(){
        profileSettingsVBox.setManaged(false);
        profileSettingsVBox.setVisible(false);
        confirmDeleteVBox.setManaged(true);
        confirmDeleteVBox.setVisible(true);
    }

    @FXML
    public void confirmDeleteAccount(){
        deleteAccount();
    }

    @FXML
    public void rejectDeleteAccount(){
        showResponseMessage("Your account was NOT deleted.", 1000);
        errorProfileSettings.setText("Your account was not deleted.");
        profileSettingsVBox.setManaged(true);
        profileSettingsVBox.setVisible(true);
        confirmDeleteVBox.setManaged(false);
        confirmDeleteVBox.setVisible(false);
    }

    @FXML
    public void logout(){
        Event event = new LogoutEvent();
        controller.newViewEvent(event);

        waitForResponse();
    }

    private void deleteAccount() {
        Event event = new DeleteAccountEvent();
        controller.newViewEvent(event);

        waitForResponse();
    }

    @FXML
    public void exit(){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.CREATE_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    //showResponseMessage(message, 2000);
                    //transition(new LobbyState(view));
                } else {
                    Platform.runLater(() -> errorLobbyCreate.setText("Lobby creation failed " + message));
                    //showResponseMessage("Lobby creation failed " + message, 2000);
                }
                break;
            case EventID.DELETE_ACCOUNT:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginMenu.fxml"));
                            Parent root = loader.load();
                            LoginController nextController = loader.getController();

                            Scene scene = stage.getScene();
                            scene.setRoot(root);
                            transition(nextController);
                        }
                        catch (IOException exception){
                            errorProfileSettings.setText("Account deletion failed " + message);
                            confirmDeleteVBox.setManaged(false);
                            confirmDeleteVBox.setVisible(false);
                            profileSettingsVBox.setManaged(true);
                            profileSettingsVBox.setVisible(true);
                            //showResponseMessage("Account deletion failed: " + message, 2000);
                        }
                    });
                    //showResponseMessage("Account deleted successfully!", 1500);
                } else {
                    Platform.runLater(() -> {
                        errorProfileSettings.setText("Account deletion failed " + message);
                        confirmDeleteVBox.setManaged(false);
                        confirmDeleteVBox.setVisible(false);
                        profileSettingsVBox.setManaged(true);
                        profileSettingsVBox.setVisible(true);
                    });
                    //showResponseMessage("Account deletion failed: " + message, 2000);
                }

                break;
            case EventID.LOGOUT:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginMenu.fxml"));
                            Parent root = loader.load();
                            LoginController nextController = loader.getController();

                            Scene scene = stage.getScene();
                            scene.setRoot(root);
                            transition(nextController);
                        }
                        catch (IOException exception){
                            Platform.runLater(() -> errorProfileSettings.setText("Logout failed " + message));
                            //showResponseMessage("Logout failed: " + message, 2000);
                        }
                    });
                    //showResponseMessage(message, 1500);
                } else {
                    Platform.runLater(() -> errorProfileSettings.setText("Logout failed " + message));
                    //showResponseMessage("Logout failed: " + message, 2000);
                }
                break;

            /*case EventID.AVAILABLE_LOBBIES:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 0);
                } else {
                    showResponseMessage("Failed to get available lobbies from server: " + message, 2000);
                    run();
                }
                break;
            case EventID.JOIN_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 2000);
                    transition(new LobbyState(view));
                } else {
                    showResponseMessage("Failed to join lobby: " + message, 2000);
                    availableLobbies();
                }
                break;
            case EventID.GET_MY_OFFLINE_GAMES:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 800);
                } else {
                    showResponseMessage("Failed to get offline games: " + message, 2000);
                    run();
                }
                break;
            case EventID.RECONNECT_TO_GAME:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage(message, 2000);
                    // TODO Reconnect to game status
                } else {
                    showResponseMessage("Failed to reconnect to game: " + message, 2000);
                    reconnect();
                }
                break;
             */

        }
        notifyResponse();
    }

    @FXML
    public void goBack(){
        menuFX.setManaged(true);
        menuFX.setVisible(true);
        lobbyCreateMenuFX.setVisible(false);
        lobbyCreateMenuFX.setManaged(false);
        reconnectGamesMenuFX.setVisible(false);
        reconnectGamesMenuFX.setManaged(false);
        profileSettingsMenuFX.setVisible(false);
        profileSettingsMenuFX.setManaged(false);
    }

    @FXML
    private void submitCreateLobby(ActionEvent e) {
        String lobbyName = lobbyNameField.getText();
        try {
            int numPlayers = Integer.parseInt(numPlayersField.getText());
            if(numPlayers < 2 || numPlayers > 4){
                errorLobbyCreate.setText("Number of players must be between 2 and 4.");
            }
            else {
                lobbyNameField.clear();
                numPlayersField.clear();
                errorLobbyCreate.setText("");

                Event event = new CreateLobbyEvent(lobbyName, numPlayers);
                controller.newViewEvent(event);
                waitForResponse();
            }
        }
        catch (NumberFormatException exception){
            errorLobbyCreate.setText("Invalid. Please put a number.");
        }
    }

    @Override
    public boolean inMenu(){
        return  true;
    }

}
