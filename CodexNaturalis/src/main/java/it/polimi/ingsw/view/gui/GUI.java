package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.view.gui.state.ChooseConnectionState;
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
    public void serverDisconnected(){}

    @Override
    public void clearInput() {

    }

    private final ViewController controller;

    private ViewState state;

    public GUI() {
        this.in = new Scanner(System.in);
        this.out = new PrintStream(System.out, true);
        this.controller = new ViewController(this);
        this.state = new ChooseConnectionState(this);
        this.executor = Executors.newCachedThreadPool();
    }

    public void run(){
        try {
            launch();
            state.run();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage stage) throws IOException, NullPointerException{
        Font.loadFont(getClass().getResourceAsStream("/font/BebasNeue-Regular.ttf"), 14);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/Main.fxml"));
        Scene scene = new Scene(root);

        String css = this.getClass().getResource("/css/main/Main.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setWidth(1920);
        stage.setHeight(1080);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goChooseConnection(ActionEvent e) throws Exception {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();

        /*Can't use the FXML standard loader as it requires a parameter-less constructor!*/
        ChooseConnectionState controller = new ChooseConnectionState(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseConnectionState/ChooseConnectionState.fxml"));
        String css = this.getClass().getResource("/css/chooseConnectionState/ChooseConnectionState.css").toExternalForm();
        loader.setController(controller);
        Parent root = loader.load();

        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);

        stage.getScene().setRoot(root);
    }

    @FXML
    public void exit(ActionEvent e){
        executor.shutdown();
        try{
            if(!executor.awaitTermination(5, TimeUnit.SECONDS)){
                executor.shutdownNow();
            }
        }
        catch(InterruptedException exception){
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void goOptions(ActionEvent e) throws Exception{
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/Options.fxml"));
        String css = this.getClass().getResource("/css/main/Options.css").toExternalForm();

        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);

        stage.getScene().setRoot(root);
        stage.show();
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

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/Main.fxml"));
        String css = this.getClass().getResource("/css/main/Main.css").toExternalForm();

        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(css);
        stage.getScene().setRoot(root);

        stage.show();
    }

    @FXML
    public void goCredits(ActionEvent e) throws Exception{
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/Credits.fxml"));
        String css = this.getClass().getResource("/css/main/Credits.css").toExternalForm();

        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);

        stage.getScene().setRoot(root);
        stage.show();
    }


    public void setState(ViewState state){
        this.state = state;
    }

    @Override
    public void printMessage(String message){
        out.println(message);
    }

    public void serverCrashed(){

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

}
