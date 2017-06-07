package com.systemofmonitoring.controllers;


import com.healthmarketscience.jackcess.Table;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import com.systemofmonitoring.controllers.meters.ElectricMeterController;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReportsController extends Controller {
    private static Parent root;
    private static Stage stage;
    private boolean flag = false;
    private DatePicker datePicker;
    private ComboBox comboBoxReportPanel, comboBoxIntervalReportPanel;
    private TextArea textArea;
    private Label label;
    private ConnectWithServer connectWithServer;
    private Button buttonShowDatas, buttonMakeReport;
    private RadioButton radioButtonResources, radioButtonSensors;
    private File file;
    private String sensorName, resourceName, saveFileName, interval;
    JSONArray date, time, value, active, passive;
    JSONObject jsonObjectResult = new JSONObject();


    public ReportsController(Parent root, Stage stage) throws JSONException {
        ReportsController.root = root;
        ReportsController.stage = stage;
        Init();
    }

    private void InitArrays() throws JSONException {
        date = jsonObjectResult.getJSONArray("date");
        time = jsonObjectResult.getJSONArray("time");
        active = jsonObjectResult.getJSONArray("activeValue");
        passive = jsonObjectResult.getJSONArray("passiveValue");
    }

    private void Init() {
        comboBoxReportPanel = (ComboBox) root.lookup("#idComboBoxReportPanel");
        comboBoxIntervalReportPanel = (ComboBox) root.lookup("#idComboBoxIntervalReportPanel");
        datePicker = (DatePicker) root.lookup("#idDatePickerReportPanel");
        textArea = (TextArea) root.lookup("#idTextAreaReportPanel");
        buttonShowDatas = (Button) root.lookup("#idButtonOverviewDatas");
        buttonMakeReport = (Button) root.lookup("#idButtonMakeReport");
        label = (Label) root.lookup("#idLabelInReportPanel");
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonResources = (RadioButton) root.lookup("#idRadioButtonMakeReportToResource");
        radioButtonResources.setToggleGroup(toggleGroup);
        radioButtonSensors = (RadioButton) root.lookup("#idRadioButtonMakeReportToSensor");
        radioButtonSensors.setToggleGroup(toggleGroup);
        connectWithServer = new ConnectWithServer();

        comboBoxIntervalReportPanel.getItems().addAll
                ("За час", "За день", "За неделю", "За месяц", "За год");

        comboBoxIntervalReportPanel.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    interval = comboBoxIntervalReportPanel.getValue().toString();
                }
        );

        toggleGroup.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
                    if (toggleGroup.getSelectedToggle() != null) {
                        if (toggleGroup.getSelectedToggle().getUserData().toString().equals("Sensor")) {
                            try {
                                label.setText("Выберите датчик:");
                                comboBoxReportPanel.getItems().clear();
                                comboBoxReportPanel.setPromptText("Выберите датчик");
                                fillComboBoxSensors(comboBoxReportPanel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            comboBoxReportPanel.getSelectionModel().selectedItemProperty().addListener(
                                    (observable, oldValue, newValue) -> {
                                        sensorName = comboBoxReportPanel.getValue().toString();
                                        resourceName = "";
                                    });
                        } else if (toggleGroup.getSelectedToggle().getUserData().toString().equals("Resource")) {
                            try {
                                label.setText("Выберите энергоресурс:");
                                comboBoxReportPanel.getItems().clear();
                                comboBoxReportPanel.setPromptText("Выберите энергоресурс");
                                fillComboBoxResource(comboBoxReportPanel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            comboBoxReportPanel.getSelectionModel().selectedItemProperty().addListener(
                                    (observable, oldValue, newValue) -> {
                                        resourceName = comboBoxReportPanel.getValue().toString();
                                        sensorName = "";
                                    });
                        }
                    }
                });
    }

    public void fillComboBoxResource(ComboBox comboBox) throws JSONException {
        JSONObject querieListSensors = new JSONObject();
        querieListSensors
                .put("action", "get resources");
        JSONObject result = new ConnectWithServer().SendMessage(querieListSensors);

        JSONArray jsonArray = (JSONArray) result.get("resources");

        for (int i = 0; i < jsonArray.length(); i++)
            comboBox.getItems().add(jsonArray.get(i));
    }

    public void showFileDialog(Stage stage) {
        flag = true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор файла для сохранения");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            saveFileName = file.getPath();
        }
    }

    public void overviewData() throws JSONException {
        if (interval.isEmpty())
            getAlert("Не выбран интервал");
        else {
            textArea.setText("");
            jsonObjectResult = connectWithServer.SendMessage(makeQuery());
            InitArrays();
            for (int i = 0; i < date.length(); i++) {
                StringBuilder stringBuilder = new StringBuilder();
                if (interval.equals("day"))
                    stringBuilder
                            .append(date.getString(i) + "\t")
                            .append(time.getString(i) + "\t")
                            .append(active.getDouble(i) + "\t")
                            .append(passive.getDouble(i) + "\t");
                else
                    stringBuilder
                            .append(date.getString(i) + "\t")
                            .append(active.getDouble(i) + "\t")
                            .append(passive.getDouble(i) + "\t");
                textArea.appendText(stringBuilder.toString() + "\n");
            }
        }
    }

    private JSONObject makeQuery() throws JSONException {
        JSONObject jsonObjectQuery = new JSONObject();

        if (!sensorName.equals("")) {
            System.out.println(interval);
            if (sensorName.contains("Electric")) {
                if (checkInterval(interval).equals("hour"))
                    jsonObjectQuery
                            .put("table", "ElectricMeter");
                if (checkInterval(interval).equals("day"))
                    jsonObjectQuery
                            .put("table", "ElectricMeterForDay");
                if (checkInterval(interval).equals("week"))
                    jsonObjectQuery
                            .put("table", "ElectricMeterForWeek");
            } else if (sensorName.contains("Gas"))
                jsonObjectQuery
                        .put("table", "GasMeter");
        } else {
            if (resourceName.contains("Electric"))
                jsonObjectQuery
                        .put("resource", "Electric");
            else if (resourceName.contains("Gas"))
                jsonObjectQuery
                        .put("resource", "Gas");
        }

        jsonObjectQuery
                .put("interval", checkInterval(interval));

        if (datePicker.getValue() != null)
            jsonObjectQuery
                    .put("date", datePicker.getValue().toString());

        return jsonObjectQuery;
    }

    private String checkInterval(String interval) {
        switch (interval) {
            case "За час":
                return "hour";
            case "За день":
                return "day";
            case "За неделю":
                return "week";
            case "За месяц":
                return "month";
            case "За год":
                return "year";
            default:
                return "";
        }
    }

    public void makeReport() {
        showFileDialog(stage);
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(saveFileName));

            BaseFont font = BaseFont.createFont("c:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font fontTitle = new Font(font, 16, Font.BOLD);
            Font fontReportName = new Font(font, 14, Font.ITALIC | Font.UNDERLINE);
            Font smallTitleText = new Font(font, 12, Font.BOLD);
            Font fontText = new Font(font, 12, Font.NORMAL);

            document.open();

            Paragraph title = new Paragraph("Отчет", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph purpose;
            if (sensorName.equals(""))
                purpose = new Paragraph(interval + " по энергоресурсу " + resourceName, fontReportName);
            else
                purpose = new Paragraph(interval + " по датчику " + sensorName, fontReportName);
            purpose.setSpacingAfter(6);
            purpose.setAlignment(Element.ALIGN_CENTER);
            document.add(purpose);

            StringBuilder stringBuilderTitle = new StringBuilder();
            Paragraph smallTitle;
            if (time.length() == 0) {
                stringBuilderTitle
                        .append(String.format("%12s", "Дата"))
                        .append(String.format("%20s", "Активный расход"))
                        .append(String.format("%20s", "Пассивный расход"));
                smallTitle = new Paragraph(stringBuilderTitle.toString(), smallTitleText);
            }
            else {
                stringBuilderTitle
                        .append(String.format("%15s", "Дата"))
                        .append(String.format("%15s", "Время"))
                        .append(String.format("%25s", "Активный расход"))
                        .append(String.format("%25s", "Пассивный расход"));
                smallTitle = new Paragraph(stringBuilderTitle.toString(), smallTitleText);
            }
            smallTitle.setSpacingAfter(3);
            document.add(smallTitle);

            Paragraph text;
            if (time.length() == 0) {
                for (int i = 0; i < date.length(); i++) {
                    text = new Paragraph(String.format("%15s", date.getString(i))
                            + String.format("%25s", active.getDouble(i))
                            + String.format("%25s", passive.getDouble(i)), fontText);
                    text.setSpacingAfter(1);
                    document.add(text);
                }
            } else {
                for (int i = 0; i < date.length(); i++) {
                    text = new Paragraph(String.format("%15s", date.getString(i))
                            + String.format("%15s", time.getString(i))
                            + String.format("%25s", active.getDouble(i))
                            + String.format("%25s", passive.getDouble(i)), fontText);
                    text.setSpacingAfter(1);
                    document.add(text);
                }
            }

        } catch (Exception de) {
            getAlert(de.getMessage());
            de.printStackTrace();
        }
        document.close();
        getAlert("Отчет создан");
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
}
