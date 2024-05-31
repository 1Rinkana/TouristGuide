package org.kursova.touristguide.dao;

import org.kursova.touristguide.data.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {
    private Connection connection;
    private ContactDAO contactDAO;

    /**
     * Constructor for NoteDAO.
     *
     * @param connection  the database connection
     * @param contactDAO  the DAO for contact-related operations
     */
    public NoteDAO(Connection connection, ContactDAO contactDAO) {
        this.connection = connection;
        this.contactDAO = contactDAO;
    }

    /**
     * Retrieves all notes for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of notes associated with the user
     */
    public List<Note> getAllNotes(Long userId) {
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM note WHERE userId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Note note = new Note();
                note.setId(resultSet.getLong("id"));
                note.setUserId(resultSet.getLong("userId"));
                note.setTitle(resultSet.getString("title"));
                note.setDescription(resultSet.getString("description"));
                note.setAddress(resultSet.getString("address"));
                note.setTopNote(resultSet.getBoolean("isFavorite"));
                note.setRating(resultSet.getInt("rating"));
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    /**
     * Retrieves all notes marked as top notes.
     *
     * @return a list of top notes
     */
    public List<Note> getTopNotes() {
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM note WHERE isFavorite = 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Note note = new Note();
                note.setId(resultSet.getLong("id"));
                note.setUserId(resultSet.getLong("userId"));
                note.setTitle(resultSet.getString("title"));
                note.setDescription(resultSet.getString("description"));
                note.setAddress(resultSet.getString("address"));
                note.setTopNote(resultSet.getBoolean("isFavorite"));
                note.setRating(resultSet.getInt("rating"));
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    /**
     * Adds a new note to the database.
     *
     * @param note the note to be added
     */
    public void addNote(Note note) {
        String query = "INSERT INTO note (userId, title, description, address, isFavorite, rating) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, note.getUserId());
            statement.setString(2, note.getTitle());
            statement.setString(3, note.getDescription());
            statement.setString(4, note.getAddress());
            statement.setBoolean(5, note.isTopNote());
            statement.setInt(6, note.getRating());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                note.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing note in the database.
     *
     * @param note the note to be updated
     */
    public void updateNote(Note note) {
        String query = "UPDATE note SET title = ?, description = ?, address = ?, isFavorite = ?, rating = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, note.getTitle());
            statement.setString(2, note.getDescription());
            statement.setString(3, note.getAddress());
            statement.setBoolean(4, note.isTopNote());
            statement.setInt(5, note.getRating());
            statement.setLong(6, note.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a note from the database by its ID.
     *
     * @param id the ID of the note to be deleted
     */
    public void deleteNoteById(long id) {
        String query = "DELETE FROM note WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
