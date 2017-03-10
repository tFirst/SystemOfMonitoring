package com.systemofmonitoring.controllers;

import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import com.systemofmonitoring.pojo.ElectricMeterDatasList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ElectricMeterController extends Controller {
    private static Parent root;
    private JSONObject jsonObjectResult;
    private Button buttonActive;
    JSONArray time, active, passive;
    JSONObject jsonObjectQuery = new JSONObject();

    ObservableList<ElectricMeterDatasList> observableList =
            FXCollections.observableArrayList();

    public ElectricMeterController() {
    }

    public ElectricMeterController(Parent root, Button button) throws JSONException {
        ElectricMeterController.root = root;
        buttonActive = button;
    }

    public void initData() throws JSONException, IOException {
        time = jsonObjectResult.getJSONArray("time");
        active = jsonObjectResult.getJSONArray("activeValue");
        passive = jsonObjectResult.getJSONArray("passiveValue");

        for (int i = 0; i < time.length(); i++) {
            observableList.add(new ElectricMeterDatasList
                    (time.getString(i), active.getDouble(i), passive.getDouble(i)));
        }
    }

    @Override
    public void setTableDatas() throws JSONException {
        ConnectWithServer connectWithServer =
                new ConnectWithServer();
        jsonObjectQuery
                .put("table", "ElectricMeter")
                .put("interval", getButtonType(buttonActive));
        jsonObjectResult = connectWithServer.SendMessage(jsonObjectQuery);

        pushOnGetDatas();
    }

    private void pushOnGetDatas() throws JSONException {
        try {
            if (getNull()) {
                getAlert();
            } else {
                try {
                    initData();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                TableView idElectricTable =
                        (TableView) root.lookup("#idElectricTable");

                TableColumn idElectricTableTime =
                        new TableColumn("Time");
                TableColumn idElectricTableValueActive =
                        new TableColumn("Value Active");
                TableColumn idElectricTableValuePassive =
                        new TableColumn("Value Passive");
                idElectricTableTime.setCellValueFactory
                        (new PropertyValueFactory<ElectricMeterDatasList, String>("time"));
                idElectricTableValueActive.setCellValueFactory
                        (new PropertyValueFactory<ElectricMeterDatasList, Double>("valueActive"));
                idElectricTableValuePassive.setCellValueFactory
                        (new PropertyValueFactory<ElectricMeterDatasList, Double>("valuePassive"));

                idElectricTable.getColumns().removeAll();
                idElectricTable.getColumns().setAll(idElectricTableTime, idElectricTableValueActive, idElectricTableValuePassive);

                idElectricTable.setItems(observableList);

                switch (getButtonType(buttonActive)) {
                    case "hour":
                        drawGraphic("График расхода ресурсов за последний час");
                        break;
                    case "day":
                        drawGraphic("График расхода ресурсов за последние сутки");
                        break;
                    case "week":
                        drawGraphic("График расхода ресурсов за неделю");
                        break;
                    case "month":
                        drawGraphic("График расхода ресурсов за месяц");
                        break;
                    case "year":
                        drawGraphic("График расхода ресурсов за год");
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getButtonType(Button button) throws JSONException {
        switch(button.getId()) {
            case "buttonForHourElectric":
                return "hour";
            case "buttonForDayElectric":
                return "day";
            case "buttonForWeekElectric":
                return "week";
            case "buttonForMonthElectric":
                return "month";
            case "buttonForYearElectric":
                return "year";
            default:
                return "";
        }
    }

    public void setConsumption(Label label, String text) {
        label.setText(text);
    }

    private void getAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText("Данных за указанный промежуток времени нет");
        alert.showAndWait();
    }

    private boolean getNull() throws JSONException {
        return jsonObjectResult.getJSONArray("time").length() == 0;
    }

    private void drawGraphic(String title) throws JSONException {
        LineChart lineChart = (LineChart) root.lookup("#idChartElectric");
        lineChart.setData(getChartData());
        lineChart.setTitle(title);
    }

    private ObservableList<XYChart.Series<String, Double>> getChartData() throws JSONException {
        ObservableList<XYChart.Series<String, Double>> answer = FXCollections
                .observableArrayList();
        XYChart.Series<String, Double> activeSeries = new XYChart.Series<>();
        XYChart.Series<String, Double> passiveSeries = new XYChart.Series<>();
        activeSeries.setName("Active");
        passiveSeries.setName("Passive");

        for (int i = 0; i < time.length(); i++) {
            activeSeries.getData().add(new XYChart.Data(Integer.toString(i), active.get(i)));
            passiveSeries.getData().add(new XYChart.Data(Integer.toString(i), passive.get(i)));
        }
        answer.addAll(activeSeries, passiveSeries);
        return answer;
    }
}
