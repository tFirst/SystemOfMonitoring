package com.systemofmonitoring;

import com.systemofmonitoring.controllers.AdminPanelController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONException;

public class AdminPanel {
    private AdminPanelController adminPanelController;
    private Scene scene;
    Parent root;
    public AdminPanel(Stage primaryStage, Parent rootOld) throws Exception {
        primaryStage.close();
        root = rootOld;
        root = FXMLLoader.load(getClass().getResource("forms/admin_panel.fxml"));
        primaryStage.setTitle("Панель администратора");
        scene = new Scene(root, 750, 470);
        adminPanelController =
                new AdminPanelController(root, scene);
        primaryStage.setScene(scene);
        primaryStage.show();

        adminPanelController.fillComboBoxSensors();
    }

    public AdminPanel() {
    }

    public void onClickCDTable(Event event) throws JSONException {
        //adminPanelController.fillComboBoxTables(chooseSensor);
    }

    public void onClickCCBColumns(Event event) throws JSONException {
        //adminPanelController.fillComboBoxColumns(chooseTable);
    }
}
