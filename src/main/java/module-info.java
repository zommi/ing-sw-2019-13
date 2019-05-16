open module project {
    requires javafx.controls;
    requires javafx.fxml;
    requires jackson.annotations;
    requires java.rmi;
    requires com.fasterxml.jackson.databind;

    exports client.gui to javafx.graphics, javafx.fxml;
}

