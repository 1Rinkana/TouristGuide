package org.kursova.touristguide.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private Connection connection;

    /**
     * Constructor for ConnectDB.
     * Initializes the connection to the SQLite database.
     */
    public ConnectDB() {
        String url = "jdbc:sqlite:touristdb.db";

        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println("Error Connecting to Database");
            e.printStackTrace();
        }
    }

    /**
     * Returns the established database connection.
     *
     * @return the database connection
     */
    public Connection getConnection() { return connection; }

    /**
     * Closes the established database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection Closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
