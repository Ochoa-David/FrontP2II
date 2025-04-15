package com.front.app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class GestionMantenimientos extends Application {

    // Definición de excepciones personalizadas
    public static class MantenimientoException extends Exception {
        public MantenimientoException(String message) {
            super(message);
        }
    }
    
    public static class IdDuplicadoException extends MantenimientoException {
        public IdDuplicadoException() {
            super("El ID de mantenimiento ya existe en el sistema.");
        }
    }
    
    public static class CampoVacioException extends MantenimientoException {
        public CampoVacioException(String campo) {
            super("El campo " + campo + " no puede estar vacío.");
        }
    }
    
    public static class NoSeleccionException extends MantenimientoException {
        public NoSeleccionException() {
            super("No hay ningún mantenimiento seleccionado.");
        }
    }
    
    public static class BaseDatosException extends MantenimientoException {
        public BaseDatosException(String operacion) {
            super("Error al " + operacion + " datos en la base de datos.");
        }
    }

    // Modelo de datos para Mantenimiento
    public static class Mantenimiento {
        private Integer id_mantenimiento;
        private String descripcion;
        private LocalDate fecha_mantenimiento;
        private Integer id_bus;
        private String accion;

        public Mantenimiento(Integer id_mantenimiento, String descripcion, LocalDate fecha_mantenimiento, 
                             Integer id_bus, String accion) {
            this.id_mantenimiento = id_mantenimiento;
            this.descripcion = descripcion;
            this.fecha_mantenimiento = fecha_mantenimiento;
            this.id_bus = id_bus;
            this.accion = accion;
        }

        // Getters y setters
        public Integer getId_mantenimiento() { return id_mantenimiento; }
        public void setId_mantenimiento(Integer id_mantenimiento) { this.id_mantenimiento = id_mantenimiento; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public LocalDate getFecha_mantenimiento() { return fecha_mantenimiento; }
        public void setFecha_mantenimiento(LocalDate fecha_mantenimiento) { this.fecha_mantenimiento = fecha_mantenimiento; }

        public Integer getId_bus() { return id_bus; }
        public void setId_bus(Integer id_bus) { this.id_bus = id_bus; }

        public String getAccion() { return accion; }
        public void setAccion(String accion) { this.accion = accion; }
        
        // Para mostrar la fecha formateada en la tabla
        public String getFechaFormateada() {
            if (fecha_mantenimiento == null) return "";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return fecha_mantenimiento.format(formatter);
        }
    }

    // Interfaz para gestionar operaciones con la API/Base de datos
    public interface MantenimientoService {
        ObservableList<Mantenimiento> obtenerMantenimientos() throws BaseDatosException;
        ObservableList<Mantenimiento> obtenerMantenimientosPorBus(Integer idBus) throws BaseDatosException;
        Mantenimiento obtenerMantenimientoPorId(Integer idMantenimiento) throws BaseDatosException;
    }

    // Implementación mock del servicio para pruebas
    public static class MantenimientoServiceMock implements MantenimientoService {
        private ObservableList<Mantenimiento> mantenimientos;
        
        public MantenimientoServiceMock() {
            // Datos de prueba
            mantenimientos = FXCollections.observableArrayList(
                new Mantenimiento(1, "Cambio de aceite", LocalDate.now().minusDays(30), 101, "Completado"),
                new Mantenimiento(2, "Revisión de frenos", LocalDate.now().minusDays(15), 102, "Pendiente"),
                new Mantenimiento(3, "Cambio de neumáticos", LocalDate.now().minusDays(10), 101, "En proceso"),
                new Mantenimiento(4, "Inspección general", LocalDate.now().minusDays(5), 103, "Completado"),
                new Mantenimiento(5, "Reparación de motor", LocalDate.now(), 102, "Programado")
            );
        }
        
        @Override
        public ObservableList<Mantenimiento> obtenerMantenimientos() throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("obtener");
            }
            
            // Ordenar por fecha (más reciente primero)
            FXCollections.sort(mantenimientos, (m1, m2) -> 
                m2.getFecha_mantenimiento().compareTo(m1.getFecha_mantenimiento()));
                
            return mantenimientos;
        }
        
        @Override
        public ObservableList<Mantenimiento> obtenerMantenimientosPorBus(Integer idBus) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("obtener");
            }
            
            // Filtrar por ID de bus y ordenar por fecha
            ObservableList<Mantenimiento> resultado = FXCollections.observableArrayList();
            
            for (Mantenimiento m : mantenimientos) {
                if (m.getId_bus().equals(idBus)) {
                    resultado.add(m);
                }
            }
            
            // Ordenar por fecha (más reciente primero)
            FXCollections.sort(resultado, (m1, m2) -> 
                m2.getFecha_mantenimiento().compareTo(m1.getFecha_mantenimiento()));
                
            return resultado;
        }
        
        @Override
        public Mantenimiento obtenerMantenimientoPorId(Integer idMantenimiento) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("obtener");
            }
            
            // Buscar por ID
            for (Mantenimiento m : mantenimientos) {
                if (m.getId_mantenimiento().equals(idMantenimiento)) {
                    return m;
                }
            }
            
            return null;
        }
    }

    // Lista observable para almacenar mantenimientos
    private ObservableList<Mantenimiento> listaMantenimientos;
    
    // Tabla para mostrar mantenimientos
    private TableView<Mantenimiento> tablaMantenimientos = new TableView<>();
    
    // Label para mostrar cuando no hay datos
    private Label lblNoData = new Label("No hay mantenimientos registrados en el sistema");
    
    // Servicio para operaciones con mantenimientos
    private MantenimientoService mantenimientoService;
    
    // Elementos para filtrado
    private TextField txtFiltroBus;
    private Button btnFiltrar;
    private Button btnLimpiarFiltro;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inicializar servicio
            mantenimientoService = new MantenimientoServiceMock();
            
            // Configurar el escenario
            primaryStage.setTitle("Gestión de Mantenimientos");
            primaryStage.setScene(crearEscena(primaryStage));
            primaryStage.show();
            
            // Manejar cierre de la ventana
            primaryStage.setOnCloseRequest(e -> {
                if (!confirmarAccion("Confirmar salida", "¿Está seguro de que desea salir?")) {
                    e.consume(); // Cancelar cierre si el usuario dice que no
                }
            });
            
            // Cargar datos iniciales
            cargarDatos();
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de inicialización", "Error al iniciar la aplicación: " + ex.getMessage());
        }
    }
    
    public Scene crearEscena(Stage stage) {
        try {
            // Crear la raíz de la interfaz
            VBox root = new VBox(20);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);
            root.getStyleClass().add("root-container");
    
            // Título de la sección
            Label titulo = new Label("Gestión de Mantenimientos");
            titulo.getStyleClass().add("titulo-principal");
    
            // Crear filtro por ID de bus
            HBox filtroBox = crearFiltro();
            
            // Configurar tabla
            configurarTabla();
            
            // Botones de acción
            HBox botonesAccion = crearBotonesAccion();
    
            // Botón para volver
            Button volverBtn = new Button("Volver al menú principal");
            volverBtn.getStyleClass().add("boton-volver");
            volverBtn.setOnAction(e -> {
                // Aquí iría la lógica para volver al menú principal
                mostrarInfo("Navegación", "Volviendo al menú principal...");
            });
    
            // Contenedor para la tabla con mensaje de no datos
            StackPane contenedorTabla = new StackPane(tablaMantenimientos, lblNoData);
            lblNoData.setVisible(false); // Inicialmente oculto
    
            // Añadir todos los elementos a la raíz
            root.getChildren().addAll(titulo, filtroBox, contenedorTabla, botonesAccion, volverBtn);
    
            // Crear la escena
            Scene scene = new Scene(root, 800, 600);
    
            try {
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception ex) {
                registrarExcepcion(ex);
                mostrarAlerta("Error de estilos", "No se pudo cargar la hoja de estilos: " + ex.getMessage());
            }
            return scene;
    
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de inicialización", "Error al crear la escena: " + ex.getMessage());
            return new Scene(new VBox(new Label("Error al cargar la interfaz")), 400, 300);
        }
    }
    
    private void configurarTabla() {
        try {
            // Configurar columnas
            TableColumn<Mantenimiento, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(new PropertyValueFactory<>("id_mantenimiento"));
            colId.setPrefWidth(50);
            colId.getStyleClass().add("columna-tabla");
            
            TableColumn<Mantenimiento, String> colDescripcion = new TableColumn<>("Descripción");
            colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            colDescripcion.setPrefWidth(200);
            colDescripcion.getStyleClass().add("columna-tabla");
            
            TableColumn<Mantenimiento, String> colFecha = new TableColumn<>("Fecha");
            colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
            colFecha.setPrefWidth(100);
            colFecha.getStyleClass().add("columna-tabla");
            
            TableColumn<Mantenimiento, Integer> colBus = new TableColumn<>("ID Bus");
            colBus.setCellValueFactory(new PropertyValueFactory<>("id_bus"));
            colBus.setPrefWidth(80);
            colBus.getStyleClass().add("columna-tabla");
            
            TableColumn<Mantenimiento, String> colAccion = new TableColumn<>("Estado");
            colAccion.setCellValueFactory(new PropertyValueFactory<>("accion"));
            colAccion.setPrefWidth(100);
            colAccion.getStyleClass().add("columna-tabla");
            
            // Añadir columnas a la tabla
            tablaMantenimientos.getColumns().addAll(colId, colDescripcion, colFecha, colBus, colAccion);
            
            // Permitir que la tabla ocupe todo el ancho disponible
            tablaMantenimientos.setPrefHeight(350);
            tablaMantenimientos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tablaMantenimientos.getStyleClass().add("tabla-mantenimientos");
            
            // Configurar el doble clic para mostrar detalles
            tablaMantenimientos.setRowFactory(tv -> {
                TableRow<Mantenimiento> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        try {
                            mostrarDetalleMantenimiento(row.getItem());
                        } catch (Exception e) {
                            registrarExcepcion(e);
                            mostrarAlerta("Error", "No se pudo mostrar el detalle: " + e.getMessage());
                        }
                    }
                });
                return row;
            });
            
            // Estilo para el mensaje de no datos
            lblNoData.getStyleClass().add("no-data-label");
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al configurar la tabla: " + ex.getMessage());
        }
    }
    
    private HBox crearFiltro() {
        HBox filtroBox = new HBox(10);
        filtroBox.setAlignment(Pos.CENTER);
        filtroBox.setPadding(new Insets(0, 0, 10, 0));
        filtroBox.getStyleClass().add("filtro-container");
        
        Label lblFiltro = new Label("Filtrar por ID Bus:");
        lblFiltro.getStyleClass().add("label-filtro");
        
        txtFiltroBus = new TextField();
        txtFiltroBus.setPromptText("Ingrese ID del bus");
        txtFiltroBus.setPrefWidth(150);
        txtFiltroBus.getStyleClass().add("campo-texto");
        
        btnFiltrar = new Button("Filtrar");
        btnFiltrar.getStyleClass().add("boton-filtrar");
        btnFiltrar.setOnAction(e -> filtrarMantenimientos());
        
        btnLimpiarFiltro = new Button("Limpiar filtro");
        btnLimpiarFiltro.getStyleClass().add("boton-limpiar");
        btnLimpiarFiltro.setOnAction(e -> {
            txtFiltroBus.clear();
            cargarDatos();
        });
        
        filtroBox.getChildren().addAll(lblFiltro, txtFiltroBus, btnFiltrar, btnLimpiarFiltro);
        
        return filtroBox;
    }
    
    private HBox crearBotonesAccion() {
        HBox botonesAccion = new HBox(15);
        
        try {
            botonesAccion.setAlignment(Pos.CENTER);
            botonesAccion.setPadding(new Insets(10, 0, 10, 0));
            botonesAccion.getStyleClass().add("botones-container");
            
            Button btnVerDetalle = new Button("Ver Detalle");
            btnVerDetalle.getStyleClass().add("boton-ver-detalle");
            
            Button btnActualizar = new Button("Actualizar Datos");
            btnActualizar.getStyleClass().add("boton-actualizar");
            
            btnVerDetalle.setOnAction(e -> {
                try {
                    Mantenimiento mantenimientoSeleccionado = tablaMantenimientos.getSelectionModel().getSelectedItem();
                    if (mantenimientoSeleccionado == null) {
                        throw new NoSeleccionException();
                    }
                    mostrarDetalleMantenimiento(mantenimientoSeleccionado);
                } catch (MantenimientoException ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error", ex.getMessage());
                }
            });
            
            btnActualizar.setOnAction(e -> cargarDatos());
            
            botonesAccion.getChildren().addAll(btnVerDetalle, btnActualizar);
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al crear los botones: " + ex.getMessage());
        }
        
        return botonesAccion;
    }
    
    private void cargarDatos() {
        try {
            // Obtener datos desde el servicio
            listaMantenimientos = mantenimientoService.obtenerMantenimientos();
            
            // Asignar datos a la tabla
            tablaMantenimientos.setItems(listaMantenimientos);
            
            // Verificar si hay datos para mostrar el mensaje correspondiente
            actualizarEstadoNoData();
            
        } catch (BaseDatosException ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de conexión", "No se pudieron cargar los datos: " + ex.getMessage());
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error inesperado", "Error al cargar datos: " + ex.getMessage());
        }
    }
    
    private void filtrarMantenimientos() {
        try {
            String idBusStr = txtFiltroBus.getText().trim();
            
            if (idBusStr.isEmpty()) {
                cargarDatos();
                return;
            }
            
            try {
                Integer idBus = Integer.parseInt(idBusStr);
                listaMantenimientos = mantenimientoService.obtenerMantenimientosPorBus(idBus);
                tablaMantenimientos.setItems(listaMantenimientos);
                actualizarEstadoNoData();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error de formato", "El ID del bus debe ser un número entero");
            }
            
        } catch (BaseDatosException ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de conexión", "No se pudieron filtrar los datos: " + ex.getMessage());
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error inesperado", "Error al filtrar datos: " + ex.getMessage());
        }
    }
    
    private void mostrarDetalleMantenimiento(Mantenimiento mantenimiento) {
        try {
            // Crear un diálogo modal
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Detalle de Mantenimiento");
            dialog.setHeaderText("Información del mantenimiento #" + mantenimiento.getId_mantenimiento());
            
            // Configurar botones
            ButtonType btnCerrar = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(btnCerrar);
            
            // Crear formulario de detalle
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(20, 20, 10, 20));
            grid.getStyleClass().add("detalle-grid");
            
            // Crear labels para mostrar información
            Label lblIdTitulo = new Label("ID Mantenimiento:");
            lblIdTitulo.getStyleClass().add("label-titulo");
            Label lblId = new Label(mantenimiento.getId_mantenimiento().toString());
            lblId.getStyleClass().add("label-valor");
            
            Label lblDescripcionTitulo = new Label("Descripción:");
            lblDescripcionTitulo.getStyleClass().add("label-titulo");
            Label lblDescripcion = new Label(mantenimiento.getDescripcion());
            lblDescripcion.getStyleClass().add("label-valor");
            lblDescripcion.setWrapText(true);
            
            Label lblFechaTitulo = new Label("Fecha:");
            lblFechaTitulo.getStyleClass().add("label-titulo");
            Label lblFecha = new Label(mantenimiento.getFechaFormateada());
            lblFecha.getStyleClass().add("label-valor");
            
            Label lblBusTitulo = new Label("ID Bus:");
            lblBusTitulo.getStyleClass().add("label-titulo");
            Label lblBus = new Label(mantenimiento.getId_bus().toString());
            lblBus.getStyleClass().add("label-valor");
            
            Label lblAccionTitulo = new Label("Estado:");
            lblAccionTitulo.getStyleClass().add("label-titulo");
            Label lblAccion = new Label(mantenimiento.getAccion());
            lblAccion.getStyleClass().add("label-valor");
            
            // Configurar apariencia según estado
            switch (mantenimiento.getAccion().toLowerCase()) {
                case "completado":
                    lblAccion.getStyleClass().add("estado-completado");
                    break;
                case "pendiente":
                    lblAccion.getStyleClass().add("estado-pendiente");
                    break;
                case "en proceso":
                    lblAccion.getStyleClass().add("estado-en-proceso");
                    break;
                case "programado":
                    lblAccion.getStyleClass().add("estado-programado");
                    break;
                default:
                    break;
            }
            
            // Añadir elementos al grid
            grid.add(lblIdTitulo, 0, 0);
            grid.add(lblId, 1, 0);
            
            grid.add(lblBusTitulo, 0, 1);
            grid.add(lblBus, 1, 1);
            
            grid.add(lblFechaTitulo, 0, 2);
            grid.add(lblFecha, 1, 2);
            
            grid.add(lblAccionTitulo, 0, 3);
            grid.add(lblAccion, 1, 3);
            
            grid.add(lblDescripcionTitulo, 0, 4);
            grid.add(lblDescripcion, 1, 4);
            
            // Añadir estilos al diálogo
            dialog.getDialogPane().getStyleClass().add("detalle-dialog");
            
            dialog.getDialogPane().setContent(grid);
            
            // Aplicar la hoja de estilos del proyecto al diálogo
            try {
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception ex) {
                registrarExcepcion(ex);
                mostrarAlerta("Error de estilos", "No se pudo cargar la hoja de estilos: " + ex.getMessage());
            }
            
            // Mostrar diálogo
            dialog.showAndWait();
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error", "Error al mostrar los detalles: " + ex.getMessage());
        }
    }
    
    private void actualizarEstadoNoData() {
        if (listaMantenimientos == null || listaMantenimientos.isEmpty()) {
            lblNoData.setVisible(true);
            tablaMantenimientos.setVisible(false);
        } else {
            lblNoData.setVisible(false);
            tablaMantenimientos.setVisible(true);
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        
        // Aplicar estilos a la alerta
        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception ex) {
            System.err.println("Error al cargar estilos para alerta: " + ex.getMessage());
        }
        dialogPane.getStyleClass().add("alerta-error");
        
        alert.showAndWait();
    }
    
    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        
        // Aplicar estilos a la alerta
        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception ex) {
            System.err.println("Error al cargar estilos para alerta: " + ex.getMessage());
        }
        dialogPane.getStyleClass().add("alerta-info");
        
        alert.showAndWait();
    }
    
    private boolean confirmarAccion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        
        // Aplicar estilos a la alerta
        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception ex) {
            System.err.println("Error al cargar estilos para alerta: " + ex.getMessage());
        }
        dialogPane.getStyleClass().add("alerta-confirmacion");
        
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    
    private void registrarExcepcion(Exception ex) {
        // Solo registrar en consola
        System.err.println("Excepción capturada: " + ex.getMessage());
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        launch(args);

    }
}