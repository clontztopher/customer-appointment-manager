package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.*;
import clontz.clientschedulingapp.Models.Country;
import clontz.clientschedulingapp.Models.Customer;
import clontz.clientschedulingapp.Models.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageCustomerController implements Initializable {
    public TableColumn<String, Integer> idCol;
    public TableColumn<String, String> nameCol;
    public TableColumn<String, String> addressCol;
    public TableColumn<String, String> postalCodeCol;
    public TableColumn<String, String> phoneCol;
    public TableColumn<String, String> divisionCol;
    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public Pane navMenu;
    public TextField nameInput;
    public TextField addressInput;
    public TextField postalCodeInput;
    public TextField phoneInput;
    public ComboBox<Country> countryComboBox;
    public ComboBox<FirstLevelDivision> divisionComboBox;
    public Text customerIDField;
    public TableView<Customer> customerTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up customer TableView
        customerTable.setItems(customerList);

        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("DivisionName"));

        CustomerDAO customerDAO = new CustomerDAO(DBConnector.connection);
        List<Customer> customers = customerDAO.findAll();

        customerList.addAll(customers);

        // Selection handler with lambda callback
        customerTable.getSelectionModel().selectedItemProperty().addListener(selected -> {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {
                return;
            }

            customerIDField.setText(String.valueOf(selectedCustomer.getId()));
            nameInput.setText(selectedCustomer.getName());
            addressInput.setText(selectedCustomer.getAddress());
            postalCodeInput.setText(selectedCustomer.getPostalCode());
            phoneInput.setText(selectedCustomer.getPhone());
            countryComboBox.setValue(selectedCustomer.getCountry());
            divisionComboBox.setValue(selectedCustomer.getDivision());
        });

        // Initialize form
        CountryDAO countryDAO = new CountryDAO(DBConnector.connection);
        List<Country> countryList = countryDAO.findAll();
        ObservableList<Country> comboList = FXCollections.observableArrayList();
        comboList.addAll(countryList);
        countryComboBox.setItems(comboList);
        countryComboBox.setPromptText("Choose Country...");
        divisionComboBox.setPromptText("Division...");

        countryComboBox.setOnAction(event -> {
            Country country = countryComboBox.getSelectionModel().getSelectedItem();

            if (country == null) {
                return;
            }

            FirstLevelDivisionDAO fldDAO = new FirstLevelDivisionDAO(DBConnector.connection);
            List<FirstLevelDivision> fldList = fldDAO.findByCountryId(country.getId());
            ObservableList<FirstLevelDivision> divisionComboList = FXCollections.observableArrayList();
            divisionComboList.addAll(fldList);

            divisionComboBox.setItems(divisionComboList);
        });
    }

    public void saveChanges(ActionEvent actionEvent) {
        String name = nameInput.getText();
        String address = addressInput.getText();
        String postal_code = postalCodeInput.getText();
        String phone = phoneInput.getText();
        Country country = countryComboBox.getSelectionModel().getSelectedItem();
        FirstLevelDivision firstLevelDivision = divisionComboBox.getSelectionModel().getSelectedItem();

        if (
                name.equals("")
                || address.equals("")
                || postal_code.equals("")
                || phone.equals("")
                || (country == null)
                || (firstLevelDivision == null)
        ) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be completed.");
            alert.showAndWait();
            return;
        }

        CustomerDAO customerDAO = new CustomerDAO(DBConnector.connection);
        Customer customer = customerTable.getSelectionModel().getSelectedItem();

        if (customer == null) {
            customer = new Customer();
        }

        customer.setName(name);
        customer.setAddress(address);
        customer.setPostalCode(postal_code);
        customer.setPhone(phone);
        customer.setCountry(country);
        customer.setDivision(firstLevelDivision);
        if (customerIDField.getText().equals("Unavailable")) {
            int customerId = customerDAO.create(customer);
            customer.setId(customerId);
            customerList.add(customer);
            customerIDField.setText(String.valueOf(customerId));
        } else {
            customerDAO.update(customer);
            Customer finalCustomer = customer;
            customerList.setAll(customerList.stream().map(c -> {
                if (c.getId() == finalCustomer.getId()) {
                    return finalCustomer;
                }
                return c;
            }).toList());
        }
    }

    public void addNewCustomer(ActionEvent actionEvent) {
        customerTable.getSelectionModel().clearSelection();

        customerIDField.setText("Unavailable");
        nameInput.setText("");
        addressInput.setText("");
        postalCodeInput.setText("");
        phoneInput.setText("");

        divisionComboBox.getSelectionModel().clearSelection();
        divisionComboBox.setValue(null);

        countryComboBox.getSelectionModel().clearSelection();
        countryComboBox.setValue(null);
    }

    public void deleteCustomer(ActionEvent actionEvent) {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a customer from the table to delete their information.");
            alert.showAndWait();
            return;
        }

        CustomerDAO customerDAO = new CustomerDAO(DBConnector.connection);
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnector.connection);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this customer from the database? Deleting a customer will also cancel all of their appointments.");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        appointmentDAO.deleteForCustomer(customer.getId());
                    } catch (Exception e) {
                        Alert warning = new Alert(Alert.AlertType.ERROR, "There was an error while trying to remove appointments for this customer. Please try again or contact support for assistance.");
                        warning.showAndWait();
                        return;
                    }
                    customerDAO.delete(customer);
                    customerList.remove(customer);
                });
    }
}
