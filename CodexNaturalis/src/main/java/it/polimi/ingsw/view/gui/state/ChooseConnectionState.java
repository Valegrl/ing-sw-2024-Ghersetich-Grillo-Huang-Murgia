package it.polimi.ingsw.view.gui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;

public class ChooseConnectionState extends ViewState {

    @FXML
    private Label errorRmi;

    @FXML
    private Label errorSocket;

    @FXML
    private TextField IpSocketField;

    @FXML
    private TextField IpRmiField;

    public ChooseConnectionState(View view) {
        super(view);
    }

    @FXML
    public void submitSocket(ActionEvent e){
        String IpSocket = IpSocketField.getText();

        if(IpSocket.isEmpty()){
            errorSocket.setText("Socket address can't be left empty!");
        }
        else{
            IpSocketField.clear();
            errorSocket.setText("");
            try {
                ClientManager.getInstance().initSocket(IpSocket, 1098);

                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginState/LoginState.fxml"));
                LoginState controller = new LoginState(view);
                loader.setController(controller);
                Parent root = loader.load();
                String css = this.getClass().getResource("/css/loginState/LoginState.css").toExternalForm();

                Scene scene = stage.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(css);
                scene.setRoot(root);

                transition(controller);

            } catch (Exception exception) {
                errorSocket.setText("Cannot connect with Socket. Make sure the IP provided is valid an try again later...");
            }
        }
    }

    @FXML
    public void submitRmi(ActionEvent e){
        String IpRmi = IpRmiField.getText();

        if(IpRmi.isEmpty()){
            errorRmi.setText("RMI address can't be left empty!");
        }
        else{
            IpRmiField.clear();
            errorRmi.setText("");
            try {
                ClientManager.getInstance().initRMI(IpRmi);

                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginState/LoginState.fxml"));
                LoginState controller = new LoginState(view);
                loader.setController(controller);
                Parent root = loader.load();
                String css = this.getClass().getResource("/css/loginState/LoginState.css").toExternalForm();

                Scene scene = stage.getScene();
                scene.getStylesheets().clear();
                scene.getStylesheets().add(css);
                scene.setRoot(root);

                transition(controller);

            } catch (Exception exception) {
                errorRmi.setText("Cannot connect with RMI. Make sure the IP provided is valid an try again later...");
            }
        }

    }

    @FXML
    public void goBackMain(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/MainMenu.fxml"));
            MainMenuState controller = new MainMenuState((GUI) view);
            loader.setController(controller);
            Parent root = loader.load();
            String css = this.getClass().getResource("/css/main/MainMenu.css").toExternalForm();

            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);
            scene.setRoot(root);

            transition(controller);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void goBackConnection(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseConnectionState/ChooseConnectionState.fxml"));
            ChooseConnectionState controller = this;
            loader.setController(controller);
            Parent root = loader.load();
            String css = this.getClass().getResource("/css/chooseConnectionState/ChooseConnectionState.css").toExternalForm();

            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);
            scene.setRoot(root);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void setSocket(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseConnectionState/SocketForm.fxml"));
            ChooseConnectionState controller = this;
            loader.setController(controller);
            Parent root = loader.load();
            String css = this.getClass().getResource("/css/chooseConnectionState/ChooseConnectionState.css").toExternalForm();

            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);
            scene.setRoot(root);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void setRmi(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseConnectionState/RmiForm.fxml"));
            ChooseConnectionState controller = this;
            loader.setController(controller);
            Parent root = loader.load();
            String css = this.getClass().getResource("/css/chooseConnectionState/ChooseConnectionState.css").toExternalForm();

            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);

            scene.setRoot(root);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }


    @Override
    public void run() {
    }

    @Override
    public boolean handleInput(int input) {
        return false;
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        // Not used
    }


    @Override
    public boolean inMenu() {
        return true;
    }
}
