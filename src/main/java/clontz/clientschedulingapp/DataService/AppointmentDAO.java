package clontz.clientschedulingapp.DataService;

import clontz.clientschedulingapp.Models.Appointment;
import clontz.clientschedulingapp.Models.Contact;
import clontz.clientschedulingapp.Models.Customer;
import clontz.clientschedulingapp.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private final Connection connection;
    private static final String FIND_ALL = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments";
    private static final String FIND_BY_CUSTOMER = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments WHERE Customer_ID = ?";
    private static final String INSERT = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
    private static final String DELETE = "DELETE FROM appointments WHERE Appointment_ID = ?";
    private static final String DELETE_CUSTOMER_APPOINTMENTS = "DELETE FROM appointments WHERE Customer_ID = ?";

    public AppointmentDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Appointment> findAll() {
        List<Appointment> allAppointments = new ArrayList<>();
        ContactDAO contactDAO = new ContactDAO(connection);
        CustomerDAO customerDAO = new CustomerDAO(connection);
        UserDAO userDAO = new UserDAO(connection);

        try (PreparedStatement prepState = connection.prepareStatement(FIND_ALL)) {
            ResultSet rs = prepState.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment();
                Contact contact = contactDAO.findById(rs.getInt("Contact_ID"));
                Customer customer = customerDAO.findById(rs.getInt("Customer_ID"));
                User user = userDAO.findById(rs.getInt("User_ID"));

                appointment.setId(rs.getInt("Appointment_ID"));
                appointment.setTitle(rs.getString("Title"));
                appointment.setDescription(rs.getString("Description"));
                appointment.setLocation(rs.getString("Location"));
                appointment.setType(rs.getString("Type"));
                appointment.setStart(rs.getTimestamp("Start").toLocalDateTime());
                appointment.setEnd(rs.getTimestamp("End").toLocalDateTime());
                appointment.setUser(user);
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

    public List<Appointment> findByCustomer(int customer_id) {
        List<Appointment> appointments = new ArrayList<>();
        ContactDAO contactDAO = new ContactDAO(connection);
        CustomerDAO customerDAO = new CustomerDAO(connection);
        UserDAO userDAO = new UserDAO(connection);

        try (PreparedStatement prepState = connection.prepareStatement(FIND_BY_CUSTOMER)) {
            prepState.setInt(1, customer_id);
            ResultSet rs = prepState.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();
                Contact contact = contactDAO.findById(rs.getInt("Contact_ID"));
                Customer customer = customerDAO.findById(rs.getInt("Customer_ID"));
                User user = userDAO.findById(rs.getInt("User_ID"));

                appointment.setId(rs.getInt("Appointment_ID"));
                appointment.setTitle(rs.getString("Title"));
                appointment.setDescription(rs.getString("Description"));
                appointment.setLocation(rs.getString("Location"));
                appointment.setType(rs.getString("Type"));
                appointment.setStart(rs.getTimestamp("Start").toLocalDateTime());
                appointment.setEnd(rs.getTimestamp("End").toLocalDateTime());
                appointment.setUser(user);
                appointment.setCustomer(customer);
                appointment.setContact(contact);
                appointments.add(appointment);
            }
            return appointments;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int create(Appointment appointment) {
        try (PreparedStatement prepState = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            prepState.setString(1, appointment.getTitle());
            prepState.setString(2, appointment.getDescription());
            prepState.setString(3, appointment.getLocation());
            prepState.setString(4, appointment.getType());
            prepState.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            prepState.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            prepState.setInt(7, appointment.getCustomer().getId());
            prepState.setInt(8, appointment.getUser().getId());
            prepState.setInt(9, appointment.getContact().getId());

            prepState.executeUpdate();
            ResultSet rs = prepState.getGeneratedKeys();

            if (!rs.next() || rs.getInt(1) < 1) {
                throw new RuntimeException("Appointment could not be created at this time.");
            }

            return rs.getInt(1);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update(Appointment appointment) {
        try (PreparedStatement prepState = connection.prepareStatement(UPDATE)) {
            prepState.setString(1, appointment.getTitle());
            prepState.setString(2, appointment.getDescription());
            prepState.setString(3, appointment.getLocation());
            prepState.setString(4, appointment.getType());
            prepState.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            prepState.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            prepState.setInt(7, appointment.getCustomer().getId());
            prepState.setInt(8, appointment.getUser().getId());
            prepState.setInt(9, appointment.getContact().getId());
            prepState.setInt(10, appointment.getId());
            prepState.execute();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void delete(Appointment appointment) {
        try (PreparedStatement prepState = connection.prepareStatement(DELETE)) {
            prepState.setInt(1, appointment.getId());
            prepState.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteForCustomer(int customer_id) throws RuntimeException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_APPOINTMENTS)) {
            preparedStatement.setInt(1, customer_id);
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
