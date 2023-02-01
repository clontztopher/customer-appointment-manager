package clontz.clientschedulingapp.DataService;

import clontz.clientschedulingapp.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    private final Connection connection;

    private static final String FIND_ID = "SELECT User_ID, User_Name FROM users WHERE User_Name = ? AND Password = ?";

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User validateLogin(String username, String password) {
        try (PreparedStatement prepState = connection.prepareStatement(FIND_ID)) {
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
}
