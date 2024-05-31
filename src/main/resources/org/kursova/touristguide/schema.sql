CREATE TABLE tourist (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE note (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    title TEXT,
    description TEXT,
    address TEXT,
    isFavorite INTEGER,
    rating INTEGER,
    FOREIGN KEY (userId) REFERENCES user(id)
);


CREATE TABLE contact (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    noteId INTEGER NOT NULL,
    name TEXT NOT NULL,
    lastName TEXT,
    phoneNumber TEXT,
    position TEXT,
    FOREIGN KEY (noteId) REFERENCES notes(id)
);
