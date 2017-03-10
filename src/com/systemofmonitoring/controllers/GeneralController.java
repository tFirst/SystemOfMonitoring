package com.systemofmonitoring.controllers;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.json.JSONException;

public class GeneralController {
    protected static Parent root;
    protected static Button buttonActive;

    public GeneralController(Parent root, Button button) throws JSONException {
        Init(root, button);
    }

    public void Init(Parent root, Button button) throws JSONException{
        GeneralController.root = root;
        GeneralController.buttonActive = button;
        AddDatasInTable(new ElectricMeterController(root, buttonActive));
    }

    private void AddDatasInTable(Controller controller) throws JSONException {
        controller.setTableDatas();
    }
}