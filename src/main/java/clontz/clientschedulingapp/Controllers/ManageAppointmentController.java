package clontz.clientschedulingapp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageAppointmentController implements Initializable {
    public Pane navMenu;
    public Text customerIDField;
    public TextField titleInput;
    public TextField locationInput;
    public TextArea descriptionTextBox;
    public ComboBox contactBox;
    public ComboBox customerBox;
    public DatePicker startDateSelect;
    public DatePicker endDateSelect;
    public ComboBox endTimeSelect;
    public ComboBox startTimeSelect;
    public TextField typeInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void saveChanges(ActionEvent actionEvent) {
    }

    public void clearAppointmentForm(ActionEvent actionEvent) {
    }

    public void deleteAppointment(ActionEvent actionEvent) {
    }
}
