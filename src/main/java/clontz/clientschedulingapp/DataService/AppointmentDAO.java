package clontz.clientschedulingapp.DataService;

import clontz.clientschedulingapp.Models.Appointment;
import clontz.clientschedulingapp.Models.Contact;
import clontz.clientschedulingapp.Models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private final Connection connection;

    private final String FIND_ALL = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments";

    public AppointmentDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Appointment> findAll() {
        List<Appointment> allAppointments = new ArrayList<>();
        ContactDAO contactDAO = new ContactDAO(connection);
        CustomerDAO customerDAO = new CustomerDAO(connection);

        try (PreparedStatement prepState = connection.prepareStatement(FIND_ALL)) {
            ResultSet rs = prepState.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment();
                Contact contact = contactDAO.findById(rs.getInt("Contact_ID"));
                Customer customer = customerDAO.findById(rs.getInt("Customer_ID"));

                appointment.setId(rs.getInt("Appointment_ID"));
                appointment.setTitle(rs.getString("Title"));
                appointment.setDescription(rs.getString("Description"));
                appointment.setLocation(rs.getString("Location"));
                appointment.setType(rs.getString("Type"));
                appointment.setStart(rs.getTimestamp("Start").toLocalDateTime());
                appointment.setEnd(rs.getTimestamp("End").toLocalDateTime());
                appointment.setUserId(rs.getInt("User_ID"));
                appointment.setCustomer(customer);
                appointment.setContact(contact);
                allAppointments.add(appointment);
            }
            return allAppointments;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
