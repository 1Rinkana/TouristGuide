package org.kursova.touristguide.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.kursova.touristguide.TouristGuideApp;
import org.kursova.touristguide.data.User;
import org.kursova.touristguide.dao.UserDAO;
import org.kursova.touristguide.db.ConnectDB;

import java.io.IOException;
import java.util.Objects;

public class AuthController {

    @FXML
    private TextField usernameField;  // TextField for login username

    @FXML
    private PasswordField passwordField;  // PasswordField for login password

    @FXML
    private TextField regUsernameField;  // TextField for registration username

    @FXML
    private PasswordField regPasswordField;  // PasswordField for registration password

    @FXML
    private PasswordField confirmPasswordField;  // PasswordField for confirming registration password

    private UserDAO userDAO;  // Data access object for user operations

    public AuthController() {
        // Establish database connection and initialize UserDAO
        ConnectDB connectDB = new ConnectDB();
        userDAO = new UserDAO(connectDB.getConnection());
    }

    @FXML
    private void handleLogin() {
        // Handle user login
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userDAO.getUserByUsernameAndPassword(username, password);

        if (user != null) {
            // Login successful
            showAlert("Login successful");
            TouristGuideApp.setCurrentUser(user);
            openTouristGuideApp();
        } else {
            // Login failed
            showAlert("Login failed. Please check your credentials and try again.");
        }
    }

    @FXML
    private void handleRegister() {
        // Handle user registration
        String username = regUsernameField.getText();
        String password = regPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            // Passwords do not match
            showAlert("Passwords do not match");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        if (userDAO.addUser(user)) {
            // Registration successful
            showAlert("Registration successful");
            TouristGuideApp.setCurrentUser(user);
            openTouristGuideApp();
        } else {
            // Registration failed
            showAlert("Registration failed. Please try again.");
        }
    }

    private void openTouristGuideApp() {
        // Open the main Tourist Guide application
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/kursova/touristguide/diary-view.fxml"))));
            stage.setScene(scene);
            stage.setTitle("Tourist Guide");
            stage.show();
            // Close the login/register window
            ((Stage) usernameField.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        // Display an alert with the specified message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
