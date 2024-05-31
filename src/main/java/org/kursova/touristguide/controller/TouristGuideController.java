package org.kursova.touristguide.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.scene.control.CheckBox;
import org.kursova.touristguide.TouristGuideApp;
import org.kursova.touristguide.data.Note;
import org.kursova.touristguide.data.Contact;
import org.kursova.touristguide.dao.NoteDAO;
import org.kursova.touristguide.dao.ContactDAO;
import org.kursova.touristguide.data.User;
import org.kursova.touristguide.db.ConnectDB;

import java.sql.Connection;
import java.util.List;

public class TouristGuideController {

    @FXML
    private ListView<Note> notesListView;  // ListView to display notes

    @FXML
    private TextField titleField;  // TextField for note title

    @FXML
    private TextArea descriptionField;  // TextArea for note description

    @FXML
    private TextField addressField;  // TextField for note address

    @FXML
    private TextField ratingField;  // TextField for note rating

    @FXML
    private TextArea contactsField;  // TextArea for note contacts

    @FXML
    private CheckBox checkBox;  // CheckBox for marking a note as top note

    private NoteDAO noteDAO;  // Data access object for notes
    private ContactDAO contactDAO;  // Data access object for contacts

    private User currentUser = TouristGuideApp.getCurrentUser();  // Current logged-in user

    public TouristGuideController() {
        // Establish database connection and initialize DAOs
        ConnectDB connectDB = new ConnectDB();
        Connection connection = connectDB.getConnection();
        contactDAO = new ContactDAO(connection);
        noteDAO = new NoteDAO(connection, contactDAO);
    }

    @FXML
    private void initialize() {
        // Set up the ListView and load initial notes
        setupListView();
        loadNotes();
    }

    @FXML
    private void handleAddNote() {
        // Gather data from input fields and create a new note
        String title = titleField.getText();
        String description = descriptionField.getText();
        String address = addressField.getText();
        int rating = Integer.parseInt(ratingField.getText());
        String contactsText = contactsField.getText();

        Note note = new Note();
        note.setUserId(currentUser.getId());
        note.setTitle(title);
        note.setDescription(description);
        note.setAddress(address);
        note.setRating(rating);
        note.setTopNote(checkBox.isSelected());

        noteDAO.addNote(note);

        // Add contacts to the note if provided
        if (!contactsText.trim().isEmpty()) {
            for (String contactInfo : contactsText.split("\n")) {
                String[] parts = contactInfo.split(",");
                Contact contact = new Contact();
                contact.setNoteId(note.getId());
                contact.setName(parts[0].trim());
                if (parts.length > 1) contact.setPhoneNumber(parts[1].trim());
                if (parts.length > 2) contact.setPosition(parts[2].trim());

                contactDAO.addContact(contact);
            }
        }

        // Refresh the notes list and clear input fields
        loadNotes();
        clearFields();
    }

    @FXML
    private void handleEditNote() {
        // Get the selected note and update its details
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            selectedNote.setTitle(titleField.getText());
            selectedNote.setDescription(descriptionField.getText());
            selectedNote.setAddress(addressField.getText());
            selectedNote.setRating(Integer.parseInt(ratingField.getText()));
            selectedNote.setTopNote(checkBox.isSelected());

            noteDAO.updateNote(selectedNote);

            // Update contacts related to the note
            contactDAO.deleteContactsByNoteId(selectedNote.getId());

            String contactsText = contactsField.getText();
            for (String contactInfo : contactsText.split("\n")) {
                String[] parts = contactInfo.split(",");
                Contact contact = new Contact();
                contact.setNoteId(selectedNote.getId());
                contact.setName(parts[0].trim());
                if (parts.length > 1) contact.setPhoneNumber(parts[1].trim());
                if (parts.length > 2) contact.setPosition(parts[2].trim());
                contactDAO.addContact(contact);
            }

            // Refresh the notes list and clear input fields
            loadNotes();
            clearFields();
        }
    }

    @FXML
    private void handleDeleteNote() {
        // Delete the selected note and its associated contacts
        Note selectedNote = notesListView.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            noteDAO.deleteNoteById(selectedNote.getId());
            contactDAO.deleteContactsByNoteId(selectedNote.getId());
            loadNotes();
        }
        clearFields();
    }

    @FXML
    private void handleShowTopNotes() {
        // Display only the top notes
        List<Note> topNotes = noteDAO.getTopNotes();
        notesListView.getItems().setAll(topNotes);
        clearFields();
    }

    @FXML
    private void handleShowNotes() {
        // Display all notes
        loadNotes();
        clearFields();
    }

    private void clearFields() {
        // Clear all input fields
        titleField.clear();
        descriptionField.clear();
        addressField.clear();
        ratingField.clear();
        contactsField.clear();
        checkBox.setSelected(false);
    }

    private void loadNotes() {
        // Load all notes for the current user and their associated contacts
        List<Note> notes = noteDAO.getAllNotes(currentUser.getId());
        for (Note note : notes) {
            List<Contact> contacts = contactDAO.getContactsByNoteId(note.getId());
            note.setContacts(contacts);
        }
        notesListView.getItems().setAll(notes);
    }

    private void setupListView() {
        // Setup custom cell factory for ListView to display note titles
        notesListView.setCellFactory(new Callback<ListView<Note>, ListCell<Note>>() {
            @Override
            public ListCell<Note> call(ListView<Note> listView) {
                return new ListCell<Note>() {
                    @Override
                    protected void updateItem(Note note, boolean empty) {
                        super.updateItem(note, empty);
                        if (note == null || empty) {
                            setText(null);
                        } else {
                            setText(note.getTitle());
                        }
                    }
                };
            }
        });

        // Add listener to update input fields when a note is selected
        notesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                titleField.setText(newValue.getTitle());
                descriptionField.setText(newValue.getDescription());
                addressField.setText(newValue.getAddress());
                ratingField.setText(String.valueOf(newValue.getRating()));
                StringBuilder contactsTextBuilder = new StringBuilder();

                for (Contact contact : newValue.getContacts()) {
                    contactsTextBuilder.append(contact.getName())
                            .append(", ")
                            .append(contact.getPhoneNumber())
                            .append(", ")
                            .append(contact.getPosition())
                            .append("\n");
                }

                String contactsText = contactsTextBuilder.toString();
                contactsField.setText(contactsText);
                checkBox.setSelected(newValue.isTopNote());
            }
        });
    }
}
