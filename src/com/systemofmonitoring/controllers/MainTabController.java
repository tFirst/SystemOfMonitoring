package com.systemofmonitoring.controllers;

import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class MainTabController {
    private static Parent root;
    private Label labelTime;
    private ConnectWithServer connectWithServer;
    private double amountActive;

    public MainTabController(Parent root) throws JSONException {
        MainTabController.root = root;
        Init();
    }

    public void Init() throws JSONException {
        connectWithServer = new ConnectWithServer();
        amountActive = Double.parseDouble
                (connectWithServer.SendMessage(new JSONObject()
                .put("table", "ElectricMeter")
                .put("interval", "amount")).getString("amountActive"));
        Label labelValueGeneralElectricalConsumption =
                (Label) MainTabController.root.lookup("#idValueGeneralElectricalConsumption");
        labelValueGeneralElectricalConsumption.setText(String.format("%.3f", amountActive));

        Label labelDate = (Label) MainTabController.root.lookup("#idDate");
        labelTime = (Label) MainTabController.root.lookup("#idTime");

        Locale locale = new Locale("ru", "RU");

        Date currentDate;
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        currentDate = new Date();

        labelDate.setText(df.format(currentDate));

        Timeline timeline =
                new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            Date currentTime = new Date();
            DateFormat tf = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
            labelTime.setText(tf.format(currentTime));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Label labelGeneralElectricalConsumption =
                (Label) MainTabController.root.lookup("#idLabelElectricalConsumption");
        AddConsumption(new ElectricMeterController(),
                labelGeneralElectricalConsumption, String.format("%.3f", amountActive));
    }

    private void AddConsumption(Controller controller, Label label, String text) {
        controller.setConsumption(label, text);
    }
}
