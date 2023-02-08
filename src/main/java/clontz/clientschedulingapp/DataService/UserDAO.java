package clontz.clientschedulingapp.DataService;

import clontz.clientschedulingapp.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final Connection connection;

    private static final String FIND_USERPASS = "SELECT User_ID, User_Name FROM users WHERE User_Name = ? AND Password = ?";
    private static final String FIND_ALL = "SELECT User_ID, User_Name FROM users";
    private static final String FIND_ID = "SELECT User_ID, User_Name FROM users WHERE User_ID = ?";

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User validateLogin(String username, String password) {
        try (PreparedStatement prepState = connection.prepareStatement(FIND_USERPASS)) {
            User user = new User();
            prepState.setString(1, username);
            prepState.setString(2, password);
            ResultSet rs = prepState.executeQuery();
            if (!rs.next()) {
                return null;
            }
            user.setId(rs.getInt("User_ID"));
            user.setUsername(rs.getString("User_Name"));
            return user;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement prepState = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = prepState.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("User_Name"));
                user.setId(resultSet.getInt("User_ID"));
                users.add(user);
            }
            return users;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public User findById(int id) {
        try (PreparedStatement prepState = connection.prepareStatement(FIND_ID)) {
            User user = new User();
            prepState.setInt(1, id);
            ResultSet rs = prepState.executeQuery();

            if (rs.next()) {
                user.setId(rs.getInt("User_ID"));
                user.setUsername(rs.getString("User_Name"));
            }
            return user;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
