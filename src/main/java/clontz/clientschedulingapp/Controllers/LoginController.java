package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public Label languageText;

    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;

    public void attemptLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            String query = "SELECT User_Name, Password from users WHERE User_Name=?;";
            PreparedStatement prepStmt = DBConnector.connection.prepareStatement(query);
            prepStmt.setString(1, username);
            ResultSet resultSet = prepStmt.executeQuery();

            if (!resultSet.next()) {
                throw new Exception("Username not found.");
            }

            String savedPassword = resultSet.getString("Password");

            if (!savedPassword.equals(password)) {
                throw new Exception("Incorrect password.");
            }

            System.out.println("Success");

            Parent root = FXMLLoader.load(getClass().getResource("reports-view.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Reports");
            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            // TODO: Output to view
            System.out.println(e.getMessage());
        }

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
