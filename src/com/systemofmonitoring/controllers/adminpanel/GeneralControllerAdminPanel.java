package com.systemofmonitoring.controllers.adminpanel;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.json.JSONException;


public class GeneralControllerAdminPanel {
    protected static Parent root;
    protected static Button buttonActive;

    public GeneralControllerAdminPanel(Parent root, Button button) throws JSONException {
        Init(root, button);
    }

    public void Init(Parent root, Button button) throws JSONException{
        GeneralControllerAdminPanel.root = root;
        GeneralControllerAdminPanel.buttonActive = button;
        if (buttonActive.getId().equals("idButtonAdd"))
            AddDatasInListView(new AdminPanelController(root));
    }

    private void AddDatasInListView(AdminPanelController adminPanelController) {
        adminPanelController.setDatasInListView();
    }
}
