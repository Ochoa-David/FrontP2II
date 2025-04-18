package com.front.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class iniciarSesion {

    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Inicia Sesión");
        titulo.setId("titulo");

        TextField entradaUsuario = new TextField();
        entradaUsuario.setPromptText("Usuario");

        PasswordField entradaPasswd = new PasswordField();
        entradaPasswd.setPromptText("Contraseña");

        Button iniciarSesionBtn = new Button("Iniciar Sesión");

        Label mensaje = new Label();
        mensaje.setId("feedback");

        iniciarSesionBtn.setOnAction(e -> {
            String usuario = entradaUsuario.getText();
            String clave = entradaPasswd.getText();
            if (usuario.isEmpty() || clave.isEmpty()) {
                mensaje.setText("Completa todos los campos.");
                return;
            }
            // Validación simple para el login sin conexión HTTP
            if (usuario.equals("admin") && clave.equals("1234")) { // Ejemplo de validación básica
                mensaje.setText("Inicio de sesión exitoso.");
                // Aquí puedes cargar la escena principal
                Scene escenaPaginaPrincipal = paginaPrincipal.crearEscena(stage);
                stage.setScene(escenaPaginaPrincipal);
                stage.setTitle("Página Principal");
            } else {
                mensaje.setText("Usuario o contraseña incorrectos.");
            }
        });

        Button volverBtn = new Button("Volver al inicio");
        volverBtn.setOnAction(e -> {
            Scene escenaInicio = inicio.crearEscena(stage);
            App.cambiarEscena(escenaInicio, "Inicio");
        });

        VBox formulario = new VBox(20,
            titulo,
            entradaUsuario,
            entradaPasswd,
            iniciarSesionBtn,
            volverBtn,
            mensaje
        );
        formulario.setPadding(new Insets(20));
        formulario.setAlignment(Pos.CENTER);

        Scene scene = new Scene(formulario, 600, 500);
        scene.getStylesheets().add(iniciarSesion.class.getResource("/styles.css").toExternalForm());
        return scene;
    }
}
