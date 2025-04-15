package com.front.app;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.time.LocalDate;

public class viajes {
    // Datos simulados de viajes
    private static final String[][] datosViajes = {
        {"1", "2025-04-20", "$150", "1", "2", "ABC123", "Volvo", "50", "Ruta A"},
        {"2", "2025-04-21", "$200", "3", "4", "DEF456", "Mercedes", "45", "Ruta B"},
        {"3", "2025-04-22", "$180", "2", "3", "XYZ789", "Scania", "60", "Ruta C"}
    };

    // Método para crear la vista de viajes
    public static Scene crearEscena(Stage stage) {
        // Título de la vista
        Label titulo = new Label("Listado de Viajes");
        titulo.setId("titulo");

        // Crear la tabla de viajes
        TableView<String[]> tablaViajes = new TableView<>();
        tablaViajes.setPrefWidth(600);

        // Definir las columnas
        TableColumn<String[], String> columnaIdViaje = new TableColumn<>("ID Viaje");
        columnaIdViaje.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));

        TableColumn<String[], String> columnaFechaSalida = new TableColumn<>("Fecha Salida");
        columnaFechaSalida.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));

        TableColumn<String[], String> columnaValor = new TableColumn<>("Valor");
        columnaValor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));

        TableColumn<String[], String> columnaCiudadOrigen = new TableColumn<>("Ciudad Origen");
        columnaCiudadOrigen.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));

        TableColumn<String[], String> columnaCiudadDestino = new TableColumn<>("Ciudad Destino");
        columnaCiudadDestino.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4]));

        TableColumn<String[], String> columnaPlaca = new TableColumn<>("Placa");
        columnaPlaca.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[5]));

        TableColumn<String[], String> columnaMarca = new TableColumn<>("Marca");
        columnaMarca.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[6]));

        TableColumn<String[], String> columnaCapacidad = new TableColumn<>("Capacidad");
        columnaCapacidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[7]));

        TableColumn<String[], String> columnaRuta = new TableColumn<>("Ruta");
        columnaRuta.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[8]));

        // Agregar las columnas a la tabla
        tablaViajes.getColumns().addAll(columnaIdViaje, columnaFechaSalida, columnaValor, columnaCiudadOrigen,
            columnaCiudadDestino, columnaPlaca, columnaMarca, columnaCapacidad, columnaRuta);

        // Agregar los datos simulados a la tabla
        for (String[] viaje : datosViajes) {
            tablaViajes.getItems().add(viaje);
        }

        // Crear DatePicker para filtrar por fecha de salida
        DatePicker filtroFechaSalida = new DatePicker();
        filtroFechaSalida.setPromptText("Filtrar por fecha de salida");
        filtroFechaSalida.setOnAction(e -> {
            LocalDate fechaSeleccionada = filtroFechaSalida.getValue();
            if (fechaSeleccionada != null) {
                // Filtrar la tabla por la fecha seleccionada
                tablaViajes.getItems().clear();
                for (String[] viaje : datosViajes) {
                    // Convertir la fecha en formato String a LocalDate para compararlo
                    LocalDate fechaViaje = LocalDate.parse(viaje[1]); // Asumimos que la fecha está en la columna [1] de los datos
                    if (fechaViaje.equals(fechaSeleccionada)) {
                        tablaViajes.getItems().add(viaje);
                    }
                }
            }
        });

        // Acción al hacer clic en un viaje
        tablaViajes.setOnMouseClicked(e -> {
            if (tablaViajes.getSelectionModel().getSelectedItem() != null) {
                String[] viajeSeleccionado = tablaViajes.getSelectionModel().getSelectedItem();
                // Al seleccionar un viaje, actualizamos el contenido de la vista con el detalle del viaje
                VBox layout = new VBox(10);
                layout.setPadding(new Insets(20));

                layout.getChildren().add(crearDetalleViaje(viajeSeleccionado, stage)); // Mostrar detalles del viaje

                // Botón para volver al listado
                Button volverBtn = new Button("Volver al listado de viajes");
                volverBtn.setOnAction(event -> {
                    Scene escenaViajes = crearEscena(stage);  // Regresar a la vista del listado de viajes
                    App.cambiarEscena(escenaViajes, "Listado de Viajes");
                });
                layout.getChildren().add(volverBtn);

                Scene scene = new Scene(layout, 800, 600);
                scene.getStylesheets().add(viajes.class.getResource("/styles.css").toExternalForm());
                stage.setScene(scene); // Actualizar la escena para mostrar detalles
            }
        });

        // Crear el botón para volver a la página principal
        Button volverPrincipalBtn = new Button("Volver a la página principal");
        volverPrincipalBtn.setOnAction(e -> {
            // Redirigir al usuario a la página principal
            Scene escenaPrincipal = inicio.crearEscena(stage);  // Aquí se debe llamar a la escena principal
            App.cambiarEscena(escenaPrincipal, "Inicio");
        });

        // Layout de la vista de listado de viajes
        VBox layout = new VBox(10, filtroFechaSalida, tablaViajes, volverPrincipalBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(viajes.class.getResource("/styles.css").toExternalForm());
        return scene;
    }

    // Crear la vista de detalle del viaje (ahora devuelve un Node)
    private static Node crearDetalleViaje(String[] viajeSeleccionado, Stage stage) {
        Label titulo = new Label("Detalle del Viaje");
        titulo.setId("titulo");

        // Crear formulario de detalles del viaje
        GridPane detallesForm = new GridPane();
        detallesForm.setHgap(10);
        detallesForm.setVgap(10);
        detallesForm.setAlignment(Pos.CENTER);

        if (viajeSeleccionado != null) {
            detallesForm.add(new Label("ID Viaje:"), 0, 0);
            detallesForm.add(new Label(viajeSeleccionado[0]), 1, 0);
            detallesForm.add(new Label("Fecha de Salida:"), 0, 1);
            detallesForm.add(new Label(viajeSeleccionado[1]), 1, 1);
            detallesForm.add(new Label("Valor:"), 0, 2);
            detallesForm.add(new Label(viajeSeleccionado[2]), 1, 2);
            detallesForm.add(new Label("Placa:"), 0, 3);
            detallesForm.add(new Label(viajeSeleccionado[5]), 1, 3);
            detallesForm.add(new Label("Marca:"), 0, 4);
            detallesForm.add(new Label(viajeSeleccionado[6]), 1, 4);
            detallesForm.add(new Label("Capacidad:"), 0, 5);
            detallesForm.add(new Label(viajeSeleccionado[7]), 1, 5);
            detallesForm.add(new Label("Ruta:"), 0, 6);
            detallesForm.add(new Label(viajeSeleccionado[8]), 1, 6);
        }

        // Retornar el formulario de detalles
        return detallesForm;
    }
}
