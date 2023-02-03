package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.AppointmentDAO;
import clontz.clientschedulingapp.DataService.ContactDAO;
import clontz.clientschedulingapp.DataService.CustomerDAO;
import clontz.clientschedulingapp.DataService.DBConnector;
import clontz.clientschedulingapp.Helpers.Session;
import clontz.clientschedulingapp.Models.Appointment;
import clontz.clientschedulingapp.Models.Contact;
import clontz.clientschedulingapp.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ManageAppointmentController implements Initializable {
    public Pane navMenu;
    public Text appointmentIDField;
    public TextField titleInput;
    public TextField locationInput;
    public TextArea descriptionTextBox;
    public ComboBox<Contact> contactBox;
    public ComboBox<Customer> customerBox;
    public DatePicker startDateSelect;
    public DatePicker endDateSelect;
    public TextField typeInput;
    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();
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
    public TextField startTimeField;
    public TextField endTimeField;



    public void saveChanges(ActionEvent actionEvent) {
        String title = titleInput.getText();
        String location = locationInput.getText();
        String description = descriptionTextBox.getText();
        String type = typeInput.getText();
        Contact contact = contactBox.getValue();
        Customer customer = customerBox.getValue();
        LocalDate startDate = startDateSelect.getValue();
        String startTimeText = startTimeField.getText();
        LocalDate endDate = endDateSelect.getValue();
        String endTimeText = endTimeField.getText();
        int user_id = Session.getUser().getId();


    }

    public void clearAppointmentForm(ActionEvent actionEvent) {
        appointmentTable.getSelectionModel().clearSelection();

        appointmentIDField.setText("Unavailable");
        titleInput.setText("");
        locationInput.setText("");
        descriptionTextBox.setText("");

        contactBox.getSelectionModel().clearSelection();
        contactBox.setValue(null);

        customerBox.getSelectionModel().clearSelection();
        customerBox.setValue(null);

        startDateSelect.setValue(null);
        startTimeField.setText("");
        endDateSelect.setValue(null);
        endTimeField.setText("");
        typeInput.setText("");

    }

    public void deleteAppointment(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Contacts
        ContactDAO contactDAO = new ContactDAO(DBConnector.connection);
        List<Contact> contactsList = contactDAO.findAll();
        ObservableList<Contact> contactsComboList = FXCollections.observableArrayList();
        contactsComboList.addAll(contactsList);
        contactBox.setItems(contactsComboList);

        // Customers
        CustomerDAO customerDAO = new CustomerDAO(DBConnector.connection);
        List<Customer> customerList = customerDAO.findAll();
        ObservableList<Customer> customerComboList = FXCollections.observableArrayList();
        customerComboList.addAll(customerList);
        customerBox.setItems(customerComboList);

        // Appointment Table
        setUpAppointmentTable();
    }

    public void setUpAppointmentTable() {
        appointmentTable.setItems(appointments);
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("UserId"));

        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);
        List<Appointment> xAppointments = appointmentDAO.findAll();

        appointments.addAll(xAppointments);

        appointmentTable.getSelectionModel().selectedItemProperty().addListener(e -> {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                return;
            }

            appointmentIDField.setText(String.valueOf(selectedAppointment.getId()));
            titleInput.setText(selectedAppointment.getTitle());
            locationInput.setText(selectedAppointment.getLocation());
            descriptionTextBox.setText(selectedAppointment.getDescription());
            contactBox.setValue(selectedAppointment.getContact());
            customerBox.setValue(selectedAppointment.getCustomer());

            // Start/End Times
            LocalDateTime startDateTime = selectedAppointment.getStart();
            LocalDate startDate = startDateTime.toLocalDate();
            LocalTime startTime = startDateTime.toLocalTime();

            LocalDateTime endDateTime = selectedAppointment.getEnd();
            LocalDate endDate = endDateTime.toLocalDate();
            LocalTime endTime = endDateTime.toLocalTime();

            startDateSelect.setValue(startDate);
            startTimeField.setText(startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            endDateSelect.setValue(endDate);
            endTimeField.setText(endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            typeInput.setText(selectedAppointment.getType());
        });
    }
}
