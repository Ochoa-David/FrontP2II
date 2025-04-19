package com.front.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class inicio {
    public static Scene crearEscena(Stage stage) {
        // Label de login
        Label login = new Label("¿Administrador?, Logueate");
        login.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-font-size: 12px;");
        login.setOnMouseClicked((MouseEvent e) -> {
            Scene escenaIniciarSesion = iniciarSesion.crearEscena(stage);
            stage.setScene(escenaIniciarSesion);
            stage.setTitle("Login");
        });

        // Contenedor sin el botón "Comprar Viajes"
        HBox botonesCentro = new HBox(20);
        botonesCentro.setAlignment(Pos.CENTER);

        // Layout principal
        BorderPane root = new BorderPane();
        root.setCenter(botonesCentro);

        // Login arriba a la derecha
        HBox topRight = new HBox(login);
        topRight.setPadding(new Insets(10));
        topRight.setAlignment(Pos.TOP_RIGHT);
        root.setTop(topRight);

        // Crear escena
        Scene scene = new Scene(root, 600, 500);
        scene.getStylesheets().add(inicio.class.getResource("/styles.css").toExternalForm());

        return scene;
    }
}
