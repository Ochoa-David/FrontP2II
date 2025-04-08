package com.front.app;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage stageGlobal;

    @Override
    public void start(Stage stage) {
        stageGlobal = stage;
        Scene escenaLogin = iniciarSesion.crearEscena(stage);
        stage.setScene(escenaLogin);
        stage.setTitle("Inicio de Sesión");
        stage.show();
    }

    public static void cambiarEscena(Scene nuevaEscena, String titulo) {
        stageGlobal.setScene(nuevaEscena);
        stageGlobal.setTitle(titulo);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
