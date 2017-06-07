package com.systemofmonitoring.controllers;

import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Controller {

    public void setConsumption(Label label, String text) {
        label.setText(text);
    }

    public void setTableDatas() throws JSONException {
    }

    public void setDatasInListView() {
    }

    public String getButtonType(Button button) throws JSONException {
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

    public void getAlert(String alertText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(alertText);
        alert.showAndWait();
    }

    public String getGraphicTitle(String buttonType) {
        switch (buttonType) {
            case "hour":
                return "График расхода ресурсов за час";
            case "day":
                return "График расхода ресурсов за сутки";
            case "week":
                return "График расхода ресурсов за неделю";
            case "month":
                return "График расхода ресурсов за месяц";
            case "year":
                return "График расхода ресурсов за год";
        }
        return "";
    }

    public void fillComboBoxSensors(ComboBox comboBox) throws JSONException {
        JSONObject querieListSensors = new JSONObject();
        querieListSensors
                .put("action", "get meters");
        JSONObject result = new ConnectWithServer().SendMessage(querieListSensors);

        JSONArray jsonArray = (JSONArray) result.get("meters");

        for (int i = 0; i < jsonArray.length(); i++)
            comboBox.getItems().add(jsonArray.get(i));
    }
}
