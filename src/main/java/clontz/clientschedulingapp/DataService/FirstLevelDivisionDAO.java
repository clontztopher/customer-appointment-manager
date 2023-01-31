package clontz.clientschedulingapp.DataService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import clontz.clientschedulingapp.Models.FirstLevelDivision;

public class FirstLevelDivisionDAO {
    private final Connection connection;

    private static final String FIND_ID = "SELECT Division_ID, Division, COUNTRY_ID FROM first_level_divisions WHERE Division_ID = ?";

    public FirstLevelDivisionDAO(Connection connection) {
        this.connection = connection;
    }

    public FirstLevelDivision findById(int id) {
        FirstLevelDivision division = new FirstLevelDivision();
        try (PreparedStatement prepState = this.connection.prepareStatement(FIND_ID)) {
            prepState.setInt(1, id);
            ResultSet rs = prepState.executeQuery();
            while(rs.next()) {
                division.setId(rs.getInt("Division_ID"));
                division.setName(rs.getString("Division"));
                division.setCountryId(rs.getInt("COUNTRY_ID"));
            }
            return division;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

