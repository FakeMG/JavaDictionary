module groupId.JavaDictionary {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires jsapi;


    opens groupId.JavaFX to javafx.fxml;
    exports groupId.JavaFX;
}