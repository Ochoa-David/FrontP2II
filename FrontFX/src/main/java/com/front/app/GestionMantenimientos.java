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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GestionMantenimientos extends Application {

    public static class BaseDatosException extends Exception {
        public BaseDatosException(String operacion) {
            super("Error al " + operacion + " datos en la base de datos.");
        }
    }
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
    
    public static class Mantenimiento {
        private Integer id_mantenimiento;
        private String descripcion;
        private LocalDate fecha_mantenimiento;
        private Integer id_bus;
        private String tipo_mantenimiento;

        public Mantenimiento(Integer id_mantenimiento, String descripcion, LocalDate fecha_mantenimiento, Integer id_bus, String tipo_mantenimiento) {
            this.id_mantenimiento = id_mantenimiento;
            this.descripcion = descripcion;
            this.fecha_mantenimiento = fecha_mantenimiento;
            this.id_bus = id_bus;
            this.tipo_mantenimiento = tipo_mantenimiento;
        }

        public Integer getId_mantenimiento() { return id_mantenimiento; }
        public String getDescripcion() { return descripcion; }
        public LocalDate getFecha_mantenimiento() { return fecha_mantenimiento; }
        public Integer getId_bus() { return id_bus; }
        public String getTipo_mantenimiento() { return tipo_mantenimiento; }

        public String getFechaFormateada() {
            if (fecha_mantenimiento == null) return "";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return fecha_mantenimiento.format(formatter);
        }
    }

    public interface MantenimientoService {
        ObservableList<Mantenimiento> obtenerMantenimientos() throws BaseDatosException;
        ObservableList<Mantenimiento> obtenerMantenimientosPorBus(Integer idBus) throws BaseDatosException;
        Mantenimiento obtenerMantenimientoPorId(Integer idMantenimiento) throws BaseDatosException;
        String obtenerMarcaBus(Integer idBus);
    }

    public static class MantenimientoServiceMock implements MantenimientoService {
        // Inicializar con una lista vacía
        private ObservableList<Mantenimiento> mantenimientos = FXCollections.observableArrayList();
        
        // Map para simular la relación entre ID de bus y marca
        private Map<Integer, String> busMarcaMap = new HashMap<Integer, String>() {{
            put(100, "Mercedes Benz");
            put(101, "Volvo");
            put(102, "Scania");
            put(103, "MAN");
            put(104, "Iveco");
        }};

        @Override
        public ObservableList<Mantenimiento> obtenerMantenimientos() throws BaseDatosException {
            if (!mantenimientos.isEmpty()) {
                FXCollections.sort(mantenimientos, (m1, m2) -> m2.getFecha_mantenimiento().compareTo(m1.getFecha_mantenimiento()));
            }
            return mantenimientos;
        }

        @Override
        public ObservableList<Mantenimiento> obtenerMantenimientosPorBus(Integer idBus) throws BaseDatosException {
            ObservableList<Mantenimiento> resultado = FXCollections.observableArrayList();
            for (Mantenimiento m : mantenimientos) {
                if (m.getId_bus().equals(idBus)) {
                    resultado.add(m);
                }
            }
            if (!resultado.isEmpty()) {
                FXCollections.sort(resultado, (m1, m2) -> m2.getFecha_mantenimiento().compareTo(m1.getFecha_mantenimiento()));
            }
            return resultado;
        }

        @Override
        public Mantenimiento obtenerMantenimientoPorId(Integer idMantenimiento) throws BaseDatosException {
            for (Mantenimiento m : mantenimientos) {
                if (m.getId_mantenimiento().equals(idMantenimiento)) {
                    return m;
                }
            }
            return null;
        }
        
        @Override
        public String obtenerMarcaBus(Integer idBus) {
            return busMarcaMap.getOrDefault(idBus, "Desconocida");
        }
    }

    private ObservableList<Mantenimiento> listaMantenimientos;
    private TableView<Mantenimiento> tablaMantenimientos = new TableView<>();
    private Label lblNoData = new Label("No hay mantenimientos registrados en el sistema");
    private MantenimientoService mantenimientoService = new MantenimientoServiceMock();
    private TextField txtFiltroBus;
    private Button btnFiltrar;
    private Button btnLimpiarFiltro;
    private StackPane contenedorTabla;

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
            
            // Cargar datos iniciales automáticamente
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
    
            Button volverBtn = new Button("Volver al menú principal");
            volverBtn.setOnAction(e -> {
                Scene escenaPrincipal = paginaPrincipal.crearEscena(stage); 
                App.cambiarEscena(escenaPrincipal, "Página Principal");
            });
    
            // Contenedor para la tabla con mensaje de no datos
            contenedorTabla = new StackPane(tablaMantenimientos, lblNoData);
            // Inicialmente mostrar mensaje de no datos
            lblNoData.setVisible(true);
            tablaMantenimientos.setVisible(true);
    
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
            // Limpiar columnas existentes (por si ya estaban agregadas)
            tablaMantenimientos.getColumns().clear();
    
            // Columna ID de mantenimiento
            TableColumn<Mantenimiento, Integer> colId = new TableColumn<>("ID Mantenimiento");
            colId.setCellValueFactory(new PropertyValueFactory<>("id_mantenimiento"));
            colId.setPrefWidth(120);
            colId.getStyleClass().add("columna-tabla");
            
            // Columna Tipo de mantenimiento
            TableColumn<Mantenimiento, String> colTipo = new TableColumn<>("Tipo");
            colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo_mantenimiento"));
            colTipo.setPrefWidth(100);
            colTipo.getStyleClass().add("columna-tabla");
    
            // Columna Fecha
            TableColumn<Mantenimiento, String> colFecha = new TableColumn<>("Fecha");
            colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada")); 
            colFecha.setPrefWidth(100);
            colFecha.getStyleClass().add("columna-tabla");
    
            // Columna ID del bus
            TableColumn<Mantenimiento, Integer> colBus = new TableColumn<>("ID Bus");
            colBus.setCellValueFactory(new PropertyValueFactory<>("id_bus"));
            colBus.setPrefWidth(80);
            colBus.getStyleClass().add("columna-tabla");
    
            // Añadir columnas
            tablaMantenimientos.getColumns().addAll(colId, colTipo, colFecha, colBus);
            
           

            tablaMantenimientos.setPrefHeight(350);
            tablaMantenimientos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tablaMantenimientos.getStyleClass().add("tabla-mantenimientos");

            tablaMantenimientos.setPlaceholder(new Label("No hay mantenimientos registrados"));
    
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
    
            lblNoData.getStyleClass().add("no-data-label");
            // Hacer el texto más grande y centrado
            lblNoData.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");
            StackPane.setAlignment(lblNoData, Pos.CENTER);
    
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
            cargarDatos(); // Recarga todos los datos automáticamente
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
    
            Button btnCrearRegistros = new Button("Crear Registros");
            btnCrearRegistros.getStyleClass().add("boton-Registros");
    
            btnCrearRegistros.setOnAction(e -> {
                Stage ventanaCrear = new Stage();
                ventanaCrear.setTitle("Nuevo Registro de Mantenimiento");
                ventanaCrear.initModality(Modality.APPLICATION_MODAL);
            
                // Campos de entrada
                Label lblMantenimiento = new Label("ID del Mantenimiento:");
                TextField txtIdMantenimiento = new TextField();
                txtIdMantenimiento.setPromptText("Ingrese ID de mantenimiento");
            
                Label lblBus = new Label("ID del Bus:");
                ComboBox<Integer> comboBus = new ComboBox<>();
                comboBus.setPromptText("Seleccione ID de bus");
                
                Label lblTipo = new Label("Tipo de Mantenimiento:");
                ComboBox<String> comboTipo = new ComboBox<>();
                comboTipo.getItems().addAll("Preventivo", "Correctivo", "Rutinario", "Programado");
                comboTipo.setPromptText("Seleccione tipo de mantenimiento");
            
                Label lblFecha = new Label("Fecha:");
                DatePicker dateFecha = new DatePicker();
            
                Label lblDescripcion = new Label("Descripción:");
                TextArea txtDescripcion = new TextArea();
                txtDescripcion.setPrefRowCount(3);
            
                // Botones
                Button btnCrear = new Button("Crear");
                Button btnCancelar = new Button("Cancelar");
            
                btnCrear.setOnAction(ev -> {
                    try {
                        // Validaciones
                        if (txtIdMantenimiento.getText().trim().isEmpty()) {
                            throw new CampoVacioException("ID de mantenimiento");
                        }
                        
                        Integer idMantenimiento;
                        try {
                            idMantenimiento = Integer.parseInt(txtIdMantenimiento.getText().trim());
                        } catch (NumberFormatException ex) {
                            mostrarAlerta("Error de formato", "El ID del mantenimiento debe ser un número entero");
                            return;
                        }
                        
                        // Verificar si existe un mantenimiento con el mismo ID
                        if (listaMantenimientos != null) {
                            for (Mantenimiento m : listaMantenimientos) {
                                if (m.getId_mantenimiento().equals(idMantenimiento)) {
                                    throw new IdDuplicadoException();
                                }
                            }
                        }
                        
                        Integer idBus = comboBus.getValue();
                        String tipo = comboTipo.getValue();
                        LocalDate fecha = dateFecha.getValue();
                        String descripcion = txtDescripcion.getText();
                    
                        if (idBus == null || tipo == null || fecha == null) {
                            mostrarAlerta("Campos vacíos", "Todos los campos son obligatorios.");
                            return;
                        }
                    
                        Mantenimiento nuevo = new Mantenimiento(
                            idMantenimiento,
                            descripcion,
                            fecha,
                            idBus,
                            tipo
                        );
                        
                        // Verificar si la lista está inicializada
                        if (listaMantenimientos == null) {
                            // Inicializar la lista si es null
                            try {
                                listaMantenimientos = mantenimientoService.obtenerMantenimientos();
                            } catch (BaseDatosException ex) {
                                listaMantenimientos = FXCollections.observableArrayList();
                                registrarExcepcion(ex);
                            }
                        }
                        
                        // Agregar el nuevo mantenimiento
                        listaMantenimientos.add(nuevo);
                        
                        // Asegurarse de que la lista se ordene correctamente
                        FXCollections.sort(listaMantenimientos, 
                            (m1, m2) -> m2.getFecha_mantenimiento().compareTo(m1.getFecha_mantenimiento()));
                        
                        // Actualizar la vista de la tabla automáticamente
                        tablaMantenimientos.setItems(listaMantenimientos);
                        tablaMantenimientos.refresh();
                        
                        // Actualizar el estado de "No hay datos"
                        actualizarEstadoNoData();
                        
                        // Mostrar confirmación al usuario
                        mostrarInfo("Registro creado", "El mantenimiento ha sido registrado correctamente.");
                        
                        ventanaCrear.close();
                    } catch (MantenimientoException ex) {
                        mostrarAlerta("Error de validación", ex.getMessage());
                    } catch (Exception ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error inesperado", "Error al crear el mantenimiento: " + ex.getMessage());
                    }
                });
            
                btnCancelar.setOnAction(ev -> ventanaCrear.close());
            
                // Layout
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(15));
            
                grid.add(lblMantenimiento, 0, 0);
                grid.add(txtIdMantenimiento, 1, 0);
                grid.add(lblBus, 0, 1);
                grid.add(comboBus, 1, 1);
                grid.add(lblTipo, 0, 2);
                grid.add(comboTipo, 1, 2);
                grid.add(lblFecha, 0, 3);
                grid.add(dateFecha, 1, 3);
                grid.add(lblDescripcion, 0, 4);
                grid.add(txtDescripcion, 1, 4);
            
                HBox botones = new HBox(10, btnCrear, btnCancelar);
                botones.setAlignment(Pos.CENTER_RIGHT);
                grid.add(botones, 1, 5);
            
                // Cargar datos a las listas desplegables
                comboBus.getItems().addAll(100, 101, 102, 103, 104);
            
                Scene scene = new Scene(grid, 500, 350);
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            
                ventanaCrear.setScene(scene);
                ventanaCrear.showAndWait();
            });
            
    
            btnVerDetalle.setOnAction(e -> {
                try {
                    Mantenimiento mantenimientoSeleccionado = tablaMantenimientos.getSelectionModel().getSelectedItem();
                    if (mantenimientoSeleccionado == null) throw new NoSeleccionException();
                    mostrarDetalleMantenimiento(mantenimientoSeleccionado);
                } catch (MantenimientoException ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error", ex.getMessage());
                }
            });
    
            botonesAccion.getChildren().addAll(btnVerDetalle, btnCrearRegistros);
    
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al crear los botones: " + ex.getMessage());
        }
    
        return botonesAccion;
    }
    
    
    private void cargarDatos() {
        try {
            listaMantenimientos = mantenimientoService.obtenerMantenimientos();
            tablaMantenimientos.setItems(listaMantenimientos);
            actualizarEstadoNoData();
            System.out.println("Datos cargados: " + (listaMantenimientos != null ? listaMantenimientos.size() : 0) + " registros");
        } catch (BaseDatosException ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de conexión", ex.getMessage());
        }
    }

    
    private void filtrarMantenimientos() {
        try {
            String idBusStr = txtFiltroBus.getText().trim();
            
            if (idBusStr.isEmpty()) {
                cargarDatos(); // Carga todos los mantenimientos
                return;
            }
            
            try {
                Integer idBus = Integer.parseInt(idBusStr);
                listaMantenimientos = mantenimientoService.obtenerMantenimientosPorBus(idBus);
                tablaMantenimientos.setItems(listaMantenimientos);
                actualizarEstadoNoData();
                System.out.println("Datos filtrados para bus ID: " + idBus + " - Resultados: " + 
                                  (listaMantenimientos != null ? listaMantenimientos.size() : 0));
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
            // Obtener la marca del bus
            String marcaBus = mantenimientoService.obtenerMarcaBus(mantenimiento.getId_bus());
            
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
            
            Label lblTipoTitulo = new Label("Tipo de Mantenimiento:");
            lblTipoTitulo.getStyleClass().add("label-titulo");
            Label lblTipo = new Label(mantenimiento.getTipo_mantenimiento());
            lblTipo.getStyleClass().add("label-valor");
            
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
            
            Label lblMarcaTitulo = new Label("Marca del Bus:");
            lblMarcaTitulo.getStyleClass().add("label-titulo");
            Label lblMarca = new Label(marcaBus);
            lblMarca.getStyleClass().add("label-valor");
            
            // Añadir elementos al grid
            grid.add(lblIdTitulo, 0, 0);
            grid.add(lblId, 1, 0);
            
            grid.add(lblTipoTitulo, 0, 1);
            grid.add(lblTipo, 1, 1);
            
            grid.add(lblBusTitulo, 0, 2);
            grid.add(lblBus, 1, 2);
            
            grid.add(lblMarcaTitulo, 0, 3);
            grid.add(lblMarca, 1, 3);
            
            grid.add(lblFechaTitulo, 0, 4);
            grid.add(lblFecha, 1, 4);
            
            grid.add(lblDescripcionTitulo, 0, 5);
            grid.add(lblDescripcion, 1, 5);
            
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
            tablaMantenimientos.setVisible(true); // ← mantener visible
        } else {
            lblNoData.setVisible(true);
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