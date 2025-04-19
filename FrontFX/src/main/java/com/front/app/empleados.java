package com.front.app;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.List;

public class empleados {

    // Clase interna para representar empleados
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

        // Getters
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

    // Lista de empleados
    private static List<Empleado> empleadosList = new ArrayList<>();

    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Gestión de Empleados");
        titulo.setId("titulo");

        // --- EMPLEADOS QUEMADOS --- Solo agregar los empleados una vez
        if (empleadosList.isEmpty()) { // Solo agregar si la lista está vacía (evitar duplicados)
            empleadosList.add(new Empleado("1014306711", "Sebastian", "Buitrago", "3223975787", "buitrago@gmail.com", "Bogotá", "Bogotá", "Conductor", "Sede Bogotá"));
            empleadosList.add(new Empleado("1014306712", "Carlos", "Ramírez", "3123456789", "carlos@gmail.com", "Medellín", "Medellín", "Administrador", "Sede Medellín"));
            empleadosList.add(new Empleado("1014306713", "Laura", "Gómez", "3109876543", "laura@gmail.com", "Cali", "Cali", "Mensajero", "Sede Cali"));
        }

        // --- TABLA ---
        TableView<Empleado> tabla = new TableView<>();

        // Definir las columnas de la tabla
        TableColumn<Empleado, String> colCedula = new TableColumn<>("Cédula");
        TableColumn<Empleado, String> colNombres = new TableColumn<>("Nombres");
        TableColumn<Empleado, String> colApellidos = new TableColumn<>("Apellidos");
        TableColumn<Empleado, String> colTelefono = new TableColumn<>("Teléfono");
        TableColumn<Empleado, String> colCorreo = new TableColumn<>("Correo");
        TableColumn<Empleado, String> colCiudadOrigen = new TableColumn<>("Ciudad Origen");
        TableColumn<Empleado, String> colCiudadResidencia = new TableColumn<>("Ciudad Residencia");
        TableColumn<Empleado, String> colTipo = new TableColumn<>("Tipo");
        TableColumn<Empleado, String> colSede = new TableColumn<>("Sede");

        // Establecer el tamaño fijo para las columnas
        colCedula.setPrefWidth(140);  
        colNombres.setPrefWidth(140);  
        colApellidos.setPrefWidth(140);  
        colTelefono.setPrefWidth(140);  
        colCorreo.setPrefWidth(160);  
        colCiudadOrigen.setPrefWidth(160);  
        colCiudadResidencia.setPrefWidth(190);  
        colTipo.setPrefWidth(130);  
        colSede.setPrefWidth(130);  

        // Deshabilitar la redimensión de las columnas
        colCedula.setResizable(false);
        colNombres.setResizable(false);
        colApellidos.setResizable(false);
        colTelefono.setResizable(false);
        colCorreo.setResizable(false);
        colCiudadOrigen.setResizable(false);
        colCiudadResidencia.setResizable(false);
        colTipo.setResizable(false);
        colSede.setResizable(false);

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

        // Limpiar cualquier columna existente y agregar las columnas a la tabla
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colCedula, colNombres, colApellidos, colTelefono, colCorreo, colCiudadOrigen, colCiudadResidencia, colTipo, colSede);

        // Mostrar los empleados "quemados" en la tabla SOLO UNA VEZ
        tabla.getItems().clear(); // Asegurarse de que no haya empleados duplicados
        tabla.getItems().addAll(empleadosList);

        // --- FILTRO POR CÉDULA ---
        HBox filtroBox = new HBox(10);
        filtroBox.setAlignment(Pos.CENTER);

        Label filtroLabel = new Label("Filtrar por Cédula:");
        TextField filtroCedulaField = new TextField();
        filtroCedulaField.setPromptText("Ingrese cédula...");
        Button buscarBtn = new Button("Buscar");
        Button limpiarFiltroBtn = new Button("Limpiar Filtro");

        // Acción de buscar
        buscarBtn.setOnAction(e -> {
            String cedula = filtroCedulaField.getText();
            // Filtrar empleados por cédula
            tabla.getItems().clear();  // Limpiar la tabla
            for (Empleado e1 : empleadosList) {
                if (e1.getCedula().contains(cedula)) {
                    tabla.getItems().add(e1);
                }
            }
        });

        // Acción de limpiar filtro
        limpiarFiltroBtn.setOnAction(e -> {
            filtroCedulaField.clear(); // Limpiar campo de filtro
            tabla.getItems().clear();
            tabla.getItems().addAll(empleadosList); // Mostrar todos los empleados
        });

        filtroBox.getChildren().addAll(filtroLabel, filtroCedulaField, buscarBtn, limpiarFiltroBtn);

        // --- BOTÓN CREAR EMPLEADO ---
        Button crearEmpleadoBtn = new Button("Crear Empleado");
        crearEmpleadoBtn.setOnAction(e -> {
            // Crear escena del formulario (se detalla más abajo)
            Scene formularioScene = crearFormularioEmpleado(stage);
            stage.setScene(formularioScene);
        });

        // --- BOTÓN VOLVER AL MENÚ PRINCIPAL ---
        Button volverBtn = new Button("Volver al menú principal");
        volverBtn.setOnAction(e -> {
            // Aquí puedes cambiar a la escena de la página principal
            Scene escenaPrincipal = paginaPrincipal.crearEscena(stage);  // Asumiendo que tienes la clase paginaPrincipal
            App.cambiarEscena(escenaPrincipal, "Página Principal");
        });

        VBox layout = new VBox(15, titulo, filtroBox, tabla, crearEmpleadoBtn, volverBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 900, 700);
        scene.getStylesheets().add(empleados.class.getResource("/styles.css").toExternalForm());

        return scene;
    }

    public static Scene crearFormularioEmpleado(Stage stage) {
        Label titulo = new Label("Formulario de Empleado");
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

        // --- BOTONES ---
        Button guardarBtn = new Button("Guardar");
        Button cancelarBtn = new Button("Cancelar");

        guardarBtn.setOnAction(e -> {
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
            empleadosList.add(nuevo);
            // Volver a la tabla
            Scene escenaEmpleados = crearEscena(stage);
            stage.setScene(escenaEmpleados);
        });

        cancelarBtn.setOnAction(e -> {
            Scene escenaEmpleados = crearEscena(stage);
            stage.setScene(escenaEmpleados);
        });

        HBox botones = new HBox(10, guardarBtn, cancelarBtn);
        botones.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, titulo, form, botones);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 900, 700);
        scene.getStylesheets().add(empleados.class.getResource("/styles.css").toExternalForm());

        return scene;
    }
}
