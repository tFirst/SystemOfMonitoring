package com.systemofmonitoring.controllers.meters;

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
        if (buttonActive.getId().contains("Electric"))
            AddDatasInTable(new ElectricMeterController(root, buttonActive));
        else if (buttonActive.getId().contains("Gas"))
            AddDatasInTable(new GasMeterController(root, buttonActive));
    }

    private void AddDatasInTable(Controller controller) throws JSONException {
        controller.setTableDatas();
    }

    private void exit() {

    }
}
