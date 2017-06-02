package com.systemofmonitoring.controllers;


import com.systemofmonitoring.LogIn;
import com.systemofmonitoring.login.CheckUsersDatas;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController extends LogIn {
    private Parent root;
    private TextField login;
    private PasswordField password;
    private Button buttonActive;
    private CheckUsersDatas checkUsersDatas;

    public LogInController(Parent root, Button button) {
        this.root = root;
        this.buttonActive = button;
        Init();
        if (buttonActive.getId().contains("Cancel"))
            close();
        if (buttonActive.getId().contains("Enter")) {
            checkUsersDatas = new CheckUsersDatas();
            if (checkUsersDatas.check())
                System.out.println("OK");
        }
    }

    private void Init() {
        login = (TextField) root.lookup("#textFieldLogin");
        password = (PasswordField) root.lookup("#passwordField");
    }
}
