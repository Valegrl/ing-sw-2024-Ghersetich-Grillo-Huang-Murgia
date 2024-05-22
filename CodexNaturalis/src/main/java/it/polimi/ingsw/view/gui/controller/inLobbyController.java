package it.polimi.ingsw.view.gui.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


public class inLobbyController {


    @FXML
    private TextField chatInput;


    @FXML
    private TextArea chatSpace;


    @FXML
    private Text chatText;


    @FXML
    private HBox hBox;


    @FXML
    private Text lobbyText;


    @FXML
    private AnchorPane mainAnchor;


    @FXML
    private AnchorPane playerAnchor1;


    @FXML
    private AnchorPane playerAnchor2;


    @FXML
    private AnchorPane playerAnchor3;


    @FXML
    private ImageView playerImage1;


    @FXML
    private ImageView playerImage2;


    @FXML
    private ImageView playerImage3;


    @FXML
    private Text playerText1;


    @FXML
    private Text playerText2;


    @FXML
    private Text playerText3;


    @FXML
    private Button readyButton;


    @FXML
    private AnchorPane selfAnchor;


    @FXML
    private ImageView selfImage;


    @FXML
    private Text selfText;


    @FXML
    private Button showReady1;


    @FXML
    private Button showReady2;


    @FXML
    private Button showReady3;



/*
    public void initialize(URL url, ResourceBundle resourceBundle) {


        String cssPath = "/src/main/resources/css/Lobby/inLobby.css";
        mainAnchor.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
    }
    */


    /**
     * Method to set the name of the lobby
     *
     * @param lobbyName the name of the lobby
     */


    public void setLobbyName(String lobbyName) {
        lobbyText.setText("LOBBY: " + lobbyName);
    }


    /**
     * Method to dynamically set the information of the player in its box,
     * where he can see his username, an image, and the ready button.
     *
     * @param username the username of the player
     */
    public void setSelfAnchor(String username) {
        selfText.setText(username);
    }


    /**
     * Method to dynamically set the information of the other players that entered the lobby,
     * visualizing their username, an image, and if they are ready or not.
     * @param username the name of the player.
     */
    public void showPlayerAnchor(String username) {
        if(hBox.getChildren().size() == 1){
            playerText1.setText(username);


            //if player is ready, show the text ready
            //showReady1.setText("Ready");


            playerAnchor1.setVisible(true);
            playerAnchor1.setManaged(true);
        }
        if(hBox.getChildren().size() == 2){
            playerText2.setText(username);


            //if player is ready, show the text ready
            //showReady2.setText("Ready");


            playerAnchor2.setVisible(true);
            playerAnchor2.setManaged(true);
        }
        if(hBox.getChildren().size() == 3){
            playerText3.setText(username);


            //if player is ready, show the text ready
            //showReady3.setText("Ready");


            playerAnchor3.setVisible(true);
            playerAnchor3.setManaged(true);
        }
    }


    //TODO chat implementation


}

