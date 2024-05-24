package it.polimi.ingsw.view.gui.controller;


import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class InGameController extends FXMLController {


    @FXML
    private Label aboveText;


    @FXML
    private Label animalOcc1;


    @FXML
    private Label animalOcc11;


    @FXML
    private Label animalOcc111;


    @FXML
    private Label animalOcc112;


    @FXML
    private Label animalOcc113;


    @FXML
    private Label animalOcc2;


    @FXML
    private Label animalOcc3;


    @FXML
    private Label animalOcc4;


    @FXML
    private BorderPane borderPane;


    @FXML
    private ComboBox<?> chatChoice;


    @FXML
    private TextField chatMessage;


    @FXML
    private TextArea chatText;


    @FXML
    private Text chatTitle;


    @FXML
    private ImageView common1;


    @FXML
    private ImageView common2;


    @FXML
    private AnchorPane commonAnchor;


    @FXML
    private Label commonLabel;


    @FXML
    private Label fungiOcc1;


    @FXML
    private Label fungiOcc2;


    @FXML
    private Label fungiOcc3;


    @FXML
    private Label fungiOcc4;


    @FXML
    private ImageView gold1;


    @FXML
    private ImageView gold2;


    @FXML
    private AnchorPane goldAnchor;


    @FXML
    private ImageView goldDeck;


    @FXML
    private Label goldLabel;


    @FXML
    private HBox hBoxHand;


    @FXML
    private ImageView hand1;


    @FXML
    private ImageView hand2;


    @FXML
    private ImageView hand3;


    @FXML
    private AnchorPane handAnchor;


    @FXML
    private Label handLabel;


    @FXML
    private Label inkwellOcc1;


    @FXML
    private Label inkwellOcc2;


    @FXML
    private Label inkwellOcc3;


    @FXML
    private Label inkwellOcc4;


    @FXML
    private Label insectOcc1;


    @FXML
    private Label insectOcc2;


    @FXML
    private Label insectOcc3;


    @FXML
    private Label insectOcc4;


    @FXML
    private AnchorPane leftAnchor;


    @FXML
    private ListView<?> listView;


    @FXML
    private Label lobbyName;


    @FXML
    private AnchorPane lowerAnchor;


    @FXML
    private AnchorPane mainAnchor;


    @FXML
    private Label manuscriptOcc1;


    @FXML
    private Label manuscriptOcc2;


    @FXML
    private Label manuscriptOcc3;


    @FXML
    private Label manuscriptOcc4;


    @FXML
    private Pane paneAnimal1;


    @FXML
    private Pane paneAnimal2;


    @FXML
    private Pane paneAnimal3;


    @FXML
    private Pane paneAnimal4;


    @FXML
    private Pane paneFungi1;


    @FXML
    private Pane paneFungi2;


    @FXML
    private Pane paneFungi3;


    @FXML
    private Pane paneFungi4;


    @FXML
    private Pane paneInkwell1;


    @FXML
    private Pane paneInkwell2;


    @FXML
    private Pane paneInkwell3;


    @FXML
    private Pane paneInkwell4;


    @FXML
    private Pane paneInsect1;


    @FXML
    private Pane paneInsect2;


    @FXML
    private Pane paneInsect3;


    @FXML
    private Pane paneInsect4;


    @FXML
    private Pane paneManuscript1;


    @FXML
    private Pane paneManuscript2;


    @FXML
    private Pane paneManuscript3;


    @FXML
    private Pane paneManuscript4;


    @FXML
    private Pane panePlant1;


    @FXML
    private Pane panePlant2;


    @FXML
    private Pane panePlant3;


    @FXML
    private Pane panePlant4;


    @FXML
    private Pane panePoints1;


    @FXML
    private Pane panePoints2;


    @FXML
    private Pane panePoints3;


    @FXML
    private Pane panePoints4;


    @FXML
    private Pane paneQuill1;


    @FXML
    private Pane paneQuill2;


    @FXML
    private Pane paneQuill3;


    @FXML
    private Pane paneQuill4;


    @FXML
    private Label plantOcc1;


    @FXML
    private Label plantOcc2;


    @FXML
    private Label plantOcc3;


    @FXML
    private Label plantOcc4;


    @FXML
    private ComboBox<?> playAreaChoice;


    @FXML
    private Label quillOcc1;


    @FXML
    private Label quillOcc2;


    @FXML
    private Label quillOcc3;


    @FXML
    private Label quillOcc4;


    @FXML
    private ImageView resource1;


    @FXML
    private ImageView resource2;


    @FXML
    private AnchorPane resourceAnchor;


    @FXML
    private ImageView resourceDeck;


    @FXML
    private Label resourceLabel;


    @FXML
    private AnchorPane rightAnchor;


    @FXML
    private ScrollPane scrollPane;


    @FXML
    private ImageView secret;


    @FXML
    private AnchorPane secretAnchor;


    @FXML
    private Label secretLabel;


    @FXML
    private Button sendButton;


    @FXML
    private AnchorPane uncovered1;


    @FXML
    private AnchorPane uncovered2;


    @FXML
    private AnchorPane uncovered3;


    @FXML
    private AnchorPane uncovered4;


    @FXML
    private Text uncoveredUsername1;


    @FXML
    private Text uncoveredUsername2;


    @FXML
    private Text uncoveredUsername3;


    @FXML
    private Text uncoveredUsername4;


    @FXML
    private AnchorPane upperAnchor;


    @FXML
    private VBox vBox;


    @FXML
    private VBox vBoxGold;


    @FXML
    private VBox vBoxResource;




    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
    }
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
    }


    @Override
    public boolean inGame(){
        return true;
    }


    public void setLobbyName(String name) {
        this.lobbyName.setText(name);
    }

}
