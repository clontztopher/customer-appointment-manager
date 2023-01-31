package clontz.clientschedulingapp;

import clontz.clientschedulingapp.Controllers.NavController;
import clontz.clientschedulingapp.DataService.DBConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ClientScheduler extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DBConnector.openConnection();
        NavController.setActiveView(NavController.View.REPORTS);
        URL fxml = ClientScheduler.class.getResource("Controllers/manage-customer-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxml);
        Scene scene = new Scene(fxmlLoader.load(), 960 , 600);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        DBConnector.closeConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}