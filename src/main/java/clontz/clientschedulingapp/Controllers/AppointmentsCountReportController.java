package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.AppointmentDAO;
import clontz.clientschedulingapp.DataService.DBConnector;
import clontz.clientschedulingapp.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentsCountReportController implements Initializable {
    public ComboBox<String> typeBox;
    public ComboBox<Month> monthBox;
    public Text appointmentCount;
    List<Appointment> appointments = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);
        appointments = appointmentDAO.findAll();
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
        for (Appointment appointment : appointments) {
            if (!appointmentTypes.contains(appointment.getType())) {
                appointmentTypes.add(appointment.getType());
            }
        }
        typeBox.setItems(appointmentTypes);

        ObservableList<Month> months = FXCollections.observableArrayList();
        months.addAll(Month.values());
        monthBox.setItems(months);
    }

    /**
     * Uses streams and lambdas to reduce selected appointment attributes down to a number
     * @param actionEvent
     */
    public void handleReportSelection(ActionEvent actionEvent) {
        long count = appointments.stream()
                .filter(appointment -> appointment.getType().equals(typeBox.getValue()))
                .filter(appointment -> appointment.getStart().getMonth().equals(monthBox.getValue()))
                .count();
        appointmentCount.setText(String.valueOf(count));
    }
}
