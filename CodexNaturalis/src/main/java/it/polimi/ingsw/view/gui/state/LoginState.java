package it.polimi.ingsw.view.gui.state;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LoginEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.RegisterEvent;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginState extends ViewState {

    @FXML
    TextField loginUsernameField;

    @FXML
    PasswordField loginPasswordField;

    @FXML
    TextField registerUsernameField;

    @FXML
    PasswordField registerPasswordField;

    @FXML
    private Label errorRegistration;

    @FXML
    private Label errorLogin;


    public LoginState(View view) {
        super(view);
    }

    @Override
    public void run() {
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
    public void login(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginState/LoginForm.fxml"));
            LoginState controller = this;
            loader.setController(controller);
            Parent root = loader.load();
            String css = this.getClass().getResource("/css/loginState/LoginState.css").toExternalForm();

            loginPasswordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    submitLogin(new ActionEvent());
                }
            });

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
    public void register(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginState/RegisterForm.fxml"));
            LoginState controller = this;
            loader.setController(controller);
            Parent root = loader.load();
            String css = this.getClass().getResource("/css/loginState/LoginState.css").toExternalForm();

            registerPasswordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    submitRegister(new ActionEvent());
                }
            });

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
    public void goBack(ActionEvent e){
        try {
            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginState/LoginState.fxml"));
            LoginState controller = this;
            loader.setController(controller);
            Parent root = loader.load();
            String css = this.getClass().getResource("/css/loginState/LoginState.css").toExternalForm();

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
    public boolean handleInput(int input) {
        return false;
    }

    /*Vital method, don't delete*/
    @Override
    @FXML
    public void handleResponse(Feedback feedback, String message, String eventID) {
        notifyResponse();
        switch (EventID.getByID(eventID)) {
            case LOGIN:
                if (feedback == Feedback.SUCCESS) {
                    showResponseMessage("Login for user '" + view.getUsername() + "' successful", 2000);
                    //TODO IMPLEMENTATION
                    /*transition(new MenuState(view));*/
                } else {
                    Platform.runLater(() -> errorLogin.setText("Error " + message));
                    showResponseMessage("Login failed: " + message, 2000);
                }
                break;
            case REGISTER:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            Stage stage = (Stage) registerUsernameField.getScene().getWindow();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginState/LoginForm.fxml"));
                            LoginState controller = this;
                            loader.setController(controller);
                            Parent root = loader.load();
                            String css = this.getClass().getResource("/css/loginState/LoginState.css").toExternalForm();

                            loginPasswordField.setOnKeyPressed(event -> {
                                if (event.getCode() == KeyCode.ENTER) {
                                    submitLogin(new ActionEvent());
                                }
                            });

                            Scene scene = stage.getScene();
                            scene.getStylesheets().clear();
                            scene.getStylesheets().add(css);

                            scene.setRoot(root);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    });
                    showResponseMessage("Registered user successfully! Now log in to your account.", 1000);

                } else {
                    Platform.runLater(() -> errorRegistration.setText("Error " + message));
                    showResponseMessage("Registration failed: " + message, 2000);
                }
                run();
                break;
        }
    }

    /*Vital methods, dont' delete*/
    private void login(String loginUsername, String loginPassword){
        String user = loginUsername;
        String psw = loginPassword;

        Event event = new LoginEvent(user, psw);
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void register(String registerUsername, String registerPassword){
        String user = registerUsername;
        String psw = registerPassword;

        Event event = new RegisterEvent(user, psw);
        controller.newViewEvent(event);
        waitForResponse();
    }

    @Override
    public boolean inMenu() {
        return true;
    }
}
