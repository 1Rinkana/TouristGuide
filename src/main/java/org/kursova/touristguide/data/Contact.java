package org.kursova.touristguide.data;

public class Contact {
    private Long id;
    private Long noteId;
    private String name;
    private String phoneNumber;
    private String position;

    public Contact() {}

    public Contact(Long id, Long noteId, String name, String phoneNumber, String position) {
        this.id = id;
        this.noteId = noteId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.position = position;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getNoteId() { return noteId; }
    public void setNoteId(Long noteId) { this.noteId = noteId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}
