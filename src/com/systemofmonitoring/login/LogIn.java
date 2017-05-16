package com.systemofmonitoring.login;


import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LogIn extends Application {
   // private Parent root;
    private Button buttonActive;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/forms/login_form.fxml"));
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(root, 268, 159));
        primaryStage.show();
    }

    public void mouseClickToButton(Event event) {
        buttonActive = (Button) event.getSource();
        //new LogInController(root, buttonActive);
    }
}
