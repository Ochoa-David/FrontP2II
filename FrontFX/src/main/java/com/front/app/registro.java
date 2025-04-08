package com.front.app;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class registro {
    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Registro");
        titulo.setId("titulo");

        TextField nuevoUsuario = new TextField();
        nuevoUsuario.setPromptText("Nuevo usuario");

        PasswordField nuevoPasswd = new PasswordField();
        nuevoPasswd.setPromptText("Nueva contraseña");

        ComboBox<String> selectorTipo = new ComboBox<>();
        selectorTipo.getItems().addAll("Administrador", "Empleado", "Cliente");
        selectorTipo.setPromptText("Selecciona tu rol");

        Button volverBtn = new Button("Volver al inicio");
        volverBtn.setOnAction(e -> {
            Scene escenaLogin = iniciarSesion.crearEscena(stage);
            App.cambiarEscena(escenaLogin, "Inicio de Sesión");
        });

        Button registrarBtn = new Button("Registrarse");
        Label mensajeRegistro = new Label();
        mensajeRegistro.setId("feedback");

        registrarBtn.setOnAction(e -> {
            mensajeRegistro.setText("mensaje de feedback");
        });

        VBox layout = new VBox(10,
                titulo,
                nuevoUsuario,
                nuevoPasswd,
                selectorTipo,
                registrarBtn,
                volverBtn,
                mensajeRegistro
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add(iniciarSesion.class.getResource("/styles.css").toExternalForm());
        return scene;
    }
}
