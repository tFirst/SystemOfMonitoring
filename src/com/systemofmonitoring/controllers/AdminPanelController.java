package com.systemofmonitoring.controllers;


import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import org.controlsfx.control.CheckComboBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminPanelController {
    private ComboBox
            comboBoxAdminSensors,
            comboBoxAdminTables;
    private CheckComboBox comboBoxAdminColumns;
    private ConnectWithServer connectWithServer;

    public AdminPanelController(Parent root, Scene scene) {
        comboBoxAdminSensors = (ComboBox) root.lookup("#idComboBoxSensors");
        comboBoxAdminTables = (ComboBox) root.lookup("#idComboBoxTables");
        comboBoxAdminTables.setDisable(true);
        comboBoxAdminColumns = new CheckComboBox();
        comboBoxAdminColumns.setLayoutX(250);
        comboBoxAdminColumns.setLayoutY(218);
        comboBoxAdminColumns.setMinWidth(150.);
        comboBoxAdminColumns.setDisable(true);
        Pane rootGroup = (Pane) scene.getRoot();
        rootGroup.getChildren().add(comboBoxAdminColumns);
        connectWithServer = new ConnectWithServer();
    }

    public void fillComboBoxSensors() throws JSONException {
        comboBoxAdminSensors.getItems().addAll("Electric meter", "Gas meter");
//        if (comboBoxAdminSensors.getValue() != null) {
//            comboBoxAdminTables.setDisable(false);
//        }

        System.out.println(comboBoxAdminSensors.getValue());

        //if (!comboBoxAdminTables.isDisable())
            fillComboBoxTables();
    }

    public void fillComboBoxTables() throws JSONException {
        JSONObject querieListTables = new JSONObject();
            querieListTables
                    .put("action", "get tables")
                    .put("database", "electric");
        JSONObject result =
                connectWithServer.SendMessage(querieListTables);

        JSONArray jsonArray = (JSONArray) result.get("tables");

        ArrayList<String> listTablesAL = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++)
            listTablesAL.add((String) jsonArray.get(i));

        ObservableList<String> observableList =
                FXCollections.observableArrayList(listTablesAL);

        comboBoxAdminTables.setItems(observableList);

        if (comboBoxAdminTables.getValue() != null)
            comboBoxAdminColumns.setDisable(false);

            fillComboBoxColumns();
    }

    public void fillComboBoxColumns() throws JSONException {
        JSONObject querieListTables = new JSONObject();
        querieListTables
                .put("action", "get columns")
                .put("database", "electric")
                .put("tableName", "tblИзмерения");
        JSONObject result = connectWithServer.SendMessage(querieListTables);

        JSONArray jsonArray = (JSONArray) result.get("columns");

        ArrayList<JSONObject> listTableJO = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++)
            listTableJO.add((JSONObject) jsonArray.get(i));

        ArrayList<String> listTableS = new ArrayList<>();

        for (JSONObject aListTableJO : listTableJO)
            listTableS.add(aListTableJO.getString("name"));

        ObservableList<String> observableList =
                FXCollections.observableArrayList(listTableS);

        for (String listTable : listTableS)
            comboBoxAdminColumns.getItems().add(listTable);
    }
}
