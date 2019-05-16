module project {
    requires javafx.controls;
    requires javafx.fxml;
    requires jackson.annotations;
    requires jackson.databind;
    requires java.rmi;

    opens client.gui to javafx.fxml;
    exports client.gui to javafx.graphics, javafx.fxml;
    exports server.model.gameboard to jackson.databind;
    exports server.model.cards to jackson.annotations;
}

