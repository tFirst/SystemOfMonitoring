package com.systemofmonitoring.controllers;


import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController {
    private Parent root;
    private TextField login;
    private PasswordField password;
    private Button buttonActive;

    public LogInController(Parent root, Button button) {
        this.root = root;
        this.buttonActive = button;
        Init();
    }

    private void Init() {
        login = (TextField) root.lookup("#textFieldLogin");
        password = (PasswordField) root.lookup("#passwordField");
    }
}
