package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LoginEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.RegisterEvent;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController extends FXMLController {

    @FXML
    private AnchorPane loginMenuFX;

    @FXML
    private AnchorPane loginSubmitMenuFX;

    @FXML
    private AnchorPane registerSubmitMenuFX;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private Label errorRegistration;

    @FXML
    private Label errorLogin;


    public LoginController() {
        super();
    }

    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        loginPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitLogin(new ActionEvent());
            }
        });

        registerPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitRegister(new ActionEvent());
            }
        });
    }

    @FXML
    public void submitRegister(ActionEvent e){
        String registerUsername = registerUsernameField.getText();
        String registerPassword = registerPasswordField.getText();

        if(registerUsername.isEmpty() || registerPassword.isEmpty()){
            errorRegistration.setText("Username and password fields can't be left empty!");
        }
        else{
            registerUsernameField.clear();
            registerPasswordField.clear();
            errorRegistration.setText("");
            register(registerUsername, registerPassword);
        }
    }

    @FXML
    public void submitLogin(ActionEvent e){
        String loginUsername = loginUsernameField.getText();
        String loginPassword = loginPasswordField.getText();

        if(loginUsername.isEmpty() || loginPassword.isEmpty()){
            errorLogin.setText("Username and password fields can't be left empty!");
        }
        else{
            loginUsernameField.clear();
            loginPasswordField.clear();
            errorLogin.setText("");
            login(loginUsername, loginPassword);
        }
    }

    @FXML
    public void goLogin(ActionEvent e){

        loginMenuFX.setVisible(false);
        loginMenuFX.setManaged(false);
        loginSubmitMenuFX.setVisible(true);
        loginSubmitMenuFX.setManaged(true);
    }

    @FXML
    public void goRegister(ActionEvent e){

        loginMenuFX.setVisible(false);
        loginMenuFX.setManaged(false);
        registerSubmitMenuFX.setVisible(true);
        registerSubmitMenuFX.setManaged(true);
    }

    @FXML
    public void goBack(ActionEvent e){

        loginUsernameField.clear();
        loginPasswordField.clear();
        registerUsernameField.clear();
        registerPasswordField.clear();
        errorRegistration.setText("");
        errorLogin.setText("");

        loginMenuFX.setVisible(true);
        loginMenuFX.setManaged(true);
        loginSubmitMenuFX.setVisible(false);
        loginSubmitMenuFX.setManaged(false);
        registerSubmitMenuFX.setVisible(false);
        registerSubmitMenuFX.setManaged(false);
    }


    @Override
    @FXML
    public void handleResponse(Feedback feedback, String message, String eventID) {
        notifyResponse();
        switch (EventID.getByID(eventID)) {
            case LOGIN:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/Menu.fxml"));
                            Parent root = loader.load();
                            MenuController nextController = loader.getController();

                            Scene scene = stage.getScene();
                            scene.setRoot(root);
                            transition(nextController);
                            //showResponseMessage("Login for user '" + view.getUsername() + "' successful", 2000);
                        }
                        catch (IOException exception){
                            errorLogin.setText("Error " + exception);
                        }
                    });
                } else {
                     Platform.runLater(() -> errorLogin.setText("Error " + message));
                    //showResponseMessage("Login failed: " + message, 2000);
                }
                break;
            case REGISTER:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        registerSubmitMenuFX.setManaged(false);
                        registerSubmitMenuFX.setVisible(false);
                        loginSubmitMenuFX.setVisible(true);
                        loginSubmitMenuFX.setManaged(true);
                    });
                    //showResponseMessage("Registered user successfully! Now log in to your account.", 1000);

                } else {
                    Platform.runLater(() -> errorRegistration.setText("Error " + message));
                    //showResponseMessage("Registration failed: " + message, 2000);
                }
                break;
        }
    }

    /*Vital methods, dont' delete*/
    private void login(String loginUsername, String loginPassword){
        Event event = new LoginEvent(loginUsername, loginPassword);
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void register(String registerUsername, String registerPassword){
        Event event = new RegisterEvent(registerUsername, registerPassword);
        controller.newViewEvent(event);
        waitForResponse();
    }

    @Override
    public boolean inMenu() {
        return true;
    }

}
