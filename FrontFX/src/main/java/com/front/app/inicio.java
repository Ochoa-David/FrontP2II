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
        // Solo dejamos el botón de "Comprar Viajes"
        Button comprarViajesBtn = new Button("Comprar Viajes");
        comprarViajesBtn.setId("boton-inicio");
        comprarViajesBtn.setOnMouseClicked((MouseEvent e) -> {
            Scene escenaComprarViajes = comprarViajes.crearEscena(stage);
            stage.setScene(escenaComprarViajes);
            stage.setTitle("Viajes");
        });

        // Eliminamos todo lo relacionado con "Comprar Envios"

        // El login sigue estando
        Label login = new Label("¿Administrador?, Logueate");
        login.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-font-size: 12px;");
        login.setOnMouseClicked((MouseEvent e) -> {
            Scene escenaIniciarSesion = iniciarSesion.crearEscena(stage);
            stage.setScene(escenaIniciarSesion);
            stage.setTitle("Login");
        });

        // Solo dejamos el botón "Comprar Viajes" en el HBox
        HBox botonesCentro = new HBox(20, comprarViajesBtn);
        botonesCentro.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(botonesCentro);

        HBox topRight = new HBox(login);
        topRight.setPadding(new Insets(10));
        topRight.setAlignment(Pos.TOP_RIGHT);

        root.setTop(topRight);

        Scene scene = new Scene(root, 600, 500);
        scene.getStylesheets().add(inicio.class.getResource("/styles.css").toExternalForm());
        return scene;
    }
}
