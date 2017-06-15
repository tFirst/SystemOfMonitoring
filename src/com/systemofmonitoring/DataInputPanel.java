package com.systemofmonitoring;

import com.systemofmonitoring.controllers.datainputpanel.DataInputPanelController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;


public class DataInputPanel {
    private Scene scene;
    private static Parent root;
    private Button button;
    private static Stage stage;
    private static DataInputPanelController dataInputPanelController;

    DataInputPanel(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("forms/data_input.fxml"));
        primaryStage.setTitle("Внесение данных в систему");
        scene = new Scene(root, 750, 470);
        dataInputPanelController =
                new DataInputPanelController(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;
    }

    public DataInputPanel() {
    }

    public void getButtonId(Event event) throws JSONException, IOException {
        button = (Button) event.getSource();
        if (button.getId().contains("Overview"))
            dataInputPanelController.showFileDialog(stage);
        else if (button.getId().contains("Add"))
            dataInputPanelController.loadData();
        else if (button.getId().contains("InputData"))
            dataInputPanelController.sendToServer();
        else if (button.getId().contains("Ok"))
            stage.close();
        else if (button.getId().contains("Exit"))
            stage.close();
    }
}
