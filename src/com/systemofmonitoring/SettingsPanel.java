package com.systemofmonitoring;

import com.systemofmonitoring.controllers.settingspanel.SettingsPanelController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;

public class SettingsPanel {
    private Scene scene;
    private static Parent root;
    private Button button;
    private static Stage stage;
    private static SettingsPanelController adminPanelController;

    SettingsPanel(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("forms/settings_panel.fxml"));
        primaryStage.setTitle("Панель настроек");
        scene = new Scene(root, 750, 470);
        adminPanelController =
                new SettingsPanelController(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;
    }

    public SettingsPanel() {
    }

    public void getButtonId(Event event) throws JSONException, IOException {
        button = (Button) event.getSource();
        if (button.getId().contains("Add"))
            adminPanelController.setDatasInListView();
        else if (button.getId().contains("Delete"))
            adminPanelController.deleteDataFromListView();
        else if (button.getId().contains("Ok"))
            stage.close();
        else if (button.getId().contains("Exit"))
            stage.close();
    }
}
