package it.polimi.ingsw.view.gui.controller;


import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


//TODO USE TOGGLE BUTTONS FOR THE READY
public class LobbyController extends FXMLController {

    private List<StackPane> playerStackPanes;

    private List<Text> usernames;

    private List<Button> playerButtons;

    @FXML
    private TextField chatInput;

    @FXML
    private TextArea chatArea;

    @FXML
    private Text chatText;

    @FXML
    private HBox hBox;

    @FXML
    private Text lobbyName;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private StackPane playerStackPane0;

    @FXML
    private StackPane playerStackPane1;

    @FXML
    private StackPane playerStackPane2;

    @FXML
    private StackPane playerStackPane3;

    @FXML
    private Text username0;

    @FXML
    private Text username1;

    @FXML
    private Text username2;

    @FXML
    private Text username3;

    @FXML
    private Button playerButton0;

    @FXML
    private Button playerButton1;

    @FXML
    private Button playerButton2;

    @FXML
    private Button playerButton3;

    public LobbyController(){
        super();
    }

    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        playerStackPanes = Arrays.asList(playerStackPane0, playerStackPane1, playerStackPane2, playerStackPane3);
        usernames = Arrays.asList(username0, username1, username2, username3);
        playerButtons = Arrays.asList(playerButton0, playerButton1, playerButton2, playerButton3);

        setLobbyName(controller.getLobbyId());

        updateLobby();
    }

    private void lobbyLeaderUpdate(){
        int i = 0;
        for(String user : controller.getPlayersStatus().keySet()){
            if(controller.getUsername().equals(user)){
                playerStackPanes.get(i).setVisible(true);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(true);
                playerButtons.get(i).setText("Quit");
                playerButtons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        quitLobby();
                    }
                });
            }
            else{
            playerStackPanes.get(i).setVisible(true);
            usernames.get(i).setText(user);
            playerButtons.get(i).setVisible(true);
            playerButtons.get(i).setText("Kick");
            playerButtons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    kickPlayer(user);
                }
            });
            }
            i++;
        }
        while(i < 4){
            playerStackPanes.get(i).setVisible(false);
            playerButtons.get(i).setVisible(false);
            usernames.get(i).setText("");
            i++;
        }

    }

    private void lobbyUserUpdate(){
        //DEPENDING ON WHAT ORDER THE PLAYER JOINED HE SHOULD HAVE THE OTHER BUTTONS HIDDEN BUT NOT THEIRS
        int i = 0;
        for(String user : controller.getPlayersStatus().keySet()){
            if(controller.getUsername().equals(user)){
                playerStackPanes.get(i).setVisible(true);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(true);
                playerButtons.get(i).setText("Quit");
                playerButtons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        quitLobby();
                    }
                });
            }
            else{
                playerStackPanes.get(i).setVisible(true);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(false);
            }
            i++;
        }
        while( i< 4){
            playerStackPanes.get(i).setVisible(false);
            usernames.get(i).setText("");
            playerButtons.get(i).setVisible(false);
            i++;
        }
    }

    /**
     * Method to set the name of the lobby
     *
     * @param lobbyName the name of the lobby
     */
    public void setLobbyName(String lobbyName) {
        this.lobbyName.setText("LOBBY: " + lobbyName);
    }

    /**
     * Method to dynamically set the information of the player in its box,
     * where he can see his username, an image, and the ready button.
     *
     * @param username the username of the player
     */
    public void setSelfAnchor(String username) {

    }

    /**
     * Method to dynamically set the information of the other players that entered the lobby,
     * visualizing their username, an image, and if they are ready or not.
     * @param username the name of the player.
     */
    public void showPlayerAnchor(String username) {
        if(hBox.getChildren().size() == 1){
            username1.setText(username);

            //if player is ready, show the text ready
            //showReady1.setText("Ready");

            //playerAnchor1.setVisible(true);
            //playerAnchor1.setManaged(true);
        }
        if(hBox.getChildren().size() == 2){
            //playerText2.setText(username);

            //if player is ready, show the text ready
            //showReady2.setText("Ready");

            //playerAnchor2.setVisible(true);
            //playerAnchor2.setManaged(true);
        }
        if(hBox.getChildren().size() == 3){
            //playerText3.setText(username);

            //if player is ready, show the text ready
            //showReady3.setText("Ready");

            //playerAnchor3.setVisible(true);
            //playerAnchor3.setManaged(true);
        }
    }

    //TODO chat implementation

    @FXML
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.UPDATE_LOBBY_PLAYERS:
                //showResponseMessage(message, 1000);
                Platform.runLater(this::updateLobby);
                break;
            case EventID.QUIT_LOBBY:
                if(feedback == Feedback.SUCCESS){
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/EnterLobbiesMenu.fxml"));
                            Parent root = loader.load();
                            EnterLobbiesController nextController = loader.getController();

                            Scene scene = stage.getScene();
                            scene.setRoot(root);
                            transition(nextController);
                        }
                        catch (IOException exception){
                            exception.printStackTrace();
                        }
                    });
                }
                else{
                    System.out.println("Something bad happened, you can't quit!");
                }
                break;
            case EventID.KICKED_PLAYER_FROM_LOBBY:
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/EnterLobbiesMenu.fxml"));
                        Parent root = loader.load();
                        EnterLobbiesController nextController = loader.getController();

                        Scene scene = stage.getScene();
                        scene.setRoot(root);
                        transition(nextController);
                    }
                    catch (IOException exception){
                        exception.printStackTrace();
                    }
                });
        }
        notifyResponse();;
    }

    private void updateLobby(){
        if (controller.isLobbyLeader()) {
            lobbyLeaderUpdate();
        }
        else if (!controller.isLobbyLeader()){
            lobbyUserUpdate();
        }
    }


    private void quitLobby() {
        Event event = new QuitLobbyEvent();
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void kickPlayer(String userToKick) {
        if (controller.getPlayersStatus().size() > 1) {
            Event event = new KickFromLobbyEvent(userToKick);
            controller.newViewEvent(event);
            waitForResponse();
        }
        else{
            System.out.println("Nobody here but us chickens");
        }
    }


    @Override
    public boolean inLobby(){
        return true;
    }
}

