package com.systemofmonitoring;

import com.systemofmonitoring.controllers.GeneralController;
import com.systemofmonitoring.controllers.MainTabController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;


public class Main extends Application {
    public static Parent root;
    private static Button button;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException, JSONException {
        root = FXMLLoader.load(getClass().getResource("forms/general_form.fxml"));
        primaryStage.setTitle("Сибирь Тензо Пром");
        primaryStage.setScene(new Scene(root, 750, 470));
        primaryStage.show();
        stage = primaryStage;
        System.out.println(root.toString());
        new MainTabController(root);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void getButtonId(Event event) throws JSONException {
        button = (Button) event.getSource();
        new GeneralController(Main.root, Main.button);
    }

    public void onClickReload(Event event) throws Exception {
        new AdminPanel(stage, root);
    }
}
