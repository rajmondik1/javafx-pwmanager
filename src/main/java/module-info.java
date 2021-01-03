module lt.pwmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson;
    requires java.sql;

    opens lt.pwmanager to javafx.fxml;
    opens lt.pwmanager.controller to javafx.fxml;
    opens lt.pwmanager.model to gson;
    exports lt.pwmanager.model to gson;
    exports lt.pwmanager;
}