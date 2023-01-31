package clontz.clientschedulingapp.Controllers;

import clontz.clientschedulingapp.DataService.CountryDAO;
import clontz.clientschedulingapp.DataService.CustomerDAO;
import clontz.clientschedulingapp.DataService.DBConnector;
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

    public void saveChanges(ActionEvent actionEvent) {
    }

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
            if (selectedCustomer != null) {
                customerIDField.setText(String.valueOf(selectedCustomer.getId()));
                nameInput.setText(selectedCustomer.getName());
                addressInput.setText(selectedCustomer.getAddress());
                postalCodeInput.setText(selectedCustomer.getPostalCode());
                phoneInput.setText(selectedCustomer.getPhone());
            }
        });

        // Initialize form
        CountryDAO countryDAO = new CountryDAO(DBConnector.connection);
        List<Country> countryList = countryDAO.findAll();
        countryComboBox.getItems().addAll(countryList);
    }

    public void addNewCustomer(ActionEvent actionEvent) {
        customerTable.getSelectionModel().clearSelection();

        customerIDField.setText("Unavailable");
        nameInput.setText("");
        addressInput.setText("");
        postalCodeInput.setText("");
        phoneInput.setText("");
    }

    public void deleteCustomer(ActionEvent actionEvent) {
    }
}
