module com.noble.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires de.jensd.fx.glyphs.fontawesome; // Font awesome is for icons used to style our application
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.noble.finalproject to javafx.fxml;
    exports com.noble.finalproject;
    exports com.noble.finalproject.Controllers;
    exports com.noble.finalproject.Controllers.Admin;
    exports com.noble.finalproject.Controllers.Client;
    exports com.noble.finalproject.Models;
    exports com.noble.finalproject.Views;
}