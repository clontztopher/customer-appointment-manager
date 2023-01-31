package clontz.clientschedulingapp.DataService;

import clontz.clientschedulingapp.Models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    private final Connection connection;

    private static final String FIND_ID = "SELECT Country_ID, Country FROM countries WHERE Country_ID = ?";
    private static final String FIND_ALL = "SELECT Country_ID, Country FROM countries";

    public CountryDAO(Connection connection) {
        this.connection = connection;
    }

    public Country findById(int id) {
        Country country = new Country();
        try (PreparedStatement prepState = this.connection.prepareStatement(FIND_ID)) {
            prepState.setInt(1, id);
            ResultSet rs = prepState.executeQuery();
            while (rs.next()) {
                country.setId(rs.getInt("Country_Id"));
                country.setName(rs.getString("Country"));
            }
            return country;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Country> findAll() {
        List<Country> countries = new ArrayList<>();
        try (PreparedStatement prepState = this.connection.prepareStatement(FIND_ALL)) {
            ResultSet rs = prepState.executeQuery();
            while (rs.next()) {
                Country country = new Country();
                country.setId(rs.getInt("Country_Id"));
                country.setName(rs.getString("Country"));
                countries.add(country);
            }
            return countries;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
