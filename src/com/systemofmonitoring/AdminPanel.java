package com.systemofmonitoring;

import com.systemofmonitoring.controllers.adminpanel.AdminPanelController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;

public class AdminPanel {
    private Scene scene;
    private static Parent root;
    private Button button;
    private static Stage stage;
    private static AdminPanelController adminPanelController;
    AdminPanel(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("forms/admin_panel.fxml"));
        primaryStage.setTitle("Панель администратора");
        scene = new Scene(root, 750, 470);
        adminPanelController =
                new AdminPanelController(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;
    }

    public AdminPanel() {
    }

    public void getButtonId(Event event) throws JSONException, IOException {
        button = (Button) event.getSource();
        if (button.getId().contains("Add"))
            adminPanelController.setDatasInListView();
        else if (button.getId().contains("Exit"))
            stage.close();
    }
}
