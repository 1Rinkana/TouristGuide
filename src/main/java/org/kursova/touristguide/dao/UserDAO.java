package org.kursova.touristguide.dao;

import org.kursova.touristguide.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection;

    /**
     * Constructor for UserDAO.
     *
     * @param connection the database connection
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a user by their username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the user if found, otherwise null
     */
    public User getUserByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM tourist WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds a new user to the database.
     *
     * @param user the user to be added
     * @return true if the user was added successfully, otherwise false
     */
    public boolean addUser(User user) {
        String query = "INSERT INTO tourist (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
