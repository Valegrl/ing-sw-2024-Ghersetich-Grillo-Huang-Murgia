package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;


public class ChooseConnectionController extends FXMLController {

    @FXML
    private AnchorPane chooseConnectionMenuFX;

    @FXML
    private AnchorPane rmiMenuFX;

    @FXML
    private AnchorPane socketMenuFX;

    @FXML
    private Label errorRmi;

    @FXML
    private Label errorSocket;

    @FXML
    private TextField IpSocketField;

    @FXML
    private TextField IpRmiField;


    public ChooseConnectionController() {
        super();
    }


    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        IpSocketField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                submitSocket(new ActionEvent());
            }
        });

        IpRmiField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                submitRmi(new ActionEvent());
            }
        });
    }

    @FXML
    public void submitSocket(ActionEvent e) {
        String IpSocket = IpSocketField.getText();
        System.out.println(IpSocket);

        if(IpSocket.isEmpty()){
            errorSocket.setText("Socket address can't be left empty!");
        }
        else{
            IpSocketField.clear();
            errorSocket.setText("");
            try {
                ClientManager.getInstance().initSocket(IpSocket, 1098);
                /*Unhandled RemoteServer Exception if server is off ?*/
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginMenu.fxml"));
                Parent root = loader.load();
                LoginController nextController = loader.getController();

                Scene scene = stage.getScene();
                scene.setRoot(root);
                transition(nextController);

            } catch (RuntimeException | IOException exception ) {
                errorSocket.setText("Cannot connect with Socket. Make sure the IP provided is valid and try again later...");
            }
        }
    }

    @FXML
    public void submitRmi(ActionEvent e){
        String IpRmi = IpRmiField.getText();
        System.out.println(IpRmi);

        if(IpRmi.isEmpty()){
            errorRmi.setText("RMI address can't be left empty!");
        }
        else{
            IpRmiField.clear();
            errorRmi.setText("");
            try {
                ClientManager.getInstance().initRMI(IpRmi);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginMenu.fxml"));
                Parent root = loader.load();
                LoginController nextController = loader.getController();

                Scene scene = stage.getScene();
                scene.setRoot(root);
                transition(nextController);

            } catch (RuntimeException | IOException exception) {
                errorRmi.setText("Cannot connect with RMI. Make sure the IP provided is valid and try again later...");
            }
        }
    }

    @FXML
    public void goBackMain(ActionEvent e){
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

    @FXML
    public void goBackConnection(ActionEvent e){

        chooseConnectionMenuFX.setVisible(true);
        chooseConnectionMenuFX.setManaged(true);
        socketMenuFX.setVisible(false);
        socketMenuFX.setManaged(false);
        rmiMenuFX.setVisible(false);
        rmiMenuFX.setManaged(false);
    }

    @FXML
    public void setSocket(ActionEvent e){
        chooseConnectionMenuFX.setVisible(false);
        chooseConnectionMenuFX.setManaged(false);
        socketMenuFX.setVisible(true);
        socketMenuFX.setManaged(true);
    }

    @FXML
    public void setRmi(ActionEvent e){

        chooseConnectionMenuFX.setVisible(false);
        chooseConnectionMenuFX.setManaged(false);
        rmiMenuFX.setVisible(true);
        rmiMenuFX.setManaged(true);
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
