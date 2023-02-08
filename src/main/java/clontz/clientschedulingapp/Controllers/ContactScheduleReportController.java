package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.AppointmentDAO;
import clontz.clientschedulingapp.DataService.ContactDAO;
import clontz.clientschedulingapp.DataService.DBConnector;
import clontz.clientschedulingapp.Models.Appointment;
import clontz.clientschedulingapp.Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ContactScheduleReportController implements Initializable {
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public ComboBox<Contact> contactBox;
    public TableView<Appointment> appointmentTable;
    public TableColumn<String, Integer> idCol;
    public TableColumn<String, String> descCol;
    public TableColumn<String, String> locationCol;
    public TableColumn<String, String> contactCol;
    public TableColumn<String, String> typeCol;
    public TableColumn<String, LocalDateTime> startCol;
    public TableColumn<String, LocalDateTime> endCol;
    public TableColumn<String, Integer> customerCol;
    public TableColumn<String, Integer> userCol;

    public void handleContactSelect(ActionEvent actionEvent) {
        FilteredList<Appointment> filteredAppointments = appointments.filtered(
                appointment -> appointment.getContact().getId() == contactBox.getValue().getId()
        );
        appointmentTable.setItems(filteredAppointments);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up contacts combo box
        ContactDAO contactDAO = new ContactDAO(DBConnector.connection);
        List<Contact> contacts = contactDAO.findAll();
        ObservableList<Contact> contactsSelectList = FXCollections.observableArrayList();
        contactsSelectList.addAll(contacts);
        contactBox.setItems(contactsSelectList);

        // Fetch appointments
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);
        List<Appointment> appointmentsList = appointmentDAO.findAll();
        appointments.addAll(appointmentsList);

        // Set up appointments table
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("StartFormatted"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("EndFormatted"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("UserId"));
    }
}
