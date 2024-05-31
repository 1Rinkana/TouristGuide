package org.kursova.touristguide.dao;

import org.kursova.touristguide.data.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private Connection connection;

    public ContactDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all contacts associated with a specific note ID.
     *
     * @param noteId the ID of the note whose contacts are to be retrieved
     * @return a list of contacts associated with the specified note ID
     */
    public List<Contact> getContactsByNoteId(long noteId) {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM contact WHERE noteId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, noteId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Contact contact = new Contact();
                contact.setId(resultSet.getLong("id"));
                contact.setNoteId(resultSet.getLong("noteId"));
                contact.setName(resultSet.getString("name"));
                contact.setPhoneNumber(resultSet.getString("phoneNumber"));
                contact.setPosition(resultSet.getString("position"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    /**
     * Adds a new contact to the database.
     *
     * @param contact the contact to be added
     */
    public void addContact(Contact contact) {
        String query = "INSERT INTO contact (noteId, name, phoneNumber, position) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, contact.getNoteId());
            statement.setString(2, contact.getName());
            statement.setString(3, contact.getPhoneNumber());
            statement.setString(4, contact.getPosition());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all contacts associated with a specific note ID.
     *
     * @param noteId the ID of the note whose contacts are to be deleted
     */
    public void deleteContactsByNoteId(long noteId) {
        String query = "DELETE FROM contact WHERE noteId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, noteId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
