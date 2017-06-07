package com.systemofmonitoring.controllers.meters;

import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import com.systemofmonitoring.controllers.Controller;
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
    private DatePicker datePicker;
    JSONArray date, time, active, passive;
    JSONObject jsonObjectQuery = new JSONObject();

    ObservableList<ElectricMeterDatasList> observableList =
            FXCollections.observableArrayList();

    public ElectricMeterController() {
    }

    public ElectricMeterController(Parent root, Button button) throws JSONException {
        ElectricMeterController.root = root;
        buttonActive = button;
        datePicker = (DatePicker) root.lookup("#idDatePickerElectric");
    }

    public void initData() throws JSONException, IOException {
        date = jsonObjectResult.getJSONArray("date");
        time = jsonObjectResult.getJSONArray("time");
        active = jsonObjectResult.getJSONArray("activeValue");
        passive = jsonObjectResult.getJSONArray("passiveValue");

        for (int i = 0; i < time.length(); i++) {
            observableList.add(new ElectricMeterDatasList
                    (date.getString(i), time.getString(i), active.getDouble(i), passive.getDouble(i)));
        }
    }

    @Override
    public void setTableDatas() throws JSONException {
        ConnectWithServer connectWithServer =
                new ConnectWithServer();
        switch (getButtonType(buttonActive)) {
            case "hour":
                jsonObjectQuery
                        .put("table", "ElectricMeter");
                break;
            case "day":
                jsonObjectQuery
                        .put("table", "ElectricMeterForDay");
                break;
            case "week":
                jsonObjectQuery
                        .put("table", "ElectricMeterForWeek");
                break;
            case "month":
                jsonObjectQuery
                        .put("table", "ElectricMeterForMonth");
                break;
            case "year":
                jsonObjectQuery
                        .put("table", "ElectricMeterForYear");
                break;
        }
        jsonObjectQuery
                .put("interval", getButtonType(buttonActive));
        if (datePicker.getValue() != null) {
            jsonObjectQuery
                    .put("date", datePicker.getValue().toString());
            System.out.println(datePicker.getValue().toString());
        }

        jsonObjectResult = connectWithServer.SendMessage(jsonObjectQuery);

        pushOnGetDatas();
    }

    private void pushOnGetDatas() throws JSONException {
        try {
            if (getNull()) {
                getAlert("Данных за указанный промежуток времени нет!");
            } else {
                try {
                    initData();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                TableView idElectricTable =
                        (TableView) root.lookup("#idElectricTable");

                TableColumn
                        idElectricTableTime =
                                new TableColumn("Время"),
                        idElectricTableValueActive =
                                new TableColumn("Активный расход"),
                        idElectricTableValuePassive =
                                new TableColumn("Пассивный расход");
                if (getButtonType(buttonActive).equals("hour") ||
                        getButtonType(buttonActive).equals("day")) {
                    idElectricTableTime.setCellValueFactory
                            (new PropertyValueFactory<ElectricMeterDatasList,
                                    String>("time"));
                }
                else {
                    idElectricTableTime.setCellValueFactory
                            (new PropertyValueFactory<ElectricMeterDatasList,
                                    String>("date"));
                }
                idElectricTableValueActive.setCellValueFactory
                        (new PropertyValueFactory<ElectricMeterDatasList,
                                Double>("valueActive"));
                idElectricTableValuePassive.setCellValueFactory
                        (new PropertyValueFactory<ElectricMeterDatasList,
                                Double>("valuePassive"));

                idElectricTable.getColumns().removeAll();
                idElectricTable.getColumns()
                        .setAll(idElectricTableTime,
                                idElectricTableValueActive,
                                idElectricTableValuePassive);

                idElectricTable.setItems(observableList);

                drawGraphic(getGraphicTitle(getButtonType(buttonActive)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setConsumption(Label label, String text) {
        label.setText(text);
    }

    private boolean getNull() throws JSONException {
        return jsonObjectResult.getJSONArray("date").length() == 0;
    }

    private void drawGraphic(String title) throws JSONException {
        LineChart lineChart = (LineChart) root.lookup("#idChartElectric");
        lineChart.setData(getChartData());
        lineChart.setTitle(title);
    }

    private ObservableList<XYChart.Series<String, Double>> getChartData() throws JSONException {
        ObservableList<XYChart.Series<String, Double>> answer =
                FXCollections.observableArrayList();
        XYChart.Series<String, Double> activeSeries =
                new XYChart.Series<>();
        XYChart.Series<String, Double> passiveSeries =
                new XYChart.Series<>();
        activeSeries.setName("Активный расход");
        passiveSeries.setName("Пассивный расход");

        for (int i = 0; i < time.length(); i++) {
            activeSeries.getData()
                    .add(new XYChart.Data(Integer.toString(i), active.get(i)));
            passiveSeries.getData()
                    .add(new XYChart.Data(Integer.toString(i), passive.get(i)));
        }
        answer.addAll(activeSeries, passiveSeries);
        return answer;
    }

    public JSONArray getDate() {
        return date;
    }

    public JSONArray getTime() {
        return time;
    }

    public JSONArray getActive() {
        return active;
    }

    public JSONArray getPassive() {
        return passive;
    }
}
