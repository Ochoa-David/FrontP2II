package com.front.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class detallesBoletos {
    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Detalles de Boletos Comprados");
        titulo.setId("titulo");

        // Crear tabla
        TableView<BoletoComprado> tabla = new TableView<>();
        
        // Columnas de la tabla
        TableColumn<BoletoComprado, Integer> idBoletoCol = new TableColumn<>("ID Boleto");
        TableColumn<BoletoComprado, String> fechaCompraCol = new TableColumn<>("Fecha Compra");
        TableColumn<BoletoComprado, String> ciudadOrigenCol = new TableColumn<>("Ciudad Origen");
        TableColumn<BoletoComprado, String> ciudadDestinoCol = new TableColumn<>("Ciudad Destino");
        TableColumn<BoletoComprado, String> fechaViajeCol = new TableColumn<>("Fecha Viaje");
        TableColumn<BoletoComprado, String> horaSalidaCol = new TableColumn<>("Hora Salida");
        TableColumn<BoletoComprado, Integer> numAsientoCol = new TableColumn<>("Número Asiento");
        TableColumn<BoletoComprado, Double> precioCol = new TableColumn<>("Precio");
        TableColumn<BoletoComprado, String> estadoCol = new TableColumn<>("Estado");

        // Configurar las columnas
        idBoletoCol.setCellValueFactory(new PropertyValueFactory<>("idBoleto"));
        fechaCompraCol.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
        ciudadOrigenCol.setCellValueFactory(new PropertyValueFactory<>("ciudadOrigen"));
        ciudadDestinoCol.setCellValueFactory(new PropertyValueFactory<>("ciudadDestino"));
        fechaViajeCol.setCellValueFactory(new PropertyValueFactory<>("fechaViaje"));
        horaSalidaCol.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));
        numAsientoCol.setCellValueFactory(new PropertyValueFactory<>("numeroAsiento"));
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precio"));
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Agregar columnas a la tabla
        tabla.getColumns().addAll(
            idBoletoCol, fechaCompraCol, ciudadOrigenCol, ciudadDestinoCol,
            fechaViajeCol, horaSalidaCol, numAsientoCol, precioCol, estadoCol
        );

        // Crear datos de ejemplo
        ObservableList<BoletoComprado> datos = FXCollections.observableArrayList(
            new BoletoComprado(1, "2024-04-15", "Bogotá", "Medellín", "2024-04-20", "08:00", 15, 150000.0, "Confirmado", 
                301, 20.5, "Equipaje de mano", "Maleta pequeña"),
            new BoletoComprado(2, "2024-04-16", "Cali", "Barranquilla", "2024-04-21", "10:30", 22, 180000.0, "Confirmado",
                302, 25.0, "Equipaje grande", "Maleta grande y mochila"),
            new BoletoComprado(3, "2024-04-17", "Cartagena", "Bucaramanga", "2024-04-22", "14:00", 8, 120000.0, "Pendiente",
                303, 15.0, "Equipaje mediano", "Maleta mediana"),
            new BoletoComprado(4, "2024-04-18", "Pereira", "Manizales", "2024-04-23", "16:45", 30, 80000.0, "Confirmado",
                304, 10.0, "Equipaje pequeño", "Mochila"),
            new BoletoComprado(5, "2024-04-19", "Santa Marta", "Cúcuta", "2024-04-24", "07:15", 12, 200000.0, "Cancelado",
                305, 30.0, "Equipaje especial", "Instrumento musical")
        );

        // Agregar datos a la tabla
        tabla.setItems(datos);

        // Botones
        Button verDetallesBtn = new Button("Ver Detalles");
        verDetallesBtn.setOnAction(e -> {
            try {
                BoletoComprado boletoSeleccionado = tabla.getSelectionModel().getSelectedItem();
                if (boletoSeleccionado != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Detalles del Boleto");
                    alert.setHeaderText("Información detallada del boleto #" + boletoSeleccionado.getIdBoleto());
                    alert.setContentText(
                        "Fecha de Compra: " + boletoSeleccionado.getFechaCompra() + "\n" +
                        "Ruta: " + boletoSeleccionado.getCiudadOrigen() + " -> " + boletoSeleccionado.getCiudadDestino() + "\n" +
                        "Fecha del Viaje: " + boletoSeleccionado.getFechaViaje() + "\n" +
                        "Hora de Salida: " + boletoSeleccionado.getHoraSalida() + "\n" +
                        "Asiento: " + boletoSeleccionado.getNumeroAsiento() + "\n" +
                        "Precio: $" + boletoSeleccionado.getPrecio() + "\n" +
                        "Estado: " + boletoSeleccionado.getEstado() + "\n\n" +
                        "Información del Equipaje:\n" +
                        "ID Equipaje: " + boletoSeleccionado.getIdEquipaje() + "\n" +
                        "Peso: " + boletoSeleccionado.getPesoEquipaje() + " kg\n" +
                        "Tipo: " + boletoSeleccionado.getTipoEquipaje() + "\n" +
                        "Descripción: " + boletoSeleccionado.getDescripcionEquipaje()
                    );
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Advertencia");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor seleccione un boleto para ver sus detalles");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error al mostrar los detalles: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        Button volverBtn = new Button("Volver");
        volverBtn.setOnAction(e -> {
            Scene escenaPrincipal = paginaPrincipal.crearEscena(stage);
            App.cambiarEscena(escenaPrincipal, "Página Principal");
        });

        // Layout
        HBox botonesBox = new HBox(10);
        botonesBox.getChildren().addAll(verDetallesBtn, volverBtn);
        botonesBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titulo, tabla, botonesBox);

        Scene scene = new Scene(layout, 1000, 600);
        scene.getStylesheets().add(iniciarSesion.class.getResource("/styles.css").toExternalForm());
        return scene;
    }

    // Clase interna para representar los datos del boleto comprado
    public static class BoletoComprado {
        private final int idBoleto;
        private final String fechaCompra;
        private final String ciudadOrigen;
        private final String ciudadDestino;
        private final String fechaViaje;
        private final String horaSalida;
        private final int numeroAsiento;
        private final double precio;
        private final String estado;
        private final int idEquipaje;
        private final double pesoEquipaje;
        private final String tipoEquipaje;
        private final String descripcionEquipaje;

        public BoletoComprado(int idBoleto, String fechaCompra, String ciudadOrigen, String ciudadDestino,
                            String fechaViaje, String horaSalida, int numeroAsiento, double precio, String estado,
                            int idEquipaje, double pesoEquipaje, String tipoEquipaje, String descripcionEquipaje) {
            this.idBoleto = idBoleto;
            this.fechaCompra = fechaCompra;
            this.ciudadOrigen = ciudadOrigen;
            this.ciudadDestino = ciudadDestino;
            this.fechaViaje = fechaViaje;
            this.horaSalida = horaSalida;
            this.numeroAsiento = numeroAsiento;
            this.precio = precio;
            this.estado = estado;
            this.idEquipaje = idEquipaje;
            this.pesoEquipaje = pesoEquipaje;
            this.tipoEquipaje = tipoEquipaje;
            this.descripcionEquipaje = descripcionEquipaje;
        }

        // Getters
        public int getIdBoleto() { return idBoleto; }
        public String getFechaCompra() { return fechaCompra; }
        public String getCiudadOrigen() { return ciudadOrigen; }
        public String getCiudadDestino() { return ciudadDestino; }
        public String getFechaViaje() { return fechaViaje; }
        public String getHoraSalida() { return horaSalida; }
        public int getNumeroAsiento() { return numeroAsiento; }
        public double getPrecio() { return precio; }
        public String getEstado() { return estado; }
        public int getIdEquipaje() { return idEquipaje; }
        public double getPesoEquipaje() { return pesoEquipaje; }
        public String getTipoEquipaje() { return tipoEquipaje; }
        public String getDescripcionEquipaje() { return descripcionEquipaje; }
    }
} 