package clontz.clientschedulingapp.DataService;

import clontz.clientschedulingapp.Models.Country;
import clontz.clientschedulingapp.Models.Customer;
import clontz.clientschedulingapp.Models.FirstLevelDivision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private final Connection connection;
    private static final String INSERT = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
    private static final String FIND_ID = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM customers WHERE Customer_ID = ?";
    private static final String FIND_ALL = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM customers";
    private static final String DELETE = "DELETE FROM customers WHERE Customer_ID = ?";

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    public Customer findById(int id) {
        Customer customer = new Customer();
        try (PreparedStatement prepState = this.connection.prepareStatement(FIND_ID);) {
           prepState.setInt(1, id);
           ResultSet resultSet = prepState.executeQuery();
           while (resultSet.next()) {
               customer.setId(resultSet.getInt("Customer_ID"));
               customer.setName(resultSet.getString("Customer_Name"));
               customer.setAddress(resultSet.getString("Address"));
               customer.setPostalCode(resultSet.getString("Postal_Code"));
               customer.setPhone(resultSet.getString("Phone"));

               FirstLevelDivision division = getDivision(resultSet.getInt("Division_ID"));
               customer.setDivision(division);

               Country country = getCountry(division.getCountryId());
               customer.setCountry(country);
           }
           return customer;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try (PreparedStatement prepState = connection.prepareStatement(FIND_ALL);) {
            ResultSet rs = prepState.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("Customer_ID"));
                customer.setName(rs.getString("Customer_Name"));
                customer.setAddress(rs.getString("Address"));
                customer.setPostalCode(rs.getString("Postal_Code"));
                customer.setPhone(rs.getString("Phone"));

                FirstLevelDivision division = getDivision(rs.getInt("Division_ID"));
                customer.setDivision(division);

                Country country = getCountry(division.getCountryId());
                customer.setCountry(country);

                customers.add(customer);
            }
            return customers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(Customer customer) {
        try (PreparedStatement prepState = connection.prepareStatement(UPDATE);) {
            prepState.setString(1, customer.getName());
            prepState.setString(2, customer.getAddress());
            prepState.setString(3, customer.getPostalCode());
            prepState.setString(4, customer.getPhone());
            prepState.setInt(5, customer.getDivision().getId());
            prepState.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Customer create(Customer customer) {
        try (PreparedStatement prepState = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            prepState.setString(1, customer.getName());
            prepState.setString(2, customer.getAddress());
            prepState.setString(3, customer.getPostalCode());
            prepState.setString(4, customer.getPhone());
            prepState.setInt(5, customer.getDivision().getId());

            prepState.executeUpdate();
            ResultSet rs = prepState.getGeneratedKeys();

            if (!rs.next() || rs.getInt(1) < 1) {
                throw new RuntimeException("User could not be created.");
            }

            customer.setId(rs.getInt(1));
            return customer;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Customer customer) {
        try (PreparedStatement prepState = connection.prepareStatement(DELETE)) {
            prepState.setInt(1, customer.getId());
            prepState.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private FirstLevelDivision getDivision(int division_id) {
        FirstLevelDivisionDAO fldDAO = new FirstLevelDivisionDAO(this.connection);
        return fldDAO.findById(division_id);
    }

    private Country getCountry(int country_id) {
        CountryDAO countryDAO = new CountryDAO(this.connection);
        return countryDAO.findById(country_id);
    }
}
