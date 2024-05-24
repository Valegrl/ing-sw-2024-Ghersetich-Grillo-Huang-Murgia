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

public class MainMenuController extends FXMLController {

    @FXML private AnchorPane mainMenuFX;

    @FXML private AnchorPane optionsMenuFX;

    @FXML private AnchorPane creditsMenuFX;


    public MainMenuController(){
        super();
    }

    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
    }

    @FXML
    public void goChooseConnection(ActionEvent e) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/ChooseConnectionMenu.fxml"));
        Parent root = loader.load();
        ChooseConnectionController nextController = loader.getController();

        Scene scene = stage.getScene();
        scene.setRoot(root);
        transition(nextController);
    }

    @FXML
    public void exit(ActionEvent e){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void goOptions(ActionEvent e){

        mainMenuFX.setVisible(false);
        mainMenuFX.setManaged(false);
        optionsMenuFX.setVisible(true);
        optionsMenuFX.setManaged(true);

    }

    @FXML
    public void setFullscreen(ActionEvent e){
        if(!stage.isFullScreen()){
            stage.setResizable(false);
            stage.setFullScreen(true);
        }
        else{
            stage.setResizable(false);
            stage.setFullScreen(false);
        }
    }

    @FXML
    public void goBack(ActionEvent e){

        mainMenuFX.setVisible(true);
        mainMenuFX.setManaged(true);
        optionsMenuFX.setVisible(false);
        optionsMenuFX.setManaged(false);
        creditsMenuFX.setVisible(false);
        creditsMenuFX.setManaged(false);

    }

    @FXML
    public void goCredits(ActionEvent e){
        mainMenuFX.setVisible(false);
        mainMenuFX.setManaged(false);
        creditsMenuFX.setManaged(true);
        creditsMenuFX.setVisible(true);
    }

    @FXML
    public void goRules(ActionEvent e) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/Rules.fxml"));
        Parent root = loader.load();
        RulesController controller = loader.getController();

        Scene scene = stage.getScene();
        scene.setRoot(root);

        transition(controller);
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
    }

    @Override
    public boolean inMenu(){
        return true;
    }



}
