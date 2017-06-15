package com.systemofmonitoring;

import com.systemofmonitoring.controllers.meters.GeneralController;
import com.systemofmonitoring.controllers.MainTabController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.json.JSONException;


public class Main extends Application {
    private static Parent root;
    private static Button button;
    private MenuItem menuItem;
    private static Stage stage;
    private MainTabController mainTabController;

    public Main() {}

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("forms/general_form.fxml"));
        primaryStage.setTitle("Сибирь Тензо Пром");
        primaryStage.setScene(new Scene(root, 750, 470));
        primaryStage.show();
        stage = primaryStage;
        LogIn logIn = new LogIn(new Stage());
        System.out.println(root.toString());
        mainTabController = new MainTabController(root);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void getButtonId(Event event) throws JSONException {
        button = (Button) event.getSource();
        new GeneralController(Main.root, Main.button);
    }

    public void onClickReload(Event event) throws Exception {
        mainTabController = new MainTabController(root);
    }

    public void onClickMenuItem(ActionEvent actionEvent) throws Exception {
        menuItem = (MenuItem) actionEvent.getSource();
        if (menuItem.getId().contains("Exit"))
            stage.close();
        else if (menuItem.getId().contains("Report"))
            new ReportsPanel(new Stage());
        else if (menuItem.getId().contains("Settings"))
            new SettingsPanel(new Stage());
        else if (menuItem.getId().contains("DataInput"))
            new DataInputPanel(new Stage());
    }
}
