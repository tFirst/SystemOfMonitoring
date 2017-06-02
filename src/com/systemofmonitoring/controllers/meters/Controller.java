package com.systemofmonitoring.controllers.meters;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.json.JSONException;


public class Controller {

    public void setConsumption(Label label, String text) {
        label.setText(text);
    }

    public void setTableDatas() throws JSONException {
    }

    public void setDatasInListView() {
    }

    public void getAlert(String alertText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(alertText);
        alert.showAndWait();
    }

    public String getGraphicTitle(String buttonType) {
        switch (buttonType) {
            case "hour":
                return "График расхода ресурсов за последний час";
            case "day":
                return "График расхода ресурсов за последние сутки";
            case "week":
                return "График расхода ресурсов за неделю";
            case "month":
                return "График расхода ресурсов за месяц";
            case "year":
                return "График расхода ресурсов за год";
        }
        return "";
    }
}
