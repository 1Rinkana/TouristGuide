package org.kursova.touristguide.data;

import java.util.List;

public class Note {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String address;
    private boolean isTopNote;
    private int rating;
    private List<Contact> contacts;

    public Note() {}

    public Note(Long id, Long userId, String title, String description, String address, boolean isTopNote, int rating) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.isTopNote = isTopNote;
        this.rating = rating;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isTopNote() { return isTopNote; }
    public void setTopNote(boolean topNote) { isTopNote = topNote; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public List<Contact> getContacts() { return contacts; }
    public void setContacts(List<Contact> contacts) { this.contacts = contacts; }
}
