package com.front.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class empleados {

    // Clase interna para representar empleados (puede usarse con la tabla)
    public static class Empleado {
        private String cedula, nombres, apellidos, telefono, correo;
        private String ciudadOrigen, ciudadResidencia, tipoEmpleado, sede;

        public Empleado(String cedula, String nombres, String apellidos, String telefono, String correo,
                        String ciudadOrigen, String ciudadResidencia, String tipoEmpleado, String sede) {
            this.cedula = cedula;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.telefono = telefono;
            this.correo = correo;
            this.ciudadOrigen = ciudadOrigen;
            this.ciudadResidencia = ciudadResidencia;
            this.tipoEmpleado = tipoEmpleado;
            this.sede = sede;
        }

        // Getters necesarios para TableView
        public String getCedula() { return cedula; }
        public String getNombres() { return nombres; }
        public String getApellidos() { return apellidos; }
        public String getTelefono() { return telefono; }
        public String getCorreo() { return correo; }
        public String getCiudadOrigen() { return ciudadOrigen; }
        public String getCiudadResidencia() { return ciudadResidencia; }
        public String getTipoEmpleado() { return tipoEmpleado; }
        public String getSede() { return sede; }
    }

    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Gestión de Empleados");
        titulo.setId("titulo");

        // --- FORMULARIO ---
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        TextField cedulaField = new TextField();
        TextField nombresField = new TextField();
        TextField apellidosField = new TextField();
        TextField telefonoField = new TextField();
        TextField correoField = new TextField();

        ComboBox<String> ciudadOrigenBox = new ComboBox<>();
        ComboBox<String> ciudadResidenciaBox = new ComboBox<>();
        ComboBox<String> tipoEmpleadoBox = new ComboBox<>();
        ComboBox<String> sedeBox = new ComboBox<>();

        // Ciudades reales de Colombia
        String[] ciudadesColombia = {"Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Bucaramanga", "Pereira", "Cúcuta", "Santa Marta", "Ibagué"};
        ciudadOrigenBox.setItems(FXCollections.observableArrayList(ciudadesColombia));
        ciudadResidenciaBox.setItems(FXCollections.observableArrayList(ciudadesColombia));

        // Simulados
        tipoEmpleadoBox.setItems(FXCollections.observableArrayList("Administrador", "Mensajero", "Conductor"));
        sedeBox.setItems(FXCollections.observableArrayList("Sede Bogotá", "Sede Medellín", "Sede Cali"));

        form.add(new Label("Cédula:"), 0, 0);
        form.add(cedulaField, 1, 0);
        form.add(new Label("Nombres:"), 0, 1);
        form.add(nombresField, 1, 1);
        form.add(new Label("Apellidos:"), 0, 2);
        form.add(apellidosField, 1, 2);
        form.add(new Label("Teléfono:"), 0, 3);
        form.add(telefonoField, 1, 3);
        form.add(new Label("Correo:"), 0, 4);
        form.add(correoField, 1, 4);
        form.add(new Label("Ciudad Origen:"), 0, 5);
        form.add(ciudadOrigenBox, 1, 5);
        form.add(new Label("Ciudad Residencia:"), 0, 6);
        form.add(ciudadResidenciaBox, 1, 6);
        form.add(new Label("Tipo de Empleado:"), 0, 7);
        form.add(tipoEmpleadoBox, 1, 7);
        form.add(new Label("Sede:"), 0, 8);
        form.add(sedeBox, 1, 8);

        // --- TABLA ---
        TableView<Empleado> tabla = new TableView<>();

        TableColumn<Empleado, String> colCedula = new TableColumn<>("Cédula");
        TableColumn<Empleado, String> colNombres = new TableColumn<>("Nombres");
        TableColumn<Empleado, String> colApellidos = new TableColumn<>("Apellidos");
        TableColumn<Empleado, String> colTelefono = new TableColumn<>("Teléfono");
        TableColumn<Empleado, String> colCorreo = new TableColumn<>("Correo");
        TableColumn<Empleado, String> colCiudadOrigen = new TableColumn<>("Ciudad Origen");
        TableColumn<Empleado, String> colCiudadResidencia = new TableColumn<>("Ciudad Residencia");
        TableColumn<Empleado, String> colTipo = new TableColumn<>("Tipo");
        TableColumn<Empleado, String> colSede = new TableColumn<>("Sede");

        // Asociar columnas con propiedades
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCiudadOrigen.setCellValueFactory(new PropertyValueFactory<>("ciudadOrigen"));
        colCiudadResidencia.setCellValueFactory(new PropertyValueFactory<>("ciudadResidencia"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEmpleado"));
        colSede.setCellValueFactory(new PropertyValueFactory<>("sede"));

        tabla.getColumns().addAll(colCedula, colNombres, colApellidos, colTelefono, colCorreo, colCiudadOrigen, colCiudadResidencia, colTipo, colSede);

        // --- BOTONES ---
        Button agregarBtn = new Button("Agregar");
        Button editarBtn = new Button("Editar");
        Button eliminarBtn = new Button("Eliminar");

        // Eventos simulados
        agregarBtn.setOnAction(e -> {
            Empleado nuevo = new Empleado(
                cedulaField.getText(),
                nombresField.getText(),
                apellidosField.getText(),
                telefonoField.getText(),
                correoField.getText(),
                ciudadOrigenBox.getValue(),
                ciudadResidenciaBox.getValue(),
                tipoEmpleadoBox.getValue(),
                sedeBox.getValue()
            );
            tabla.getItems().add(nuevo);
            cedulaField.clear(); nombresField.clear(); apellidosField.clear(); telefonoField.clear(); correoField.clear();
            ciudadOrigenBox.getSelectionModel().clearSelection();
            ciudadResidenciaBox.getSelectionModel().clearSelection();
            tipoEmpleadoBox.getSelectionModel().clearSelection();
            sedeBox.getSelectionModel().clearSelection();
        });

        editarBtn.setOnAction(e -> {
            Empleado seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                tabla.getItems().remove(seleccionado);
                // Aquí podrías llenar los campos del formulario si lo deseas
            }
        });

        eliminarBtn.setOnAction(e -> {
            Empleado seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                tabla.getItems().remove(seleccionado);
            }
        });

        HBox botones = new HBox(10, agregarBtn, editarBtn, eliminarBtn);
        botones.setAlignment(Pos.CENTER);

        // --- VOLVER AL MENÚ PRINCIPAL ---
        Button volverBtn = new Button("Volver al menú principal");
        volverBtn.setOnAction(e -> {
            Scene escenaPrincipal = paginaPrincipal.crearEscena(stage);
            App.cambiarEscena(escenaPrincipal, "Página Principal");
        });

        VBox layout = new VBox(15, titulo, form, botones, tabla, volverBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 900, 700);
        scene.getStylesheets().add(empleados.class.getResource("/styles.css").toExternalForm());

        return scene;
    }
}

