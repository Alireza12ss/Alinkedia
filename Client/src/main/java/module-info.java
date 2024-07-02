module org.example.demologin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.gson;
    requires jjwt.api;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires GNAvatarView;


    opens org.example.Controllers to javafx.fxml;
    exports org.example.Controllers;
}
