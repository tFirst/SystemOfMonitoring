package com.systemofmonitoring;

import com.systemofmonitoring.controllers.LogInController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LogIn {
    private static Parent root;
    private Button buttonActive;
    private static Stage stage;

    public LogIn() {}

    LogIn(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("forms/login_form.fxml"));
        primaryStage.setTitle("Log In");
        stage = primaryStage;
        primaryStage.setScene(new Scene(root, 268, 159));
        primaryStage.show();
    }

    public void mouseClickToButton(Event event) {
        buttonActive = (Button) event.getSource();
        new LogInController(root, buttonActive);
    }

    protected void close() {
        stage.close();
    }
}
