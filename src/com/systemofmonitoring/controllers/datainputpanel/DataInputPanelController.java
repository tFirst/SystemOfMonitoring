package com.systemofmonitoring.controllers.datainputpanel;


import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import com.systemofmonitoring.controllers.Controller;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DataInputPanelController extends Controller {
    private boolean flag = false;
    private ComboBox comboBoxDataInputSensors;
    private TextField textField;
    private TextArea textArea;
    private ConnectWithServer connectWithServer;
    private Button buttonLoad, buttonInput, buttonOverview;
    private File file;
    private String sensorName;
    JSONArray date, time, value, active, passive;


    public DataInputPanelController(Parent root) throws JSONException {
        comboBoxDataInputSensors = (ComboBox) root.lookup("#idComboBoxSensorsDataInputPanel");
        textField = (TextField) root.lookup("#idTextFieldDataInputPanel");
        textArea = (TextArea) root.lookup("#idTextAreaDataInputPanel");
        buttonLoad = (Button) root.lookup("#idButtonAddDataInputPanel");
        buttonOverview = (Button) root.lookup("#idButtonOverview");
        buttonOverview.setDisable(true);
        buttonInput = (Button) root.lookup("#idButtonInputData");
        connectWithServer = new ConnectWithServer();
        fillComboBoxSensors(comboBoxDataInputSensors);
        comboBoxDataInputSensors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sensorName = comboBoxDataInputSensors.getValue().toString();
            buttonOverview.setDisable(false);
        });
    }

    public void showFileDialog(Stage stage) {
        flag = true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор файла");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            textField.setText(file.getPath());
        }
    }

    public void loadData() {
        textArea.setText(null);
        if (textField.getText().length() == 0)
            getAlert("Вы не выбрали файл!");
        else
            parseFileAndLoad();
    }

    private void parseFileAndLoad() {
        date = new JSONArray();
        time = new JSONArray();
        value = new JSONArray();
        active = new JSONArray();
        passive = new JSONArray();
        try {
            BufferedReader br;
            if (!flag) {
                br = new BufferedReader(new FileReader(textField.getText()));
            } else {
                br = new BufferedReader(new FileReader(file));
            }

            if (sensorName.contains("Electric")) {
                skip(br);
                String line = br.readLine();
                while (line != null) {
                    parseLineElectric(line.replaceAll("\t", " "));
                    StringBuilder sb = new StringBuilder();
                    sb
                            .append(date.get(date.length() - 1) + "\t")
                            .append(time.get(time.length() - 1) + "\t")
                            .append(active.get(active.length() - 1) + "\t")
                            .append(passive.get(passive.length() - 1) + "\t");
                    textArea.appendText(String.valueOf(sb) + "\n");
                    line = br.readLine();
                }
            } else {
                skip(br);
                String line = br.readLine();
                while (line != null) {
                    parseLine(line.replaceAll("\t", " "));
                    StringBuilder sb = new StringBuilder();
                    sb
                            .append(date.get(date.length() - 1) + "\t")
                            .append(time.get(time.length() - 1) + "\t")
                            .append(value.get(value.length() - 1) + "\t");
                    textArea.appendText(String.valueOf(sb) + "\n");
                    line = br.readLine();
                }
            }
        } catch (FileNotFoundException fileNotFound) {
            getAlert("Невозможно загрузить файл");
        } catch (IOException | JSONException e) {
            getAlert(e.getMessage());
        } catch (ParseException e) {
            getAlert(e.getMessage());
        }
    }

    private void parseLineElectric(String line) throws ParseException {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat();
        dateFormat1.applyPattern("dd.mm.yyyy");
        dateFormat2.applyPattern("yyyy-mm-dd");
        String[] lineParts = line.split(" ");
        date.put(dateFormat2.format(dateFormat1.parse(lineParts[0])));
        time.put(lineParts[2] + ":00");
        active.put(lineParts[5].replaceAll(",", "."));
        passive.put(lineParts[7].replaceAll(",", "."));
    }

    private void parseLine(String line) throws ParseException {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat();
        dateFormat1.applyPattern("dd-mm-yyyy");
        dateFormat2.applyPattern("yyyy-mm-dd");
        String[] lineParts = line.split(" ");
        date.put(dateFormat2.format(dateFormat1.parse(lineParts[0])));
        time.put(lineParts[2] + ":00");
        value.put(lineParts[5].replaceAll(",", "."));
    }

    private void skip(BufferedReader br) {
        for (int i = 0; i < 4; i++)
            try {
                br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void sendToServer() throws JSONException {
        if (textArea.getText() == null)
            getAlert("Не загружены данные для внесения в базу");
        else {
            JSONObject answer =
                    connectWithServer.SendMessage(new JSONObject()
                            .put("action", "insert")
                            .put("meter", sensorName)
                            .put("data", new JSONObject()
                                    .put("date", date)
                                    .put("time", time)
                                    .put("active", active)
                                    .put("passive", passive)
                                    .put("value", value)));
            getAlert(answer.getString("answer"));
        }
    }
}
