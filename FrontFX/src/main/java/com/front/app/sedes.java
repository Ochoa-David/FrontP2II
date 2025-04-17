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
import javafx.stage.Modality;

public class sedes {
    public static Scene crearEscena(Stage stage) {
        TabPane tabPane = new TabPane();
        
        // Declaración de datos quemados
        ObservableList<Departamentos> datosDepartamentos = FXCollections.observableArrayList(
            new Departamentos(1, "Cundinamarca"),
            new Departamentos(2, "Antioquia"),
            new Departamentos(3, "Valle del Cauca"),
            new Departamentos(4, "Santander"),
            new Departamentos(5, "Boyacá"),
            new Departamentos(6, "Tolima"),
            new Departamentos(7, "Huila"),
            new Departamentos(8, "Caldas"),
            new Departamentos(9, "Risaralda"),
            new Departamentos(10, "Quindío")
        );

        ObservableList<Ciudades> datosCiudades = FXCollections.observableArrayList(
            new Ciudades(1, "Bogotá", 1),
            new Ciudades(2, "Medellín", 2),
            new Ciudades(3, "Cali", 3),
            new Ciudades(4, "Bucaramanga", 4),
            new Ciudades(5, "Tunja", 5),
            new Ciudades(6, "Ibagué", 6),
            new Ciudades(7, "Neiva", 7),
            new Ciudades(8, "Manizales", 8),
            new Ciudades(9, "Pereira", 9),
            new Ciudades(10, "Armenia", 10),
            new Ciudades(11, "Soacha", 1),
            new Ciudades(12, "Bello", 2),
            new Ciudades(13, "Palmira", 3),
            new Ciudades(14, "Floridablanca", 4),
            new Ciudades(15, "Duitama", 5)
        );

        ObservableList<Sedes> datosSedes = FXCollections.observableArrayList(
            new Sedes(1, "Terminal Norte", "Calle 80 #65-45", 1),
            new Sedes(2, "Terminal Sur", "Autopista Sur #45-23", 1),
            new Sedes(3, "Terminal Central", "Carrera 50 #21-34", 2),
            new Sedes(4, "Terminal Salitre", "Avenida 68 #38-12", 1),
            new Sedes(5, "Terminal Cali", "Calle 15 #25-30", 3),
            new Sedes(6, "Terminal Bucaramanga", "Carrera 20 #35-40", 4),
            new Sedes(7, "Terminal Tunja", "Avenida 5 #10-15", 5),
            new Sedes(8, "Terminal Ibagué", "Calle 8 #12-20", 6),
            new Sedes(9, "Terminal Neiva", "Carrera 5 #8-10", 7),
            new Sedes(10, "Terminal Manizales", "Avenida 12 #15-20", 8)
        );

        ObservableList<Modulos> datosModulos = FXCollections.observableArrayList(
            new Modulos(1, "Módulo Norte", 1),
            new Modulos(2, "Módulo Sur", 2),
            new Modulos(3, "Módulo Central", 3),
            new Modulos(4, "Módulo Salitre", 4),
            new Modulos(5, "Módulo Cali", 5),
            new Modulos(6, "Módulo Bucaramanga", 6),
            new Modulos(7, "Módulo Tunja", 7),
            new Modulos(8, "Módulo Ibagué", 8),
            new Modulos(9, "Módulo Neiva", 9),
            new Modulos(10, "Módulo Manizales", 10)
        );

        ObservableList<Destinos> datosDestinos = FXCollections.observableArrayList(
            new Destinos(1, "Bogotá - Medellín", 1, 1, 2),
            new Destinos(2, "Medellín - Cali", 2, 2, 3),
            new Destinos(3, "Cali - Bogotá", 3, 3, 1),
            new Destinos(4, "Bogotá - Bucaramanga", 4, 1, 4),
            new Destinos(5, "Bucaramanga - Tunja", 5, 4, 5),
            new Destinos(6, "Tunja - Ibagué", 6, 5, 6),
            new Destinos(7, "Ibagué - Neiva", 7, 6, 7),
            new Destinos(8, "Neiva - Manizales", 8, 7, 8),
            new Destinos(9, "Manizales - Pereira", 9, 8, 9),
            new Destinos(10, "Pereira - Armenia", 10, 9, 10)
        );

        ObservableList<ParadasIntermedias> datosParadas = FXCollections.observableArrayList(
            new ParadasIntermedias(1, "Parada La Vega", 1),
            new ParadasIntermedias(2, "Parada La Estrella", 2),
            new ParadasIntermedias(3, "Parada Palmira", 3),
            new ParadasIntermedias(4, "Parada Floridablanca", 4),
            new ParadasIntermedias(5, "Parada Duitama", 5),
            new ParadasIntermedias(6, "Parada Soacha", 11),
            new ParadasIntermedias(7, "Parada Bello", 12),
            new ParadasIntermedias(8, "Parada Palmira", 13),
            new ParadasIntermedias(9, "Parada Floridablanca", 14),
            new ParadasIntermedias(10, "Parada Duitama", 15)
        );

        ObservableList<Rutas> datosRutas = FXCollections.observableArrayList(
            new Rutas(1, "Ruta Norte", 1),
            new Rutas(2, "Ruta Sur", 2),
            new Rutas(3, "Ruta Central", 3),
            new Rutas(4, "Ruta Oriental", 4),
            new Rutas(5, "Ruta Occidental", 5),
            new Rutas(6, "Ruta Norte-Sur", 6),
            new Rutas(7, "Ruta Sur-Oriente", 7),
            new Rutas(8, "Ruta Occidente", 8),
            new Rutas(9, "Ruta Eje Cafetero", 9),
            new Rutas(10, "Ruta Centro", 10)
        );

        // Pestaña de Empleados por Sede
        Tab tabEmpleados = new Tab("Empleados por Sede");
        VBox empleadosContent = new VBox(10);
        empleadosContent.setPadding(new Insets(10));

        TableView<EmpleadosPorSede> tablaEmpleados = new TableView<>();
        TableColumn<EmpleadosPorSede, String> columnaNombreSede = new TableColumn<>("Nombre Sede");
        TableColumn<EmpleadosPorSede, Void> columnaAcciones = new TableColumn<>("Acciones");

        columnaNombreSede.setCellValueFactory(new PropertyValueFactory<>("nombre_sede"));
        
        columnaAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnDetalles = new Button("Ver Detalles");

            {
                btnDetalles.setOnAction(event -> {
                    EmpleadosPorSede sede = getTableView().getItems().get(getIndex());
                    mostrarDetallesEmpleados(sede, stage);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDetalles);
                }
            }
        });

        tablaEmpleados.getColumns().addAll(columnaNombreSede, columnaAcciones);

        // Datos quemados para empleados por sede
        ObservableList<EmpleadosPorSede> datosEmpleados = FXCollections.observableArrayList(
            new EmpleadosPorSede("Terminal Norte"),
            new EmpleadosPorSede("Terminal Sur"),
            new EmpleadosPorSede("Terminal Central"),
            new EmpleadosPorSede("Terminal Salitre")
        );

        // Datos de empleados por tipo para cada sede
        ObservableList<DetalleEmpleado> detallesTerminalNorte = FXCollections.observableArrayList(
            new DetalleEmpleado("Conductor", 15),
            new DetalleEmpleado("Taquillero", 5),
            new DetalleEmpleado("Seguridad", 8)
        );
        datosEmpleados.get(0).setDetalles(detallesTerminalNorte);

        ObservableList<DetalleEmpleado> detallesTerminalSur = FXCollections.observableArrayList(
            new DetalleEmpleado("Conductor", 12),
            new DetalleEmpleado("Taquillero", 4)
        );
        datosEmpleados.get(1).setDetalles(detallesTerminalSur);

        ObservableList<DetalleEmpleado> detallesTerminalCentral = FXCollections.observableArrayList(
            new DetalleEmpleado("Conductor", 20),
            new DetalleEmpleado("Administrativo", 10)
        );
        datosEmpleados.get(2).setDetalles(detallesTerminalCentral);

        ObservableList<DetalleEmpleado> detallesTerminalSalitre = FXCollections.observableArrayList(
            new DetalleEmpleado("Conductor", 18),
            new DetalleEmpleado("Mantenimiento", 6)
        );
        datosEmpleados.get(3).setDetalles(detallesTerminalSalitre);

        tablaEmpleados.setItems(datosEmpleados);

        empleadosContent.getChildren().addAll(new Label("Empleados por Sede"), tablaEmpleados);
        tabEmpleados.setContent(empleadosContent);

        // Pestaña de Estructura Geográfica
        Tab tabEstructura = new Tab("Estructura Geográfica");
        TabPane tabPaneEstructura = new TabPane();

        // Subpestaña de Ubicaciones
        Tab tabUbicaciones = new Tab("Ubicaciones");
        VBox ubicacionesContent = new VBox(10);
        ubicacionesContent.setPadding(new Insets(10));

        // Tabla de Departamentos
        TableView<Departamentos> tablaDepartamentos = new TableView<>();
        TableColumn<Departamentos, Integer> colIdDep = new TableColumn<>("ID");
        TableColumn<Departamentos, String> colNombreDep = new TableColumn<>("Nombre");

        colIdDep.setCellValueFactory(new PropertyValueFactory<>("id_departamento"));
        colNombreDep.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tablaDepartamentos.getColumns().addAll(colIdDep, colNombreDep);
        tablaDepartamentos.setItems(datosDepartamentos);

        // Tabla de Ciudades
        TableView<Ciudades> tablaCiudades = new TableView<>();
        TableColumn<Ciudades, Integer> colIdCiudad = new TableColumn<>("ID");
        TableColumn<Ciudades, String> colNombreCiudad = new TableColumn<>("Nombre");
        TableColumn<Ciudades, Integer> colIdDepCiudad = new TableColumn<>("ID Departamento");

        colIdCiudad.setCellValueFactory(new PropertyValueFactory<>("id_ciudad"));
        colNombreCiudad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdDepCiudad.setCellValueFactory(new PropertyValueFactory<>("id_departamento"));

        tablaCiudades.getColumns().addAll(colIdCiudad, colNombreCiudad, colIdDepCiudad);
        tablaCiudades.setItems(datosCiudades);

        ubicacionesContent.getChildren().addAll(
            new Label("Departamentos"), tablaDepartamentos,
            new Label("Ciudades"), tablaCiudades
        );
        tabUbicaciones.setContent(ubicacionesContent);

        // Subpestaña de Sedes y Módulos
        Tab tabSedesModulos = new Tab("Sedes y Módulos");
        VBox sedesModulosContent = new VBox(10);
        sedesModulosContent.setPadding(new Insets(10));

        // Tabla de Sedes
        TableView<Sedes> tablaSedes = new TableView<>();
        TableColumn<Sedes, Integer> colIdSede = new TableColumn<>("ID");
        TableColumn<Sedes, String> colNombreSede = new TableColumn<>("Nombre");
        TableColumn<Sedes, String> colDireccion = new TableColumn<>("Dirección");
        TableColumn<Sedes, Integer> colIdCiudadSede = new TableColumn<>("ID Ciudad");

        colIdSede.setCellValueFactory(new PropertyValueFactory<>("id_sede"));
        colNombreSede.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colIdCiudadSede.setCellValueFactory(new PropertyValueFactory<>("id_ciudad"));

        tablaSedes.getColumns().addAll(colIdSede, colNombreSede, colDireccion, colIdCiudadSede);
        tablaSedes.setItems(datosSedes);

        // Tabla de Módulos
        TableView<Modulos> tablaModulos = new TableView<>();
        TableColumn<Modulos, Integer> colIdModulo = new TableColumn<>("ID");
        TableColumn<Modulos, String> colNombreModulo = new TableColumn<>("Nombre");
        TableColumn<Modulos, Integer> colIdSedeModulo = new TableColumn<>("ID Sede");

        colIdModulo.setCellValueFactory(new PropertyValueFactory<>("id_modulo"));
        colNombreModulo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdSedeModulo.setCellValueFactory(new PropertyValueFactory<>("id_sede"));

        tablaModulos.getColumns().addAll(colIdModulo, colNombreModulo, colIdSedeModulo);
        tablaModulos.setItems(datosModulos);

        sedesModulosContent.getChildren().addAll(
            new Label("Sedes"), tablaSedes,
            new Label("Módulos"), tablaModulos
        );
        tabSedesModulos.setContent(sedesModulosContent);

        // Subpestaña de Rutas y Destinos
        Tab tabRutasDestinos = new Tab("Rutas y Destinos");
        VBox rutasDestinosContent = new VBox(10);
        rutasDestinosContent.setPadding(new Insets(10));

        // Tabla de Destinos
        TableView<Destinos> tablaDestinos = new TableView<>();
        TableColumn<Destinos, Integer> colIdDestino = new TableColumn<>("ID");
        TableColumn<Destinos, String> colNombreDestino = new TableColumn<>("Nombre");
        TableColumn<Destinos, Integer> colIdModuloDestino = new TableColumn<>("ID Módulo");
        TableColumn<Destinos, Integer> colIdCiudadOrigen = new TableColumn<>("ID Ciudad Origen");
        TableColumn<Destinos, Integer> colIdCiudadDestino = new TableColumn<>("ID Ciudad Destino");

        colIdDestino.setCellValueFactory(new PropertyValueFactory<>("id_destino"));
        colNombreDestino.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdModuloDestino.setCellValueFactory(new PropertyValueFactory<>("id_modulo"));
        colIdCiudadOrigen.setCellValueFactory(new PropertyValueFactory<>("id_ciudad_origen"));
        colIdCiudadDestino.setCellValueFactory(new PropertyValueFactory<>("id_ciudad_destino"));

        tablaDestinos.getColumns().addAll(colIdDestino, colNombreDestino, colIdModuloDestino, 
                                        colIdCiudadOrigen, colIdCiudadDestino);
        tablaDestinos.setItems(datosDestinos);

        // Tabla de Paradas Intermedias
        TableView<ParadasIntermedias> tablaParadas = new TableView<>();
        TableColumn<ParadasIntermedias, Integer> colIdParada = new TableColumn<>("ID");
        TableColumn<ParadasIntermedias, String> colNombreParada = new TableColumn<>("Nombre Parada");
        TableColumn<ParadasIntermedias, Integer> colIdCiudadParada = new TableColumn<>("ID Ciudad");

        colIdParada.setCellValueFactory(new PropertyValueFactory<>("id_parada"));
        colNombreParada.setCellValueFactory(new PropertyValueFactory<>("nombre_parada"));
        colIdCiudadParada.setCellValueFactory(new PropertyValueFactory<>("id_ciudad"));

        tablaParadas.getColumns().addAll(colIdParada, colNombreParada, colIdCiudadParada);
        tablaParadas.setItems(datosParadas);

        // Tabla de Rutas
        TableView<Rutas> tablaRutas = new TableView<>();
        TableColumn<Rutas, Integer> colIdRuta = new TableColumn<>("ID");
        TableColumn<Rutas, String> colNombreRuta = new TableColumn<>("Nombre");
        TableColumn<Rutas, Integer> colIdParadasIntermedias = new TableColumn<>("ID Paradas Intermedias");

        colIdRuta.setCellValueFactory(new PropertyValueFactory<>("id_ruta"));
        colNombreRuta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdParadasIntermedias.setCellValueFactory(new PropertyValueFactory<>("id_paradas_intermedias"));

        tablaRutas.getColumns().addAll(colIdRuta, colNombreRuta, colIdParadasIntermedias);
        tablaRutas.setItems(datosRutas);

        rutasDestinosContent.getChildren().addAll(
            new Label("Destinos"), tablaDestinos,
            new Label("Paradas Intermedias"), tablaParadas,
            new Label("Rutas"), tablaRutas
        );
        tabRutasDestinos.setContent(rutasDestinosContent);

        // Agregar subpestañas a la pestaña de estructura
        tabPaneEstructura.getTabs().addAll(tabUbicaciones, tabSedesModulos, tabRutasDestinos);
        tabEstructura.setContent(tabPaneEstructura);

        // Agregar pestañas principales
        tabPane.getTabs().addAll(tabEmpleados, tabEstructura);

        Button volverBtn = new Button("Volver al menú principal");
        volverBtn.setOnAction(e -> {
            Scene escenaPrincipal = paginaPrincipal.crearEscena(stage);
            App.cambiarEscena(escenaPrincipal, "Página Principal");
        });

        VBox layout = new VBox(10, tabPane, volverBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(iniciarSesion.class.getResource("/styles.css").toExternalForm());
        return scene;
    }

    // Método para mostrar los detalles de empleados de una sede
    private static void mostrarDetallesEmpleados(EmpleadosPorSede sede, Stage ownerStage) {
        Stage ventanaDetalles = new Stage();
        ventanaDetalles.initModality(Modality.WINDOW_MODAL);
        ventanaDetalles.initOwner(ownerStage);
        ventanaDetalles.setTitle("Detalles de Empleados - " + sede.getNombre_sede());

        TableView<DetalleEmpleado> tablaDetalles = new TableView<>();
        TableColumn<DetalleEmpleado, String> columnaTipo = new TableColumn<>("Tipo de Empleado");
        TableColumn<DetalleEmpleado, Integer> columnaCantidad = new TableColumn<>("Cantidad");

        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo_empleado"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        tablaDetalles.getColumns().addAll(columnaTipo, columnaCantidad);
        tablaDetalles.setItems(sede.getDetalles());

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));
        contenido.getChildren().addAll(
            new Label("Detalles de Empleados en " + sede.getNombre_sede()),
            tablaDetalles
        );

        Scene sceneDetalles = new Scene(contenido, 400, 300);
        ventanaDetalles.setScene(sceneDetalles);
        ventanaDetalles.show();
    }

    // Clases para la estructura de datos
    public static class EmpleadosPorSede {
        private final String nombre_sede;
        private ObservableList<DetalleEmpleado> detalles;

        public EmpleadosPorSede(String nombre_sede) {
            this.nombre_sede = nombre_sede;
        }

        public String getNombre_sede() { return nombre_sede; }
        public ObservableList<DetalleEmpleado> getDetalles() { return detalles; }
        public void setDetalles(ObservableList<DetalleEmpleado> detalles) { this.detalles = detalles; }
    }

    public static class DetalleEmpleado {
        private final String tipo_empleado;
        private final int cantidad;

        public DetalleEmpleado(String tipo_empleado, int cantidad) {
            this.tipo_empleado = tipo_empleado;
            this.cantidad = cantidad;
        }

        public String getTipo_empleado() { return tipo_empleado; }
        public int getCantidad() { return cantidad; }
    }

    public static class Departamentos {
        private final int id_departamento;
        private final String nombre;

        public Departamentos(int id_departamento, String nombre) {
            this.id_departamento = id_departamento;
            this.nombre = nombre;
        }

        public int getId_departamento() { return id_departamento; }
        public String getNombre() { return nombre; }
    }

    public static class Ciudades {
        private final int id_ciudad;
        private final String nombre;
        private final int id_departamento;

        public Ciudades(int id_ciudad, String nombre, int id_departamento) {
            this.id_ciudad = id_ciudad;
            this.nombre = nombre;
            this.id_departamento = id_departamento;
        }

        public int getId_ciudad() { return id_ciudad; }
        public String getNombre() { return nombre; }
        public int getId_departamento() { return id_departamento; }
    }

    public static class Sedes {
        private final int id_sede;
        private final String nombre;
        private final String direccion;
        private final int id_ciudad;

        public Sedes(int id_sede, String nombre, String direccion, int id_ciudad) {
            this.id_sede = id_sede;
            this.nombre = nombre;
            this.direccion = direccion;
            this.id_ciudad = id_ciudad;
        }

        public int getId_sede() { return id_sede; }
        public String getNombre() { return nombre; }
        public String getDireccion() { return direccion; }
        public int getId_ciudad() { return id_ciudad; }
    }

    public static class Modulos {
        private final int id_modulo;
        private final String nombre;
        private final int id_sede;

        public Modulos(int id_modulo, String nombre, int id_sede) {
            this.id_modulo = id_modulo;
            this.nombre = nombre;
            this.id_sede = id_sede;
        }

        public int getId_modulo() { return id_modulo; }
        public String getNombre() { return nombre; }
        public int getId_sede() { return id_sede; }
    }

    public static class Destinos {
        private final int id_destino;
        private final String nombre;
        private final int id_modulo;
        private final int id_ciudad_origen;
        private final int id_ciudad_destino;

        public Destinos(int id_destino, String nombre, int id_modulo, 
                       int id_ciudad_origen, int id_ciudad_destino) {
            this.id_destino = id_destino;
            this.nombre = nombre;
            this.id_modulo = id_modulo;
            this.id_ciudad_origen = id_ciudad_origen;
            this.id_ciudad_destino = id_ciudad_destino;
        }

        public int getId_destino() { return id_destino; }
        public String getNombre() { return nombre; }
        public int getId_modulo() { return id_modulo; }
        public int getId_ciudad_origen() { return id_ciudad_origen; }
        public int getId_ciudad_destino() { return id_ciudad_destino; }
    }

    public static class ParadasIntermedias {
        private final int id_parada;
        private final String nombre_parada;
        private final int id_ciudad;

        public ParadasIntermedias(int id_parada, String nombre_parada, int id_ciudad) {
            this.id_parada = id_parada;
            this.nombre_parada = nombre_parada;
            this.id_ciudad = id_ciudad;
        }

        public int getId_parada() { return id_parada; }
        public String getNombre_parada() { return nombre_parada; }
        public int getId_ciudad() { return id_ciudad; }
    }

    public static class Rutas {
        private final int id_ruta;
        private final String nombre;
        private final int id_paradas_intermedias;

        public Rutas(int id_ruta, String nombre, int id_paradas_intermedias) {
            this.id_ruta = id_ruta;
            this.nombre = nombre;
            this.id_paradas_intermedias = id_paradas_intermedias;
        }

        public int getId_ruta() { return id_ruta; }
        public String getNombre() { return nombre; }
        public int getId_paradas_intermedias() { return id_paradas_intermedias; }
    }
} 