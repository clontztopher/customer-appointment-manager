package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.AppointmentDAO;
import clontz.clientschedulingapp.DataService.DBConnector;
import clontz.clientschedulingapp.Helpers.Session;
import clontz.clientschedulingapp.Models.Appointment;
import clontz.clientschedulingapp.Models.Contact;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {
    @FXML
    public Pane navMenu;
    @FXML
    public Pane notificationPane;
    @FXML
    public Text notificationText;
    @FXML
    public TabPane reportTabs;
    public Tab apptCountsTab;
    public Tab contactScheduleTab;
    public Tab myAppointmentsTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkForUpcomingAppointment();

        FXMLLoader apptCountReportLoader = new FXMLLoader();
        FXMLLoader contactScheduleReportLoader = new FXMLLoader();
        FXMLLoader myApptReportLoader = new FXMLLoader();
        try {
            HBox apptCountReportView = apptCountReportLoader.load(getClass().getResource("appointments-count-report.fxml"));
            apptCountsTab.setContent(apptCountReportView);

            VBox contactScheduleReportView = contactScheduleReportLoader.load(getClass().getResource("contact-schedule-report.fxml"));
            contactScheduleTab.setContent(contactScheduleReportView);

            VBox myApptReportView = myApptReportLoader.load(getClass().getResource("my-appointments-report.fxml"));
            myAppointmentsTab.setContent(myApptReportView);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void checkForUpcomingAppointment() {
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);
        List<Appointment> appointments = appointmentDAO.findAll();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fifteenMinutes = now.plusMinutes(15);
        Appointment nearestWithinFifteenMin = null;

        for (Appointment appointment : appointments) {
            if (appointment.getUser().getId() != Session.getUser().getId()) {
                // Only looking for appointments associated with current user
                continue;
            }
            if (appointment.getStart().isBefore(now) || appointment.getStart().isAfter(fifteenMinutes)) {
                // Appointment doesn't start within fifteen minutes
                continue;
            }
            if (nearestWithinFifteenMin == null || nearestWithinFifteenMin.getStart().isAfter(appointment.getStart())) {
                // Replace saved appointment if the current one is also within fifteen minutes but even closer than the current
                nearestWithinFifteenMin = appointment;
            }
        }

        if (nearestWithinFifteenMin != null) {
            notificationPane.setStyle("-fx-background-color: lightgreen;");
            notificationText.setText("Appointment ID: " + nearestWithinFifteenMin.getId() + ", is starting soon at: " + nearestWithinFifteenMin.getStartFormatted() + ".");
        } else {
            notificationPane.setStyle("-fx-background-color: lightblue;");
            notificationText.setText("There are no upcoming appointments.");
        }
    }
}
