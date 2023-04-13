module psa.app.psawindowapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens appinterface to javafx.fxml;
    opens models to javafx.base;
    exports appinterface;
    exports appinterface.controllers;
    opens appinterface.controllers to javafx.fxml;
}