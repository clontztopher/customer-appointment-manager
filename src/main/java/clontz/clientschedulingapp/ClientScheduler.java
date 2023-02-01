package clontz.clientschedulingapp;

import clontz.clientschedulingapp.DataService.DBConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientScheduler extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database connection
        DBConnector.openConnection();
        // Load login screen
        URL fxml = ClientScheduler.class.getResource("Controllers/signin-view.fxml");
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