package com.systemofmonitoring.controllers.meters;

import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import com.systemofmonitoring.controllers.Controller;
import com.systemofmonitoring.pojo.ElectricMeterDatasList;
import com.systemofmonitoring.pojo.GasMeterDatasList;
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


public class GasMeterController extends Controller {
    private static Parent root;
    private JSONObject jsonObjectResult;
    private Button buttonActive;
    private DatePicker datePicker;
    JSONArray date, time, value;
    JSONObject jsonObjectQuery = new JSONObject();

    ObservableList<GasMeterDatasList> observableList =
            FXCollections.observableArrayList();

    public GasMeterController() {
    }

    public GasMeterController(Parent root, Button button) throws JSONException {
        GasMeterController.root = root;
        buttonActive = button;
        datePicker = (DatePicker) root.lookup("#idDatePickerGas");
    }

    public void initData() throws JSONException, IOException {
        date = jsonObjectResult.getJSONArray("date");
        time = jsonObjectResult.getJSONArray("time");
        value = jsonObjectResult.getJSONArray("value");

        for (int i = 0; i < time.length(); i++) {
            observableList.add(new GasMeterDatasList
                    (date.getString(i), time.getString(i), value.getDouble(i)));
        }
    }

    @Override
    public void setTableDatas() throws JSONException {
        ConnectWithServer connectWithServer =
                new ConnectWithServer();
        switch (getButtonType(buttonActive)) {
            case "hour":
                jsonObjectQuery
                        .put("table", "GasMeter");
                break;
            case "day":
                jsonObjectQuery
                        .put("table", "GasMeterForDay");
                break;
            case "week":
                jsonObjectQuery
                        .put("table", "GasMeterForWeek");
                break;
            case "month":
                jsonObjectQuery
                        .put("table", "GasMeterForMonth");
                break;
            case "year":
                jsonObjectQuery
                        .put("table", "GasMeterForYear");
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
                TableView idGasTable =
                        (TableView) root.lookup("#idGasTable");

                TableColumn
                        idGasTableTime = new TableColumn("Time"),
                        idGasTableValue = new TableColumn("Value");
                if (getButtonType(buttonActive).equals("hour") ||
                        getButtonType(buttonActive).equals("day")) {
                    idGasTableTime.setCellValueFactory
                            (new PropertyValueFactory<GasMeterDatasList, String>("time"));
                }
                else {
                    idGasTableTime.setCellValueFactory
                            (new PropertyValueFactory<GasMeterDatasList, String>("date"));
                }
                idGasTableValue.setCellValueFactory
                        (new PropertyValueFactory<ElectricMeterDatasList, Double>("value"));

                //idGasTable.getColumns().removeAll();
                idGasTable.getColumns().setAll(idGasTableTime, idGasTableValue);

                idGasTable.setItems(observableList);

                drawGraphic(getGraphicTitle(getButtonType(buttonActive)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getButtonType(Button button) throws JSONException {
        switch(button.getId()) {
            case "buttonForHourGas":
                return "hour";
            case "buttonForDayGas":
                return "day";
            case "buttonForWeekGas":
                return "week";
            case "buttonForMonthGas":
                return "month";
            case "buttonForYearGas":
                return "year";
            default:
                return "";
        }
    }

    public void setConsumption(Label label, String text) {
        label.setText(text);
    }

    private boolean getNull() throws JSONException {
        return jsonObjectResult.getJSONArray("date").length() == 0;
    }

    private void drawGraphic(String title) throws JSONException {
        LineChart lineChart = (LineChart) root.lookup("#idChartGas");
        lineChart.setData(getChartData());
        lineChart.setTitle(title);
    }

    private ObservableList<XYChart.Series<String, Double>> getChartData() throws JSONException {
        ObservableList<XYChart.Series<String, Double>> answer =
                FXCollections.observableArrayList();
        XYChart.Series<String, Double> valueSeries = new XYChart.Series<>();
        valueSeries.setName("Value");

        for (int i = 0; i < time.length(); i++) {
            valueSeries.getData().add(new XYChart.Data(Integer.toString(i), value.get(i)));
        }
        answer.addAll(valueSeries);
        return answer;
    }
}
