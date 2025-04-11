package com.front.app;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class paginaPrincipal {
    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Pagina Principal");
        titulo.setId("titulo");

        Button busesBtn = new Button("Datos sobre buses");
        busesBtn.setId("boton-grande");
        busesBtn.setOnAction(e -> {
            Scene escenaLogin = iniciarSesion.crearEscena(stage);
            App.cambiarEscena(escenaLogin, "Inicio de Sesión");
        });

        Button paqueteriaBtn = new Button("Datos sobre paqueteria");
        paqueteriaBtn.setId("boton-grande");
        paqueteriaBtn.setOnAction(e -> {
            Scene escenaLogin = iniciarSesion.crearEscena(stage);
            App.cambiarEscena(escenaLogin, "Inicio de Sesión");
        });

        Button sedesBtn = new Button("Datos sobre sedes");
        sedesBtn.setId("boton-grande");
        sedesBtn.setOnAction(e -> {
            Scene escenaLogin = iniciarSesion.crearEscena(stage);
            App.cambiarEscena(escenaLogin, "Inicio de Sesión");
        });

        Button viajesBtn = new Button("Datos sobre viajes");
        viajesBtn.setId("boton-grande");
        viajesBtn.setOnAction(e -> {
            Scene escenaLogin = iniciarSesion.crearEscena(stage);
            App.cambiarEscena(escenaLogin, "Inicio de Sesión");
        });

        Button empleadosBtn = new Button("Datos sobre empleados");
        empleadosBtn.setId("boton-grande");
        empleadosBtn.setOnAction(e -> {
            Scene escenaLogin = iniciarSesion.crearEscena(stage);
            App.cambiarEscena(escenaLogin, "Inicio de Sesión");
        });

        Button empresasBtn = new Button("Datos sobre empresas");
        empresasBtn.setId("boton-grande");
        empresasBtn.setOnAction(e -> {
            GestionEmpresas gestion = new GestionEmpresas();
            Scene escenaEmpresas = gestion.crearEscena(stage); // Usa el 'stage' ya existente
            App.cambiarEscena(escenaEmpresas, "Gestión de Empresas");
        });

        Button volverBtn = new Button("Volver al inicio de sesion");
        volverBtn.setOnAction(e -> {
            Scene escenaLogin = iniciarSesion.crearEscena(stage);
            App.cambiarEscena(escenaLogin, "Inicio de Sesión");
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.add(busesBtn, 0, 0);
        grid.add(paqueteriaBtn, 1, 0);
        grid.add(sedesBtn, 0, 1);
        grid.add(viajesBtn, 1, 1);
        grid.add(empleadosBtn, 0, 2);
        grid.add(empresasBtn, 1, 2);

        VBox layout = new VBox(10,
            titulo,
            grid,
            volverBtn
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add(iniciarSesion.class.getResource("/styles.css").toExternalForm());
        return scene;
    }
}
