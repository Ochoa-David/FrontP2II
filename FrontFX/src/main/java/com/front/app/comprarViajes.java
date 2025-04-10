package com.front.app;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class comprarViajes {
    public static Scene crearEscena(Stage stage) {
        Label titulo1 = new Label("Cuentanos mas de tu viaje");
        titulo1.setId("titulo");

        TextField nombreUsuario = new TextField();
        nombreUsuario.setPromptText("Nombre Completo");

        TextField idUsuario = new TextField();
        idUsuario.setPromptText("Numero de Identificacion");

        ComboBox<String> ciudadOrigen = new ComboBox<>();
        ciudadOrigen.getItems().addAll("Administrador", "Empleado", "Cliente");
        ciudadOrigen.setPromptText("Ciudad de origen");

        ComboBox<String> ciudadDestino = new ComboBox<>();
        ciudadDestino.getItems().addAll("Administrador", "Empleado", "Cliente");
        ciudadDestino.setPromptText("Ciudad de Destino");

        TextField cantidadEquipaje = new TextField();
        cantidadEquipaje.setPromptText("Cantidad de Equipaje");
        cantidadEquipaje.setDisable(true);

        TextField pesoTotalEquipaje = new TextField();
        pesoTotalEquipaje.setPromptText("Peso en Total del Equipaje");
        pesoTotalEquipaje.setDisable(true);

        CheckBox hayEquipaje = new CheckBox("¿Equipaje?");
        hayEquipaje.setStyle("-fx-padding: 30 0 0 0;");
        hayEquipaje.setOnAction(e -> {
            boolean seleccionado = hayEquipaje.isSelected();
            cantidadEquipaje.setDisable(!seleccionado);
            pesoTotalEquipaje.setDisable(!seleccionado);
        });

        DatePicker fechaViaje = new DatePicker();
        fechaViaje.setPromptText("Fecha para tu Viaje?");

        Label mensaje = new Label();
        mensaje.setId("feedback");

        Button pagarBtn = new Button("Pagar");
        pagarBtn.setOnAction(e -> {
            if (nombreUsuario.getText().isEmpty() ||
                idUsuario.getText().isEmpty() ||
                ciudadOrigen.getValue() == null ||
                ciudadDestino.getValue() == null ||
                fechaViaje.getValue() == null ||
                (hayEquipaje.isSelected() && cantidadEquipaje.getText().isEmpty() && pesoTotalEquipaje.getText().isEmpty())
                ) {
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
            nombreUsuario,
            idUsuario,
            ciudadOrigen,
            ciudadDestino,
            fechaViaje,
            hayEquipaje,
            cantidadEquipaje,
            pesoTotalEquipaje,
            pagarBtn,
            volverBtn,
            mensaje
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add(comprarViajes.class.getResource("/styles.css").toExternalForm());
        return scene;
    }
}
