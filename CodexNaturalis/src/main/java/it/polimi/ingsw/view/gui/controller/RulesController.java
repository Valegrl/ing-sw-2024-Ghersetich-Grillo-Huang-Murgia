package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RulesController extends FXMLController{

    @FXML
    private Button backButton;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private Pagination pagination;

    @FXML
    private VBox vBox;

    private final Image[] images = new Image[6];

    @FXML
    public void initialize() {
        imageView.fitWidthProperty().bind(vBox.widthProperty());
        imageView.fitHeightProperty().bind(vBox.heightProperty());
    }

    public RulesController(){
        super();
        images[0] = new Image("/it/polimi/ingsw/images/rules/rules1.png");
        images[1] = new Image("/it/polimi/ingsw/images/rules/rules2.png");
        images[2] = new Image("/it/polimi/ingsw/images/rules/rules3.png");
        images[3] = new Image("/it/polimi/ingsw/images/rules/rules4.png");
        images[4] = new Image("/it/polimi/ingsw/images/rules/rules5.png");
        images[5] = new Image("/it/polimi/ingsw/images/rules/rules6.png");
    }

    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        imageView.setImage(images[0]);
        pagination.setPageFactory(this::createPage);
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) ->
                imageView.setImage(images[newVal.intValue()]));
    }

    private Node createPage(int pageIndex) {
        VBox box = new VBox();
        return box;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        // Not used
    }


    @Override
    public boolean inMenu() {
        return true;
    }

    @FXML
    public void goBackMain(ActionEvent e){
        try {
            switchScreen("MainMenu");
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }
}

