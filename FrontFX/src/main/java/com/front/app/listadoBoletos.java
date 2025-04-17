package com.front.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class listadoBoletos {
    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Listado de Boletos");
        titulo.setId("titulo");

        // Crear tabla
        TableView<Boleto> tabla = new TableView<>();
        
        // Columnas de la tabla
        TableColumn<Boleto, Integer> idBoletoCol = new TableColumn<>("ID Boleto");
        TableColumn<Boleto, Integer> numAsientoCol = new TableColumn<>("Número Asiento");
        TableColumn<Boleto, Integer> idViajeCol = new TableColumn<>("ID Viaje");
        TableColumn<Boleto, Integer> idClienteCol = new TableColumn<>("ID Cliente");
        TableColumn<Boleto, Integer> idBusCol = new TableColumn<>("ID Bus");
        TableColumn<Boleto, String> ciudadOrigenCol = new TableColumn<>("Ciudad Origen");
        TableColumn<Boleto, String> ciudadDestinoCol = new TableColumn<>("Ciudad Destino");
        TableColumn<Boleto, Double> precioCol = new TableColumn<>("Precio");
        TableColumn<Boleto, Integer> idEquipajeCol = new TableColumn<>("ID Equipaje");
        TableColumn<Boleto, Double> pesoCol = new TableColumn<>("Peso");
        TableColumn<Boleto, String> descripcionCol = new TableColumn<>("Descripción");

        // Configurar las columnas
        idBoletoCol.setCellValueFactory(new PropertyValueFactory<>("idBoleto"));
        numAsientoCol.setCellValueFactory(new PropertyValueFactory<>("numeroAsiento"));
        idViajeCol.setCellValueFactory(new PropertyValueFactory<>("idViaje"));
        idClienteCol.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        idBusCol.setCellValueFactory(new PropertyValueFactory<>("idBus"));
        ciudadOrigenCol.setCellValueFactory(new PropertyValueFactory<>("ciudadOrigen"));
        ciudadDestinoCol.setCellValueFactory(new PropertyValueFactory<>("ciudadDestino"));
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precio"));
        idEquipajeCol.setCellValueFactory(new PropertyValueFactory<>("idEquipaje"));
        pesoCol.setCellValueFactory(new PropertyValueFactory<>("peso"));
        descripcionCol.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Agregar columnas a la tabla
        tabla.getColumns().addAll(
            idBoletoCol, numAsientoCol, idViajeCol, idClienteCol, idBusCol,
            ciudadOrigenCol, ciudadDestinoCol, precioCol, idEquipajeCol,
            pesoCol, descripcionCol
        );

        // Crear datos de ejemplo
        ObservableList<Boleto> datos = FXCollections.observableArrayList(
            new Boleto(1, 15, 101, 1001, 201, "Bogotá", "Medellín", 150000.0, 301, 20.5, "Equipaje de mano"),
            new Boleto(2, 22, 102, 1002, 202, "Cali", "Barranquilla", 180000.0, 302, 25.0, "Equipaje grande"),
            new Boleto(3, 8, 103, 1003, 203, "Cartagena", "Bucaramanga", 120000.0, 303, 15.0, "Equipaje mediano"),
            new Boleto(4, 30, 104, 1004, 204, "Pereira", "Manizales", 80000.0, 304, 10.0, "Equipaje pequeño"),
            new Boleto(5, 12, 105, 1005, 205, "Santa Marta", "Cúcuta", 200000.0, 305, 30.0, "Equipaje especial")
        );

        // Configurar el filtrado
        FilteredList<Boleto> datosFiltrados = new FilteredList<>(datos, b -> true);

        // Filtros
        TextField filtroIdViaje = new TextField();
        filtroIdViaje.setPromptText("Filtrar por ID Viaje");
        TextField filtroIdCliente = new TextField();
        filtroIdCliente.setPromptText("Filtrar por ID Cliente");

        // Agregar listeners a los filtros
        filtroIdViaje.textProperty().addListener((observable, oldValue, newValue) -> {
            datosFiltrados.setPredicate(boleto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return String.valueOf(boleto.getIdViaje()).contains(newValue);
            });
        });

        filtroIdCliente.textProperty().addListener((observable, oldValue, newValue) -> {
            datosFiltrados.setPredicate(boleto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return String.valueOf(boleto.getIdCliente()).contains(newValue);
            });
        });

        // Envolver los datos filtrados en un SortedList
        SortedList<Boleto> datosOrdenados = new SortedList<>(datosFiltrados);
        datosOrdenados.comparatorProperty().bind(tabla.comparatorProperty());

        // Agregar datos a la tabla
        tabla.setItems(datosOrdenados);

        // Botones
        Button comprarBtn = new Button("Comprar Boleto");
        comprarBtn.setOnAction(e -> {
            try {
                Boleto boletoSeleccionado = tabla.getSelectionModel().getSelectedItem();
                if (boletoSeleccionado != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Compra de Boleto");
                    alert.setHeaderText(null);
                    alert.setContentText("Boleto #" + boletoSeleccionado.getIdBoleto() + " comprado exitosamente");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Advertencia");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor seleccione un boleto para comprar");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error al comprar el boleto: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        Button volverBtn = new Button("Volver");
        volverBtn.setOnAction(e -> {
            Scene escenaPrincipal = paginaPrincipal.crearEscena(stage);
            App.cambiarEscena(escenaPrincipal, "Página Principal");
        });

        // Layout
        HBox filtrosBox = new HBox(10);
        filtrosBox.getChildren().addAll(filtroIdViaje, filtroIdCliente);
        filtrosBox.setAlignment(Pos.CENTER);

        HBox botonesBox = new HBox(10);
        botonesBox.getChildren().addAll(comprarBtn, volverBtn);
        botonesBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titulo, filtrosBox, tabla, botonesBox);

        Scene scene = new Scene(layout, 1000, 600);
        scene.getStylesheets().add(iniciarSesion.class.getResource("/styles.css").toExternalForm());
        return scene;
    }

    // Clase interna para representar los datos del boleto
    public static class Boleto {
        private final int idBoleto;
        private final int numeroAsiento;
        private final int idViaje;
        private final int idCliente;
        private final int idBus;
        private final String ciudadOrigen;
        private final String ciudadDestino;
        private final double precio;
        private final int idEquipaje;
        private final double peso;
        private final String descripcion;

        public Boleto(int idBoleto, int numeroAsiento, int idViaje, int idCliente, int idBus,
                     String ciudadOrigen, String ciudadDestino, double precio, int idEquipaje,
                     double peso, String descripcion) {
            this.idBoleto = idBoleto;
            this.numeroAsiento = numeroAsiento;
            this.idViaje = idViaje;
            this.idCliente = idCliente;
            this.idBus = idBus;
            this.ciudadOrigen = ciudadOrigen;
            this.ciudadDestino = ciudadDestino;
            this.precio = precio;
            this.idEquipaje = idEquipaje;
            this.peso = peso;
            this.descripcion = descripcion;
        }

        // Getters
        public int getIdBoleto() { return idBoleto; }
        public int getNumeroAsiento() { return numeroAsiento; }
        public int getIdViaje() { return idViaje; }
        public int getIdCliente() { return idCliente; }
        public int getIdBus() { return idBus; }
        public String getCiudadOrigen() { return ciudadOrigen; }
        public String getCiudadDestino() { return ciudadDestino; }
        public double getPrecio() { return precio; }
        public int getIdEquipaje() { return idEquipaje; }
        public double getPeso() { return peso; }
        public String getDescripcion() { return descripcion; }
    }
} 