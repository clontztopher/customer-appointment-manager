package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.DBConnector;
import clontz.clientschedulingapp.DataService.UserDAO;
import clontz.clientschedulingapp.Helpers.LocalizationService;
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
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    public Button signinBtn;
    @FXML
    public Label languageText;
    public Label languageLabel;
    public Label timeZoneLabel;
    public Label timeZoneText;
    public Label usernameLabel;
    public Label passwordLabel;

    public void attemptLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("") || password.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, LocalizationService.getString("form_blank"));
            alert.showAndWait();
            return;
        }

        UserDAO userDAO = new UserDAO(DBConnector.connection);
        User user = userDAO.validateLogin(username, password);

        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, LocalizationService.getString("incorrect_user_password"));
            alert.showAndWait();
            return;
        }

        Session.setUser(user);
        NavController.setActiveView(NavController.View.REPORTS);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("reports-view.fxml"));
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
//        LocalizationService.changeLocale(Locale.FRENCH);
        // END test instructions

        LocalizationService.setResourceBundle("labels");
        usernameLabel.setText(LocalizationService.getString("username"));
        passwordLabel.setText(LocalizationService.getString("password"));
        signinBtn.setText(LocalizationService.getString("signin"));
        languageText.setText(LocalizationService.getString("language"));
        timeZoneText.setText(LocalizationService.getString("local_time_zone"));

        timeZoneLabel.setText(ZoneId.systemDefault().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        languageLabel.setText(Locale.getDefault().getDisplayName());
    }
}
