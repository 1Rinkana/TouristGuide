module org.kursova.touristguide {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.kursova.touristguide to javafx.fxml;
    exports org.kursova.touristguide;
    exports org.kursova.touristguide.controller;
    opens org.kursova.touristguide.controller to javafx.fxml;
}