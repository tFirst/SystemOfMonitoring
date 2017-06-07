package com.systemofmonitoring;

import com.systemofmonitoring.controllers.ReportsController;
import com.systemofmonitoring.controllers.datainputpanel.DataInputPanelController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;


public class ReportsPanel {
    private Scene scene;
    private static Parent root;
    private Button button;
    private static Stage stage;
    private static ReportsController reportsController;

    ReportsPanel(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("forms/reports_panel.fxml"));
        primaryStage.setTitle("Составление отчетов");
        scene = new Scene(root, 750, 470);
        reportsController =
                new ReportsController(root, stage);
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;
    }

    public ReportsPanel() {
    }

    public void getButtonId(Event event) throws JSONException, IOException {
        button = (Button) event.getSource();
        if (button.getId().contains("Overview"))
            reportsController.overviewData();
        else if (button.getId().contains("MakeReport"))
            reportsController.makeReport();
        else if (button.getId().contains("Ok"))
            stage.close();
        else if (button.getId().contains("Exit"))
            stage.close();
    }
}
