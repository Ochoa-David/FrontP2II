package com.front.app;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class sedes {
    public static Scene crearEscena(Stage stage) {
        Label titulo = new Label("Gestión de Sedes");
        titulo.setId("titulo");

        // Pestañas para Sedes, Departamentos y Ciudades
        TabPane tabPane = new TabPane();

        // Pestaña de Sedes
        Tab tabSedes = new Tab("Sedes");
        VBox sedesContent = new VBox(10);
        sedesContent.setPadding(new Insets(10));

        // Formulario para Sedes
        GridPane sedesForm = new GridPane();
        sedesForm.setHgap(10);
        sedesForm.setVgap(10);
        sedesForm.setAlignment(Pos.CENTER);

        TextField nombreSede = new TextField();
        TextField direccionSede = new TextField();
        ComboBox<String> departamentoSede = new ComboBox<>();
        ComboBox<String> ciudadSede = new ComboBox<>();
        TextField telefonoSede = new TextField();

        sedesForm.add(new Label("Nombre:"), 0, 0);
        sedesForm.add(nombreSede, 1, 0);
        sedesForm.add(new Label("Dirección:"), 0, 1);
        sedesForm.add(direccionSede, 1, 1);
        sedesForm.add(new Label("Departamento:"), 0, 2);
        sedesForm.add(departamentoSede, 1, 2);
        sedesForm.add(new Label("Ciudad:"), 0, 3);
        sedesForm.add(ciudadSede, 1, 3);
        sedesForm.add(new Label("Teléfono:"), 0, 4);
        sedesForm.add(telefonoSede, 1, 4);

        Button agregarSede = new Button("Agregar Sede");
        Button editarSede = new Button("Editar Sede");
        Button eliminarSede = new Button("Eliminar Sede");

        // Tabla para mostrar sedes
        TableView<String> tablaSedes = new TableView<>();
        TableColumn<String, String> columnaNombre = new TableColumn<>("Nombre");
        TableColumn<String, String> columnaDireccion = new TableColumn<>("Dirección");
        TableColumn<String, String> columnaDepartamento = new TableColumn<>("Departamento");
        TableColumn<String, String> columnaCiudad = new TableColumn<>("Ciudad");
        TableColumn<String, String> columnaTelefono = new TableColumn<>("Teléfono");

        tablaSedes.getColumns().addAll(columnaNombre, columnaDireccion, columnaDepartamento, columnaCiudad, columnaTelefono);

        sedesContent.getChildren().addAll(sedesForm, agregarSede, editarSede, eliminarSede, tablaSedes);
        tabSedes.setContent(sedesContent);

        // Pestaña de Departamentos
        Tab tabDepartamentos = new Tab("Departamentos");
        VBox departamentosContent = new VBox(10);
        departamentosContent.setPadding(new Insets(10));

        // Formulario para Departamentos
        GridPane departamentosForm = new GridPane();
        departamentosForm.setHgap(10);
        departamentosForm.setVgap(10);
        departamentosForm.setAlignment(Pos.CENTER);

        TextField nombreDepartamento = new TextField();
        TextField codigoDepartamento = new TextField();

        departamentosForm.add(new Label("Nombre:"), 0, 0);
        departamentosForm.add(nombreDepartamento, 1, 0);
        departamentosForm.add(new Label("Código:"), 0, 1);
        departamentosForm.add(codigoDepartamento, 1, 1);

        Button agregarDepartamento = new Button("Agregar Departamento");
        Button editarDepartamento = new Button("Editar Departamento");
        Button eliminarDepartamento = new Button("Eliminar Departamento");

        // Tabla para mostrar departamentos
        TableView<String> tablaDepartamentos = new TableView<>();
        TableColumn<String, String> columnaNombreDep = new TableColumn<>("Nombre");
        TableColumn<String, String> columnaCodigoDep = new TableColumn<>("Código");

        tablaDepartamentos.getColumns().addAll(columnaNombreDep, columnaCodigoDep);

        departamentosContent.getChildren().addAll(departamentosForm, agregarDepartamento, editarDepartamento, eliminarDepartamento, tablaDepartamentos);
        tabDepartamentos.setContent(departamentosContent);

        // Pestaña de Ciudades
        Tab tabCiudades = new Tab("Ciudades");
        VBox ciudadesContent = new VBox(10);
        ciudadesContent.setPadding(new Insets(10));

        // Formulario para Ciudades
        GridPane ciudadesForm = new GridPane();
        ciudadesForm.setHgap(10);
        ciudadesForm.setVgap(10);
        ciudadesForm.setAlignment(Pos.CENTER);

        TextField nombreCiudad = new TextField();
        ComboBox<String> departamentoCiudad = new ComboBox<>();
        TextField codigoCiudad = new TextField();

        ciudadesForm.add(new Label("Nombre:"), 0, 0);
        ciudadesForm.add(nombreCiudad, 1, 0);
        ciudadesForm.add(new Label("Departamento:"), 0, 1);
        ciudadesForm.add(departamentoCiudad, 1, 1);
        ciudadesForm.add(new Label("Código:"), 0, 2);
        ciudadesForm.add(codigoCiudad, 1, 2);

        Button agregarCiudad = new Button("Agregar Ciudad");
        Button editarCiudad = new Button("Editar Ciudad");
        Button eliminarCiudad = new Button("Eliminar Ciudad");

        // Tabla para mostrar ciudades
        TableView<String> tablaCiudades = new TableView<>();
        TableColumn<String, String> columnaNombreCiudad = new TableColumn<>("Nombre");
        TableColumn<String, String> columnaDepartamentoCiudad = new TableColumn<>("Departamento");
        TableColumn<String, String> columnaCodigoCiudad = new TableColumn<>("Código");

        tablaCiudades.getColumns().addAll(columnaNombreCiudad, columnaDepartamentoCiudad, columnaCodigoCiudad);

        ciudadesContent.getChildren().addAll(ciudadesForm, agregarCiudad, editarCiudad, eliminarCiudad, tablaCiudades);
        tabCiudades.setContent(ciudadesContent);

        // Agregar todas las pestañas al TabPane
        tabPane.getTabs().addAll(tabSedes, tabDepartamentos, tabCiudades);

        Button volverBtn = new Button("Volver al menú principal");
        volverBtn.setOnAction(e -> {
            Scene escenaPrincipal = paginaPrincipal.crearEscena(stage);
            App.cambiarEscena(escenaPrincipal, "Página Principal");
        });

        VBox layout = new VBox(10,
            titulo,
            tabPane,
            volverBtn
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(iniciarSesion.class.getResource("/styles.css").toExternalForm());
        return scene;
    }
} 