package com.front.app;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class paginaPrincipal {
    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Pagina Principal");
        titulo.setId("titulo");

        Button volverBtn = new Button("Volver al inicio");
        volverBtn.setOnAction(e -> {
            Scene escenaLogin = iniciarSesion.crearEscena(stage);
            App.cambiarEscena(escenaLogin, "Inicio de Sesión");
        });

        VBox layout = new VBox(10,
                titulo,
                volverBtn
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add(iniciarSesion.class.getResource("/styles.css").toExternalForm());
        return scene;
    }
}
