package com.front.app;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class comprarEnvios {
    public static Scene crearEscena(Stage stage) {
        Label titulo1 = new Label("Remitente:");
        titulo1.setId("titulo");

        TextField nombreRemitente = new TextField();
        nombreRemitente.setPromptText("Nombre Completo");

        TextField idRemitente = new TextField();
        idRemitente.setPromptText("Numero de Identificacion");

        Label titulo2 = new Label("Destinatario:");
        titulo2.setId("titulo");

        TextField nombreDestinatario = new TextField();
        nombreDestinatario.setPromptText("Nombre Completo");

        TextField idDestinatario = new TextField();
        idDestinatario.setPromptText("Numero de Identificacion");

        ComboBox<String> sedeEnvio = new ComboBox<>();
        sedeEnvio.getItems().addAll("Administrador", "Empleado", "Cliente");
        sedeEnvio.setPromptText("Sede de envio");

        ComboBox<String> sedeEntrega = new ComboBox<>();
        sedeEntrega.getItems().addAll("Administrador", "Empleado", "Cliente");
        sedeEntrega.setPromptText("Sede de entrega");

        TextField pesoPaquete = new TextField();
        pesoPaquete.setPromptText("Peso del paquete (Kg)");

        Label mensaje = new Label();
        mensaje.setId("feedback");

        Button pagarBtn = new Button("Pagar");
        pagarBtn.setOnAction(e -> {
            if (nombreRemitente.getText().isEmpty() ||
                idRemitente.getText().isEmpty() ||
                nombreDestinatario.getText().isEmpty() ||
                idDestinatario.getText().isEmpty() ||
                sedeEnvio.getValue() == null ||
                sedeEntrega.getValue() == null ||
                pesoPaquete.getText().isEmpty()) {
                mensaje.setText("Completa todos los campos.");
                return;
            }
        });

        Button volverBtn = new Button("Volver al inicio");
        volverBtn.setOnAction(e -> {
            Scene escenaInicio = inicio.crearEscena(stage);
            App.cambiarEscena(escenaInicio, "Inicio");
        });

        VBox layout = new VBox(10,
            titulo1,
            nombreRemitente,
            idRemitente,
            titulo2,
            nombreDestinatario,
            idDestinatario,
            sedeEnvio,
            sedeEntrega,
            pesoPaquete,
            pagarBtn,
            volverBtn,
            mensaje
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add(comprarEnvios.class.getResource("/styles.css").toExternalForm());
        return scene;
    }
}
