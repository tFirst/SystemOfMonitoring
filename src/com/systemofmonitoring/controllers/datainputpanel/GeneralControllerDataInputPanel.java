package com.systemofmonitoring.controllers.datainputpanel;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.json.JSONException;


public class GeneralControllerDataInputPanel {
    protected static Parent root;
    protected static Button buttonActive;

    public GeneralControllerDataInputPanel(Parent root, Button button) throws JSONException {
        Init(root, button);
    }

    public void Init(Parent root, Button button) throws JSONException{
        GeneralControllerDataInputPanel.root = root;
        GeneralControllerDataInputPanel.buttonActive = button;
        if (buttonActive.getId().equals("idButtonAdd"))
            AddDatasInListView(new DataInputPanelController(root));
    }

    private void AddDatasInListView(DataInputPanelController dataInputPanelController) {
        dataInputPanelController.setDatasInListView();
    }
}
