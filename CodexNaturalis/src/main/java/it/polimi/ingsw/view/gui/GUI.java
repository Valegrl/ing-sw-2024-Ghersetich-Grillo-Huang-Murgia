package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.view.gui.state.ChooseConnectionState;
import it.polimi.ingsw.view.gui.state.MainMenuState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GUI extends Application implements View {

    private final Scanner in;

    private final PrintStream out;

    private final ExecutorService executor;

    private String username;

    private final ViewController controller;

    private ViewState state;

    public GUI() {
        this.in = new Scanner(System.in);
        this.out = new PrintStream(System.out, true);
        this.controller = new ViewController(this);
        this.state = new MainMenuState(this);
        this.executor = Executors.newCachedThreadPool();
    }

    public void run(){
        launch();
        state.run();
    }

    @Override
    public void start(Stage stage) throws IOException, NullPointerException{
        /*This only has UPPERCASE letters*/
        Font.loadFont(getClass().getResourceAsStream("/font/BebasNeue-Regular.ttf"), 14);
        /*This has both UPPERCASE and LOWERCASE letters, used for forms*/
        Font.loadFont(getClass().getResourceAsStream("/font/BebasNeueNormal-Bold.otf"), 14);

        MainMenuState controller = (MainMenuState) this.state;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/MainMenu.fxml"));
        loader.setController(controller);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/css/main/MainMenu.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setResizable(false);
        stage.setFullScreen(true);

        stage.setScene(scene);

        stage.show();
    }


    public void setState(ViewState state){
        this.state = state;
    }

    @Override
    public void printMessage(String message){
        out.println(message);
    }

    @Override
    public ViewController getController() {
        return controller;
    }

    @Override
    public String getInput(){
        return in.nextLine();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public ViewState getState() {
        return state;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void handleResponse(String eventID, Feedback feedback, String message) {
        executor.execute(() -> state.handleResponse(feedback, message, eventID));
    }

    @Override
    public void stopInputRead(boolean stopInputRead) {

    }

    public void serverDisconnected(){}

    @Override
    public void clearInput() {
    }

}
