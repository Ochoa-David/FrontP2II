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

        // Agregar opciones a los ComboBox
        departamentoSede.getItems().addAll("Cundinamarca", "Antioquia", "Valle del Cauca", "Santander", "Atlántico");
        ciudadSede.getItems().addAll("Bogotá", "Medellín", "Cali", "Bucaramanga", "Barranquilla");

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
        TableView<Sede> tablaSedes = new TableView<>();
        TableColumn<Sede, String> columnaNombre = new TableColumn<>("Nombre");
        TableColumn<Sede, String> columnaDireccion = new TableColumn<>("Dirección");
        TableColumn<Sede, String> columnaDepartamento = new TableColumn<>("Departamento");
        TableColumn<Sede, String> columnaCiudad = new TableColumn<>("Ciudad");
        TableColumn<Sede, String> columnaTelefono = new TableColumn<>("Teléfono");

        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        columnaCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tablaSedes.getColumns().addAll(columnaNombre, columnaDireccion, columnaDepartamento, columnaCiudad, columnaTelefono);

        // Filtro para sedes
        TextField filtroSedes = new TextField();
        filtroSedes.setPromptText("Buscar sede...");

        // Datos quemados para sedes
        ObservableList<Sede> datosSedes = FXCollections.observableArrayList(
            new Sede("Sede Principal", "Calle 72 # 10-45", "Cundinamarca", "Bogotá", "601 1234567"),
            new Sede("Sede Norte", "Carrera 45 # 20-30", "Antioquia", "Medellín", "604 2345678"),
            new Sede("Sede Sur", "Avenida 6N # 15-20", "Valle del Cauca", "Cali", "602 3456789"),
            new Sede("Sede Oriente", "Calle 30 # 25-10", "Santander", "Bucaramanga", "607 4567890"),
            new Sede("Sede Costa", "Carrera 50 # 30-15", "Atlántico", "Barranquilla", "605 5678901"),
            new Sede("Sede Occidente", "Calle 15 # 25-40", "Valle del Cauca", "Palmira", "602 6789012"),
            new Sede("Sede Centro", "Carrera 7 # 12-35", "Cundinamarca", "Zipaquirá", "601 7890123"),
            new Sede("Sede Caribe", "Avenida Circunvalar # 45-60", "Bolívar", "Cartagena", "605 8901234"),
            new Sede("Sede Eje Cafetero", "Calle 8 # 10-15", "Risaralda", "Pereira", "606 9012345"),
            new Sede("Sede Amazonía", "Carrera 20 # 30-45", "Putumayo", "Mocoa", "608 0123456")
        );

        // Configurar filtrado para sedes
        FilteredList<Sede> sedesFiltradas = new FilteredList<>(datosSedes, b -> true);
        filtroSedes.textProperty().addListener((observable, oldValue, newValue) -> {
            sedesFiltradas.setPredicate(sede -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return sede.getNombre().toLowerCase().contains(lowerCaseFilter) ||
                       sede.getCiudad().toLowerCase().contains(lowerCaseFilter) ||
                       sede.getDepartamento().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Sede> sedesOrdenadas = new SortedList<>(sedesFiltradas);
        sedesOrdenadas.comparatorProperty().bind(tablaSedes.comparatorProperty());
        tablaSedes.setItems(sedesOrdenadas);

        sedesContent.getChildren().addAll(sedesForm, filtroSedes, agregarSede, editarSede, eliminarSede, tablaSedes);
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
        TableView<Departamento> tablaDepartamentos = new TableView<>();
        TableColumn<Departamento, String> columnaNombreDep = new TableColumn<>("Nombre");
        TableColumn<Departamento, String> columnaCodigoDep = new TableColumn<>("Código");

        columnaNombreDep.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCodigoDep.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        tablaDepartamentos.getColumns().addAll(columnaNombreDep, columnaCodigoDep);

        // Filtro para departamentos
        TextField filtroDepartamentos = new TextField();
        filtroDepartamentos.setPromptText("Buscar departamento...");

        // Datos quemados para departamentos
        ObservableList<Departamento> datosDepartamentos = FXCollections.observableArrayList(
            new Departamento("Cundinamarca", "11"),
            new Departamento("Antioquia", "05"),
            new Departamento("Valle del Cauca", "76"),
            new Departamento("Santander", "68"),
            new Departamento("Atlántico", "08")
        );

        // Configurar filtrado para departamentos
        FilteredList<Departamento> departamentosFiltrados = new FilteredList<>(datosDepartamentos, b -> true);
        filtroDepartamentos.textProperty().addListener((observable, oldValue, newValue) -> {
            departamentosFiltrados.setPredicate(departamento -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return departamento.getNombre().toLowerCase().contains(lowerCaseFilter) ||
                       departamento.getCodigo().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Departamento> departamentosOrdenados = new SortedList<>(departamentosFiltrados);
        departamentosOrdenados.comparatorProperty().bind(tablaDepartamentos.comparatorProperty());
        tablaDepartamentos.setItems(departamentosOrdenados);

        departamentosContent.getChildren().addAll(departamentosForm, filtroDepartamentos, agregarDepartamento, editarDepartamento, eliminarDepartamento, tablaDepartamentos);
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

        // Agregar opciones al ComboBox de departamentos
        departamentoCiudad.getItems().addAll("Cundinamarca", "Antioquia", "Valle del Cauca", "Santander", "Atlántico");

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
        TableView<Ciudad> tablaCiudades = new TableView<>();
        TableColumn<Ciudad, String> columnaNombreCiudad = new TableColumn<>("Nombre");
        TableColumn<Ciudad, String> columnaDepartamentoCiudad = new TableColumn<>("Departamento");
        TableColumn<Ciudad, String> columnaCodigoCiudad = new TableColumn<>("Código");

        columnaNombreCiudad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDepartamentoCiudad.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        columnaCodigoCiudad.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        tablaCiudades.getColumns().addAll(columnaNombreCiudad, columnaDepartamentoCiudad, columnaCodigoCiudad);

        // Filtro para ciudades
        TextField filtroCiudades = new TextField();
        filtroCiudades.setPromptText("Buscar ciudad...");

        // Datos quemados para ciudades
        ObservableList<Ciudad> datosCiudades = FXCollections.observableArrayList(
            new Ciudad("Bogotá", "Cundinamarca", "11001"),
            new Ciudad("Medellín", "Antioquia", "05001"),
            new Ciudad("Cali", "Valle del Cauca", "76001"),
            new Ciudad("Bucaramanga", "Santander", "68001"),
            new Ciudad("Barranquilla", "Atlántico", "08001")
        );

        // Configurar filtrado para ciudades
        FilteredList<Ciudad> ciudadesFiltradas = new FilteredList<>(datosCiudades, b -> true);
        filtroCiudades.textProperty().addListener((observable, oldValue, newValue) -> {
            ciudadesFiltradas.setPredicate(ciudad -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return ciudad.getNombre().toLowerCase().contains(lowerCaseFilter) ||
                       ciudad.getDepartamento().toLowerCase().contains(lowerCaseFilter) ||
                       ciudad.getCodigo().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Ciudad> ciudadesOrdenadas = new SortedList<>(ciudadesFiltradas);
        ciudadesOrdenadas.comparatorProperty().bind(tablaCiudades.comparatorProperty());
        tablaCiudades.setItems(ciudadesOrdenadas);

        ciudadesContent.getChildren().addAll(ciudadesForm, filtroCiudades, agregarCiudad, editarCiudad, eliminarCiudad, tablaCiudades);
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

    // Clases internas para representar los datos
    public static class Sede {
        private final String nombre;
        private final String direccion;
        private final String departamento;
        private final String ciudad;
        private final String telefono;

        public Sede(String nombre, String direccion, String departamento, String ciudad, String telefono) {
            this.nombre = nombre;
            this.direccion = direccion;
            this.departamento = departamento;
            this.ciudad = ciudad;
            this.telefono = telefono;
        }

        public String getNombre() { return nombre; }
        public String getDireccion() { return direccion; }
        public String getDepartamento() { return departamento; }
        public String getCiudad() { return ciudad; }
        public String getTelefono() { return telefono; }
    }

    public static class Departamento {
        private final String nombre;
        private final String codigo;

        public Departamento(String nombre, String codigo) {
            this.nombre = nombre;
            this.codigo = codigo;
        }

        public String getNombre() { return nombre; }
        public String getCodigo() { return codigo; }
    }

    public static class Ciudad {
        private final String nombre;
        private final String departamento;
        private final String codigo;

        public Ciudad(String nombre, String departamento, String codigo) {
            this.nombre = nombre;
            this.departamento = departamento;
            this.codigo = codigo;
        }

        public String getNombre() { return nombre; }
        public String getDepartamento() { return departamento; }
        public String getCodigo() { return codigo; }
    }
} 