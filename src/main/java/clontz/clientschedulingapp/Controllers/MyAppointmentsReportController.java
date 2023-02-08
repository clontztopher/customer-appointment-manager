package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.AppointmentDAO;
import clontz.clientschedulingapp.DataService.DBConnector;
import clontz.clientschedulingapp.Helpers.Session;
import clontz.clientschedulingapp.Models.Appointment;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MyAppointmentsReportController implements Initializable {
    public Text myApptCount;
    public Text myApptNextContact;
    public Text myApptNextDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);
        List<Appointment> appointments = appointmentDAO.findAll();

        Long apptCount = appointments.stream()
                .filter(appointment -> appointment.getUser().getId() == Session.getUser().getId())
                .count();
        myApptCount.setText(String.valueOf(apptCount));

        Appointment nextAppointment = null;
        LocalDateTime now = LocalDateTime.now();

        for (Appointment appointment : appointments) {
            // Needs work
            if (nextAppointment == null && appointment.getStart().isAfter(now)) {
                nextAppointment = appointment;
            }
            if (nextAppointment != null && appointment.getStart().isAfter(now) && appointment.getStart().isBefore(nextAppointment.getStart())) {
                nextAppointment = appointment;
            }
        }

        if (nextAppointment != null) {
            myApptNextDate.setText(nextAppointment.getStartFormatted());
            myApptNextContact.setText(nextAppointment.getContact().getName() + ", " + nextAppointment.getContact().getEmail());
            return;
        }

        myApptCount.setText("You have no upcoming appointments.");
    }
}
