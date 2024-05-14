package it.polimi.ingsw.view.gui.state;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    @FXML TextField IpRmiField;

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
                stage.getScene().setRoot(root);

                String css = this.getClass().getResource("/css/loginState/LoginState.css").toExternalForm();
                stage.getScene().getStylesheets().clear();
                stage.getScene().getStylesheets().add(css);

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
                stage.getScene().setRoot(root);

                String css = this.getClass().getResource("/css/loginState/LoginState.css").toExternalForm();
                stage.getScene().getStylesheets().clear();
                stage.getScene().getStylesheets().add(css);

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
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/Main.fxml"));
            String css = this.getClass().getResource("/css/main/Main.css").toExternalForm();

            stage.getScene().getStylesheets().clear();
            stage.getScene().getStylesheets().add(css);

            stage.getScene().setRoot(root);
            stage.show();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void goBackConnection(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();

            ChooseConnectionState controller = new ChooseConnectionState((GUI) view);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseConnectionState/ChooseConnectionState.fxml"));
            loader.setController(controller);
            Parent root = loader.load();

            String css = this.getClass().getResource("/css/chooseConnectionState/ChooseConnectionState.css").toExternalForm();

            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);

            stage.getScene().setRoot(root);
            stage.show();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void setSocket(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();

            ChooseConnectionState controller = this;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseConnectionState/SocketForm.fxml"));
            loader.setController(controller);
            Parent root = loader.load();

            String css = this.getClass().getResource("/css/chooseConnectionState/ChooseConnectionState.css").toExternalForm();

            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);

            stage.getScene().setRoot(root);
            stage.show();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void setRmi(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = stage.getScene();

            ChooseConnectionState controller = this;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseConnectionState/RmiForm.fxml"));
            loader.setController(controller);
            Parent root = loader.load();

            String css = this.getClass().getResource("/css/chooseConnectionState/ChooseConnectionState.css").toExternalForm();

            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);

            stage.getScene().setRoot(root);
            stage.show();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public ChooseConnectionState(GUI view) {
        super(view);
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
