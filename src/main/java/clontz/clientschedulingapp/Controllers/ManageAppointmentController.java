package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.*;
import clontz.clientschedulingapp.Models.Appointment;
import clontz.clientschedulingapp.Models.Contact;
import clontz.clientschedulingapp.Models.Customer;
import clontz.clientschedulingapp.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
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
    public ComboBox<User> userBox;
    public DatePicker dateSelect;
    public TextField typeInput;
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
    public RadioButton radioAll;
    public RadioButton radioWeek;
    public RadioButton radioMonth;
    public ToggleGroup appointmentFilterGroup;


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

        // Users
        UserDAO userDAO = new UserDAO(DBConnector.connection);
        List<User> userList = userDAO.findAll();
        ObservableList<User> userComboList = FXCollections.observableArrayList();
        userComboList.addAll(userList);
        userBox.setItems(userComboList);

        // Appointment Table
        setUpAppointmentTable();
        updateTabe();
    }

    public void saveChanges(ActionEvent actionEvent) {
        String title = titleInput.getText();
        String location = locationInput.getText();
        String description = descriptionTextBox.getText();
        String type = typeInput.getText();
        Contact contact = contactBox.getValue();
        Customer customer = customerBox.getValue();
        User user = userBox.getValue();
        LocalDate date = dateSelect.getValue();
        String startTimeText = startTimeField.getText();
        String endTimeText = endTimeField.getText();

        if (
                title.equals("")
                || location.equals("")
                || description.equals("")
                || type.equals("")
                || (contact == null)
                || (customer == null)
                || (date == null)
                || (user == null)
                || startTimeText.equals("")
                || endTimeText.equals("")
        ) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please make sure all fields are completed.");
            alert.showAndWait();
            return;
        }

        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (appointment == null) {
            appointment = new Appointment();
        }

        LocalTime startTime;
        LocalTime endTime;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        // Check valid time format
        try {
            startTime = LocalTime.parse(startTimeText);
            endTime = LocalTime.parse(endTimeText);
            startDateTime = LocalDateTime.of(date, startTime);
            endDateTime = LocalDateTime.of(date, endTime);
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a time format matching 'HH:mm'.");
            alert.showAndWait();
            return;
        }

        if (!checkTimeZoneOfficeHours(startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Appointments must be scheduled within business hours of 8:00 AM and 10:00 PM US/Eastern time.");
            alert.showAndWait();
            return;
        }

        if (startDateTime.isBefore(LocalDateTime.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a future date.");
            alert.showAndWait();
            return;
        }

        if (startDateTime.isAfter(endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "End time must be after start time.");
            alert.showAndWait();
            return;
        }

        if (hasConflicts(appointment.getId(), customer, startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment start/end times conflict with existing appointment. Please choose another time.");
            alert.showAndWait();
            return;
        }

        appointment.setStart(startDateTime);
        appointment.setEnd(endDateTime);
        appointment.setTitle(title);
        appointment.setLocation(location);
        appointment.setDescription(description);
        appointment.setContact(contact);
        appointment.setCustomer(customer);
        appointment.setType(type);
        appointment.setUser(user);

        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);

        if (appointmentIDField.getText().equals("Unavailable")) {
                int appointmentId = appointmentDAO.create(appointment);
                appointmentIDField.setText(String.valueOf(appointmentId));
                updateTabe();
        } else {
            appointmentDAO.update(appointment);
            updateTabe();
        }
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

        userBox.getSelectionModel().clearSelection();
        userBox.setValue(null);

        dateSelect.setValue(null);
        startTimeField.setText("");
        endTimeField.setText("");
        typeInput.setText("");

    }

    public void deleteAppointment(ActionEvent actionEvent) {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (appointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an appointment.");
            alert.showAndWait();
            return;
        }

        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm cancellation of appointment: " + appointment.getId() + ", " + appointment.getType() + ".");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    appointmentDAO.delete(appointment);
                    updateTabe();
                });
    }

    public void setUpAppointmentTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("StartFormatted"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("EndFormatted"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("UserId"));

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
            userBox.setValue(selectedAppointment.getUser());

            // Start/End Times
            LocalDateTime startDateTime = selectedAppointment.getStart();
            LocalDate startDate = startDateTime.toLocalDate();
            LocalTime startTime = startDateTime.toLocalTime();

            LocalDateTime endDateTime = selectedAppointment.getEnd();
            LocalTime endTime = endDateTime.toLocalTime();

            dateSelect.setValue(startDate);
            startTimeField.setText(startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            endTimeField.setText(endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            typeInput.setText(selectedAppointment.getType());
        });
    }

    private boolean checkTimeZoneOfficeHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ZonedDateTime easternOpen = ZonedDateTime.of(
                startDateTime.getYear(),
                startDateTime.getMonthValue(),
                startDateTime.getDayOfMonth(),
                8,
                0,
                0,
                0,
                ZoneId.of("US/Eastern")
        );

        ZonedDateTime easternClose = ZonedDateTime.of(
                startDateTime.getYear(),
                startDateTime.getMonthValue(),
                startDateTime.getDayOfMonth(),
                22,
                0,
                0,
                0,
                ZoneId.of("US/Eastern")
        );

        ZonedDateTime startZoned = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime openInLocalTime = easternOpen.withZoneSameInstant(ZoneId.systemDefault());

        ZonedDateTime endZoned = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime closeInLocalTime = easternClose.withZoneSameInstant(ZoneId.systemDefault());

        return !startZoned.isBefore(openInLocalTime)
                && !startZoned.isAfter(closeInLocalTime)
                && !endZoned.isBefore(openInLocalTime)
                && !endZoned.isAfter(closeInLocalTime);
    }

    private boolean hasConflicts(int appointmentId, Customer customer, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);
        List<Appointment> appointments = appointmentDAO.findByCustomer(customer.getId());

        for (Appointment appointment : appointments) {
            if (appointment.getId() == appointmentId) {
                continue;
            }
            boolean startTimeOverlaps = (startDateTime.isAfter(appointment.getStart()) || startDateTime.isEqual(appointment.getStart())) && startDateTime.isBefore(appointment.getEnd());
            boolean endTimeOverlaps = (endDateTime.isBefore(appointment.getEnd()) || endDateTime.isEqual(appointment.getEnd())) && endDateTime.isAfter(appointment.getStart());
            boolean appointmentOverlaps = startDateTime.isBefore(appointment.getStart()) && endDateTime.isAfter(appointment.getEnd());
            if (startTimeOverlaps || endTimeOverlaps || appointmentOverlaps) {
                return true;
            }
        }
        return false;
    }

    public void filterChange(ActionEvent actionEvent) {
        updateTabe();
    }

    public void updateTabe() {
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);
        List<Appointment> xAppointments = appointmentDAO.findAll();
        ObservableList<Appointment> apptsTableList = FXCollections.observableArrayList();
        apptsTableList.addAll(xAppointments);

        Toggle selected = appointmentFilterGroup.getSelectedToggle();
        if (selected.equals(radioAll)) {
            appointmentTable.setItems(apptsTableList);
            return;
        }
        if (selected.equals(radioWeek)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endOfWeek = now.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            FilteredList<Appointment> filteredList = apptsTableList.filtered(appointment ->
                    appointment.getStart().isAfter(now)
                            && appointment.getStart().isBefore(endOfWeek));
            appointmentTable.setItems(filteredList);
            return;
        }
        if (selected.equals(radioMonth)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextMonth = now.with(TemporalAdjusters.firstDayOfNextMonth());
            FilteredList<Appointment> filteredList = apptsTableList.filtered(appointment ->
                    appointment.getStart().isBefore(nextMonth)
                            && appointment.getStart().isAfter(now));
            appointmentTable.setItems(filteredList);
        }
    }
}
