package com.systemofmonitoring.controllers;

import javafx.scene.control.Label;
import org.json.JSONException;

/**
 * Created by Stanislav Trushin on 01.03.17.
 */
public class Controller {

    public void setConsumption(Label label, String text) {
        label.setText(text);
    }

    public void setTableDatas() throws JSONException {
    }
}