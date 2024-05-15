package it.polimi.ingsw.view.gui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuState extends ViewState {


    public MainMenuState(GUI view){
        super(view);
    }

    public void run(){
        /*Code if stage can be passed between states*/
    }

    @FXML
    public void goChooseConnection(ActionEvent e) throws Exception {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();

        /*Can't use the FXML standard loader as it requires a parameter-less constructor!*/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseConnectionState/ChooseConnectionState.fxml"));
        ChooseConnectionState controller = new ChooseConnectionState(view);
        loader.setController(controller);

        Parent root = loader.load();
        String css = this.getClass().getResource("/css/chooseConnectionState/ChooseConnectionState.css").toExternalForm();

        Scene scene = stage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        scene.setRoot(root);

        transition(controller);
    }

    @FXML
    public void exit(ActionEvent e){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void goOptions(ActionEvent e) throws Exception{
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/Options.fxml"));
        MainMenuState controller = this;
        loader.setController(controller);
        Parent root = loader.load();
        String css = this.getClass().getResource("/css/main/Options.css").toExternalForm();

        Scene scene = stage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        scene.setRoot(root);
    }

    @FXML
    public void setFullscreen(ActionEvent e){
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
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
    public void goBack(ActionEvent e) throws Exception{
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/MainMenu.fxml"));
        MainMenuState controller = this;
        loader.setController(controller);
        Parent root = loader.load();
        String css = this.getClass().getResource("/css/main/MainMenu.css").toExternalForm();

        Scene scene = stage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        scene.setRoot(root);
    }

    @FXML
    public void goCredits(ActionEvent e) throws Exception{
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();

        MainMenuState controller = this;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/Credits.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        String css = this.getClass().getResource("/css/main/Credits.css").toExternalForm();

        Scene scene = stage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        scene.setRoot(root);
    }

    @Override
    public boolean handleInput(int input) {
        return false;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
    }

    @Override
    public boolean inMenu(){
        return true;
    }
}
