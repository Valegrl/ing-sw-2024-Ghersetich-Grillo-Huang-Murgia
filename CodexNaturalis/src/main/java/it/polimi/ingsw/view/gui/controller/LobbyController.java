package it.polimi.ingsw.view.gui.controller;


import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

        if (controller.isLobbyLeader() && controller.getPlayersStatus().size() == 1) {
            lobbyLeaderSetup();
        }
        else if (!controller.isLobbyLeader()){
            lobbyUserSetup();
        }
    }

    private void lobbyLeaderSetup(){
        playerStackPane0.setVisible(true);
        playerButton0.setText("Quit");
        username0.setText(controller.getUsername());
        //TODO add event listener for quitting
    }

    private void lobbyUserSetup(){
        //DEPENDING ON WHAT ORDER THE PLAYER JOINED HE SHOULD HAVE THE OTHER BUTTONS HIDDEN BUT NOT THEIRS
        int i = 0;
        StackPane currPane;
        Text currText;
        Button currButton;

        for(String username : controller.getPlayersStatus().keySet()){
            currPane = playerStackPanes.get(i);
            currText = usernames.get(i);
            currButton = playerButtons.get(i);

            currPane.setVisible(true);
            currText.setText(username);
            currButton.setVisible(false);
            if(controller.getUsername().equals(username)){
                currButton.setVisible(true);
                currButton.setText("Quit");
                //TODO add event listener for quitting

                break;
            }
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

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.UPDATE_LOBBY_PLAYERS:
                //showResponseMessage(message, 1000);
                updateLobby();
                break;
        }
    }

    private void updateLobby(){
        Platform.runLater(() -> {
            StackPane currPane;
            Text currText;
            Button currButton;

            List<String> usernameStrings = new ArrayList<>();
            for (Text username : usernames) {
                if (!username.getText().isEmpty()) {
                    usernameStrings.add(username.getText());
                }
            }
            List<String> actualUsernameStrings = new ArrayList<>(controller.getPlayersStatus().keySet());

            int i = 0;
            if (actualUsernameStrings.size() > usernameStrings.size()) {
                if (controller.isLobbyLeader()) {
                    for (String username : actualUsernameStrings) {
                        currPane = playerStackPanes.get(i);
                        currText = usernames.get(i);
                        currButton = playerButtons.get(i);
                        if (!usernameStrings.contains(username)) {
                            currPane.setVisible(true);
                            currText.setText(username);
                            currButton.setText("Kick");
                            //TODO add event listener for kicking;

                        }
                        i++;
                    }
                } else {
                    for (String username : actualUsernameStrings) {
                        currPane = playerStackPanes.get(i);
                        currText = usernames.get(i);
                        if (!usernameStrings.contains(username)) {
                            currPane.setVisible(true);
                            currText.setText(username);
                        }
                        i++;
                    }
                }

            } else if (actualUsernameStrings.size() < usernameStrings.size()) {

                if(controller.isLobbyLeader()){
                    for (String username : usernameStrings){
                        currPane = playerStackPanes.get(i);
                        currText = usernames.get(i);
                        currButton = playerButtons.get(i);
                        if(username.equals(controller.getUsername())){
                            currPane.setVisible(true);
                            currText.setText(username);
                            currButton.setVisible(true);
                            currButton.setText("Quit");
                            //TODO add event listener for quitting
                        }
                        else{
                            if(actualUsernameStrings.contains(username)) {
                                currPane.setVisible(true);
                                currText.setText(username);
                                currButton.setVisible(true);
                                currButton.setText("Kick");
                                //TODO add event listener for kicking;
                            }
                            else{
                                currPane.setVisible(false);
                                currText.setText("");
                            }
                        }
                        i++;
                    }
                }
                else{
                    for(String username : usernameStrings){
                        currPane = playerStackPanes.get(i);
                        currText = usernames.get(i);
                        currButton = playerButtons.get(i);
                        if(username.equals(controller.getUsername())){
                            currPane.setVisible(true);
                            currText.setText(username);
                            currButton.setVisible(true);
                            currButton.setText("Quit");
                            //TODO add listener for quitting
                        }
                        else{
                            if(actualUsernameStrings.contains(username)){
                                currPane.setVisible(true);
                                currText.setText(username);
                                currButton.setVisible(false);
                            }
                            else{
                                currPane.setVisible(false);
                                currText.setText("");
                            }
                        }
                        i++;
                    }
                }
            }
        });
    }


    @Override
    public boolean inLobby(){
        return true;
    }
}

