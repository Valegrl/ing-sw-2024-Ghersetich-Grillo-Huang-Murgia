package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.network.clientSide.ClientManager;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
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


/**
 * The ChooseConnectionController class is responsible for handling the user interface that allows the user to choose the type of connection (RMI or Socket).
 * It extends the FXMLController class.
 */
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
    private TextField ipSocketField;

    @FXML
    private TextField ipRmiField;

    /**
     * Default constructor for ChooseConnectionController.
     */
    public ChooseConnectionController() {
        super();
    }

    /**
     * Initializes the controller with the given view and stage.
     * Sets up key press events for the IP address text fields.
     * @param view The view associated with this controller
     * @param stage The stage in which the FXML view is shown
     */
    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        ipSocketField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                submitSocket(new ActionEvent());
            }
        });

        ipRmiField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                submitRmi(new ActionEvent());
            }
        });

        ipSocketField.setText("127.0.0.1");
        ipRmiField.setText("127.0.0.1");
    }


    /**
     * Sends the submission of the Socket connection form.
     * @param e The action event associated with the form submission.
     */
    @FXML
    public void submitSocket(ActionEvent e) {
        String IpSocket = ipSocketField.getText();

        if (!ClientManager.validateAddress(IpSocket)) {
            errorSocket.setText("Invalid IP address. Please provide a valid one:");
        } else if(IpSocket.isEmpty()){
            errorSocket.setText("Socket address can't be left empty!");
        } else {
            ipSocketField.clear();
            errorSocket.setText("");
            Platform.runLater(() ->{
                try {
                    ClientManager.getInstance().initSocket(IpSocket, 1098);
                    switchScreen("LoginMenu");
                } catch (IOException exception) {
                    errorSocket.setText("Cannot connect with Socket. Make sure the IP provided is valid and try again later...");
                }
            });

        }
    }


    /**
     * Sends the submission of the Socket connection form.
     * @param e The action event associated with the form submission.
     */
    @FXML
    public void submitRmi(ActionEvent e){
        String IpRmi = ipRmiField.getText();

        if (!ClientManager.validateAddress(IpRmi)) {
            errorRmi.setText("Invalid IP address. Please provide a valid one:");
        } else if (IpRmi.isEmpty()) {
            errorRmi.setText("RMI address can't be left empty!");
        } else {
            ipRmiField.clear();
            errorRmi.setText("");
            Platform.runLater(() -> {
                try {
                    ClientManager.getInstance().initRMI(IpRmi);
                    switchScreen("LoginMenu");
                } catch (IOException exception) {
                    errorRmi.setText("Cannot connect with RMI. Make sure the IP provided is valid and try again later...");
                }
            });

        }
    }

    /**
     * Handles the action of going back to the main menu. It loads a new FXML scene, creates the associated
     * FXMLController and transitions to the new scene.
     */
    @FXML
    public void goBackMain(){
        try {
            switchScreen("MainMenu");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the action of going back to the connection choice menu.
     */
    @FXML
    public void goBackConnection(){
        chooseConnectionMenuFX.setVisible(true);
        chooseConnectionMenuFX.setManaged(true);
        socketMenuFX.setVisible(false);
        socketMenuFX.setManaged(false);
        rmiMenuFX.setVisible(false);
        rmiMenuFX.setManaged(false);
    }

    /**
     * Handles the action of choosing the Socket connection type.
     */
    @FXML
    public void setSocket(){
        chooseConnectionMenuFX.setVisible(false);
        chooseConnectionMenuFX.setManaged(false);
        socketMenuFX.setVisible(true);
        socketMenuFX.setManaged(true);
    }

    /**
     * Handles the action of choosing the RMI connection type.
     */
    @FXML
    public void setRmi(){
        chooseConnectionMenuFX.setVisible(false);
        chooseConnectionMenuFX.setManaged(false);
        rmiMenuFX.setVisible(true);
        rmiMenuFX.setManaged(true);
    }

    /**
     * Handles the response from the server.
     * This method is not used in this controller.
     * @param feedback The feedback from the server
     * @param message The message associated with the feedback
     * @param eventID The ID of the event
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        // Not used
    }

    /**
     * Indicates whether the user is in the menu.
     * @return true since the user is in the menu when this controller is active
     */
    @Override
    public boolean inMenu() {
        return true;
    }

}
