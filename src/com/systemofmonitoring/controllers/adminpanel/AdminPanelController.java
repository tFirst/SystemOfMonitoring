package com.systemofmonitoring.controllers.adminpanel;


import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import com.systemofmonitoring.controllers.meters.Controller;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminPanelController extends Controller {
    private ComboBox
            comboBoxAdminSensors,
            comboBoxAdminTables,
            comboBoxAdminColumns;
    private ConnectWithServer connectWithServer;
    private ListView listView;

    private String sensorName, tableName, columnName;

    public AdminPanelController(Parent root) throws JSONException {
        listView = (ListView) root.lookup("#idListViewAdminPanel");
        comboBoxAdminSensors = (ComboBox) root.lookup("#idComboBoxSensors");
        comboBoxAdminTables = (ComboBox) root.lookup("#idComboBoxTables");
        comboBoxAdminColumns = (ComboBox) root.lookup("#idComboBoxColumns");
        comboBoxAdminTables.setDisable(true);
        comboBoxAdminColumns.setDisable(true);
        connectWithServer = new ConnectWithServer();
        fillComboBoxSensors();
    }

    private void fillComboBoxSensors() throws JSONException {
        JSONObject querieListSensors = new JSONObject();
        querieListSensors
                .put("action", "get meters");
        JSONObject result = connectWithServer.SendMessage(querieListSensors);

        JSONArray jsonArray = (JSONArray) result.get("meters");

        for (int i = 0; i < jsonArray.length(); i++)
            comboBoxAdminSensors.getItems().add(jsonArray.get(i));

        comboBoxAdminSensors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!comboBoxAdminTables.getItems().isEmpty())
                    comboBoxAdminTables.getItems().remove(0, comboBoxAdminTables.getItems().size());
                comboBoxAdminTables.setDisable(false);
                sensorName = comboBoxAdminSensors.getValue().toString();
                fillComboBoxTables(comboBoxAdminSensors.getValue().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void fillComboBoxTables(String sensorName) throws JSONException {
        JSONObject querieListTables = new JSONObject();
        querieListTables
                .put("action", "get tables")
                .put("database", getDatabaseName(sensorName));
        JSONObject result =
                connectWithServer.SendMessage(querieListTables);

        JSONArray jsonArray = (JSONArray) result.get("tables");

        for (int i = 0; i < jsonArray.length(); i++)
            comboBoxAdminTables.getItems().add(jsonArray.get(i));

        comboBoxAdminTables.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!comboBoxAdminColumns.getItems().isEmpty())
                    comboBoxAdminColumns.getItems().remove(0, comboBoxAdminColumns.getItems().size());
                tableName = comboBoxAdminTables.getValue().toString();
                comboBoxAdminColumns.setDisable(false);
                fillComboBoxColumns(comboBoxAdminTables.getValue().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void fillComboBoxColumns(String tableName) throws JSONException {
        JSONObject querieListTables = new JSONObject();
        querieListTables
                .put("action", "get columns")
                .put("tableName", tableName);
        JSONObject result = connectWithServer.SendMessage(querieListTables);

        JSONArray jsonArray = (JSONArray) result.get("columns");

        for (int i = 0; i < jsonArray.length(); i++)
            comboBoxAdminColumns.getItems().add(jsonArray.get(i));

        comboBoxAdminColumns.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            columnName = comboBoxAdminColumns.getValue().toString();
        });
    }

    private String getDatabaseName(String sensorName) {
        if (sensorName.contains("Electric"))
            return "Electric";
        else if (sensorName.contains("Gas"))
            return "Gas";
        else if (sensorName.contains("Water"))
            return "Water";
        else if (sensorName.contains("Temperature"))
            return "Temperature";
        else
            return "Pressure";
    }

    @Override
    public void setDatasInListView() {
        listView.getItems().add(sensorName + "\t" + tableName + "\t" + columnName);
    }
}
