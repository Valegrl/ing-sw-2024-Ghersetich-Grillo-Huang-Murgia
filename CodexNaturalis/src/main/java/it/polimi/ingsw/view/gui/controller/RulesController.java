package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RulesController extends FXMLController {
    @FXML
    private Pagination pagination;
    @FXML
    private ImageView imageView;

    private final Image[] images = new Image[6];

    public RulesController(){
        super();
        images[0] = new Image("/images/rules/rules1.png");
        images[1] = new Image("/images/rules/rules2.png");
        images[2] = new Image("/images/rules/rules3.png");
        images[3] = new Image("/images/rules/rules4.png");
        images[4] = new Image("/images/rules/rules5.png");
        images[5] = new Image("/images/rules/rules6.png");
    }



    @FXML
    public void initialize() {
        pagination.setPageFactory(this::createPage);
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) ->
                imageView.setImage(images[newVal.intValue()]));
        pagination.getStylesheets().add(getClass().getResource("/css/rules/Pagination.css").toExternalForm());
    }


    private Node createPage(int pageIndex) {
        VBox box = new VBox();
        Label label = new Label("Page " + (pageIndex + 1));
        box.getChildren().add(label);
        return box;
    }

    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        imageView.setImage(images[0]);
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
        /*
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/MainMenu.fxml"));
            MainMenuController controller = new MainMenuController((GUI) view);
            loader.setController(controller);
            Parent root = loader.load();
            String css = this.getClass().getResource("/css/main/Main.css").toExternalForm();

            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);
            scene.setRoot(root);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
         */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            MainMenuController nextController = loader.getController();

            Scene scene = stage.getScene();
            scene.setRoot(root);

            transition(nextController);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

}
