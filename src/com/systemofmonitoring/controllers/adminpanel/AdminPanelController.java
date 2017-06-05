package com.systemofmonitoring.controllers.adminpanel;


import com.systemofmonitoring.connecttoserver.ConnectWithServer;
import com.systemofmonitoring.controllers.Controller;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminPanelController extends Controller {
    private ComboBox comboBoxAdminSensors;
    private ConnectWithServer connectWithServer;
    private ListView listView;
    private Button buttonAdd;

    private String sensorName;

    public AdminPanelController(Parent root) throws JSONException {
        listView = (ListView) root.lookup("#idListViewAdminPanel");
        comboBoxAdminSensors = (ComboBox) root.lookup("#idComboBoxSensors");
        buttonAdd = (Button) root.lookup("#idButtonAdd") ;
        buttonAdd.setDisable(true);
        connectWithServer = new ConnectWithServer();
        fillComboBoxSensors(comboBoxAdminSensors);

        comboBoxAdminSensors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sensorName = comboBoxAdminSensors.getValue().toString();
            buttonAdd.setDisable(false);
        });
    }

    @Override
    public void setDatasInListView() {
        if (!listView.getItems().contains(sensorName))
            listView.getItems().add(sensorName);
        else
            getAlert("Такой датчик уже выбран!");
    }

    public void deleteDataFromListView() {
        if (!listView.getItems().isEmpty() &&
                listView.getFocusModel().getFocusedItem() != null)
            listView.getItems().remove(listView.getFocusModel().getFocusedIndex());
        else if (listView.getItems().isEmpty())
            getAlert("Список пуст!");
        else if (listView.getFocusModel().getFocusedItem() == null)
            getAlert("Вы ничего не выбрали!");
    }
}
