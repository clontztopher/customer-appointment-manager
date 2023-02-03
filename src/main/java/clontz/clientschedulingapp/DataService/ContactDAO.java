package clontz.clientschedulingapp.DataService;

import clontz.clientschedulingapp.Models.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private final Connection connection;
    private final String FIND_ALL = "SELECT Contact_ID, Contact_Name, Email from contacts";
    private final String FIND_ID = "SELECT Contact_ID, Contact_Name, Email from contacts WHERE Contact_ID = ?";

    public ContactDAO(Connection connection) {
        this.connection = connection;
    }

    public Contact findById(int id) {
        Contact contact = new Contact();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                contact.setId(rs.getInt("Contact_ID"));
                contact.setName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));
            }
            return contact;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Contact> findAll() {
        List<Contact> allContacts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("Contact_ID"));
                contact.setName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));
                allContacts.add(contact);
            }
            return allContacts;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
