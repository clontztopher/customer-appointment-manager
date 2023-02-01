package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.DBConnector;
import clontz.clientschedulingapp.DataService.UserDAO;
import clontz.clientschedulingapp.Helpers.Session;
import clontz.clientschedulingapp.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public Label languageText;

    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;
    public Button signinBtn;

    public void attemptLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("") || password.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a username and password.");
            alert.showAndWait();
            return;
        }

        UserDAO userDAO = new UserDAO(DBConnector.connection);
        User user = userDAO.validateLogin(username, password);

        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Username and/or password incorrect.");
            alert.showAndWait();
            return;
        }

        Session.setUser(user);
        NavController.setActiveView(NavController.View.REPORTS);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("manage-appointment-view.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Reports");
            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: For testing, remove
        usernameField.setText("test");
        passwordField.setText("test");
    }
}
