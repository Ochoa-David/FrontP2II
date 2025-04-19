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
            VBox root = new VBox(20);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);
            root.getStyleClass().add("root-container");
    
            Label titulo = new Label("Gestión de Mantenimientos");
            titulo.getStyleClass().add("titulo-principal");
    
            HBox filtroBox = crearFiltro();
            configurarTabla();
            HBox botonesAccion = crearBotonesAccion();
    
            Button volverBtn = new Button("Volver al menú principal");
            volverBtn.setOnAction(e -> {
                Scene escenaPrincipal = paginaPrincipal.crearEscena(stage); 
                App.cambiarEscena(escenaPrincipal, "Página Principal");
            });
    
            contenedorTabla = new StackPane(tablaMantenimientos);
            tablaMantenimientos.setVisible(true);
    
            root.getChildren().addAll(titulo, filtroBox, contenedorTabla, botonesAccion, volverBtn);
    
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
            tablaMantenimientos.getColumns().clear();
    
            TableColumn<Mantenimiento, Integer> colId = new TableColumn<>("ID Mantenimiento");
            colId.setCellValueFactory(new PropertyValueFactory<>("id_mantenimiento"));
            colId.setPrefWidth(120);
            colId.getStyleClass().add("columna-tabla");
            
            TableColumn<Mantenimiento, String> colTipo = new TableColumn<>("Tipo");
            colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo_mantenimiento"));
            colTipo.setPrefWidth(100);
            colTipo.getStyleClass().add("columna-tabla");
    
            TableColumn<Mantenimiento, String> colFecha = new TableColumn<>("Fecha");
            colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada")); 
            colFecha.setPrefWidth(100);
            colFecha.getStyleClass().add("columna-tabla");
    
            TableColumn<Mantenimiento, Integer> colBus = new TableColumn<>("ID Bus");
            colBus.setCellValueFactory(new PropertyValueFactory<>("id_bus"));
            colBus.setPrefWidth(80);
            colBus.getStyleClass().add("columna-tabla");
    
            TableColumn<Mantenimiento, String> colDescripcion = new TableColumn<>("Descripción");
            colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            colDescripcion.setPrefWidth(200);
            colDescripcion.getStyleClass().add("columna-tabla");
    
            tablaMantenimientos.getColumns().addAll(colId, colTipo, colFecha, colBus, colDescripcion);
            tablaMantenimientos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al configurar la tabla: " + ex.getMessage());
        }
    }
    
    private HBox crearFiltro() {
        HBox filtroBox = new HBox(10);
        filtroBox.setAlignment(Pos.CENTER);
    
        Label lblFiltro = new Label("Filtrar por ID de Bus:");
        txtFiltroBus = new TextField();
        txtFiltroBus.setPromptText("Ingrese ID del bus");
        txtFiltroBus.setPrefWidth(150);
    
        btnFiltrar = new Button("Filtrar");
        btnFiltrar.setOnAction(e -> filtrarMantenimientos());
    
        btnLimpiarFiltro = new Button("Limpiar");
        btnLimpiarFiltro.setOnAction(e -> {
            txtFiltroBus.clear();
            cargarDatos();
        });
    
        filtroBox.getChildren().addAll(lblFiltro, txtFiltroBus, btnFiltrar, btnLimpiarFiltro);
        return filtroBox;
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
    
    private HBox crearBotonesAccion() {
        HBox botonesBox = new HBox(10);
        botonesBox.setAlignment(Pos.CENTER);
    
        Button btnAgregar = new Button("Agregar Mantenimiento");
        btnAgregar.setOnAction(e -> mostrarModalAgregar());
    
        Button btnEditar = new Button("Editar Mantenimiento");
        btnEditar.setOnAction(e -> {
            Mantenimiento seleccionado = tablaMantenimientos.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                mostrarModalEditar(seleccionado);
            } else {
                mostrarAlerta("Error", "Por favor seleccione un mantenimiento para editar");
            }
        });
    
        Button btnEliminar = new Button("Eliminar Mantenimiento");
        btnEliminar.setOnAction(e -> {
            try {
                eliminarMantenimiento();
            } catch (MantenimientoException ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });
    
        Button btnDetalles = new Button("Ver Detalles");
        btnDetalles.setOnAction(e -> {
            Mantenimiento seleccionado = tablaMantenimientos.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                mostrarDetalleMantenimiento(seleccionado);
            } else {
                mostrarAlerta("Error", "Por favor seleccione un mantenimiento para ver sus detalles");
            }
        });
    
        botonesBox.getChildren().addAll(btnAgregar, btnEditar, btnEliminar, btnDetalles);
        return botonesBox;
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
            tablaMantenimientos.setVisible(true); // ← mantener visible
        } else {
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

    private void mostrarModalAgregar() {
        try {
            Dialog<Mantenimiento> dialog = new Dialog<>();
            dialog.setTitle("Agregar Mantenimiento");
            dialog.setHeaderText("Ingrese los datos del nuevo mantenimiento");

            ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

            // Crear campos de formulario
            TextField txtId = new TextField();
            TextField txtDescripcion = new TextField();
            DatePicker dateFecha = new DatePicker();
            ComboBox<Integer> comboBus = new ComboBox<>();
            ComboBox<String> comboTipo = new ComboBox<>();

            // Configurar ComboBox
            comboBus.setPromptText("Seleccione ID de bus");
            comboBus.getItems().addAll(100, 101, 102, 103, 104);
            
            comboTipo.setPromptText("Seleccione tipo de mantenimiento");
            comboTipo.getItems().addAll("Preventivo", "Correctivo", "Rutinario", "Programado");

            try {
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception ex) {
                registrarExcepcion(ex);
            }

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("ID:"), 0, 0);
            grid.add(txtId, 1, 0);
            txtId.setPromptText("ID mantenimiento (solo números)");

            grid.add(new Label("Descripción:"), 0, 1);
            grid.add(txtDescripcion, 1, 1);

            grid.add(new Label("Fecha:"), 0, 2);
            grid.add(dateFecha, 1, 2);

            grid.add(new Label("ID Bus:"), 0, 3);
            grid.add(comboBus, 1, 3);

            grid.add(new Label("Tipo:"), 0, 4);
            grid.add(comboTipo, 1, 4);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validar campos
                        if (txtId.getText().trim().isEmpty()) {
                            throw new CampoVacioException("ID");
                        }
                        if (txtDescripcion.getText().trim().isEmpty()) {
                            throw new CampoVacioException("Descripción");
                        }
                        if (dateFecha.getValue() == null) {
                            throw new CampoVacioException("Fecha");
                        }
                        if (comboBus.getValue() == null) {
                            throw new CampoVacioException("ID Bus");
                        }
                        if (comboTipo.getValue() == null) {
                            throw new CampoVacioException("Tipo");
                        }

                        Integer id = Integer.parseInt(txtId.getText().trim());
                        String descripcion = txtDescripcion.getText().trim();
                        LocalDate fecha = dateFecha.getValue();
                        Integer idBus = comboBus.getValue();
                        String tipo = comboTipo.getValue();

                        // Verificar si existe un mantenimiento con el mismo ID
                        if (listaMantenimientos != null) {
                            for (Mantenimiento m : listaMantenimientos) {
                                if (m.getId_mantenimiento().equals(id)) {
                                    throw new IdDuplicadoException();
                                }
                            }
                        }

                        return new Mantenimiento(id, descripcion, fecha, idBus, tipo);
                    } catch (MantenimientoException | NumberFormatException ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error de validación", ex instanceof MantenimientoException ?
                                ex.getMessage() : "El ID debe ser un número entero.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Mantenimiento> resultado = dialog.showAndWait();

            resultado.ifPresent(mantenimiento -> {
                try {
                    // Agregar a la lista local
                    if (listaMantenimientos == null) {
                        listaMantenimientos = FXCollections.observableArrayList();
                    }
                    listaMantenimientos.add(mantenimiento);
                    
                    // Actualizar la tabla
                    tablaMantenimientos.setItems(listaMantenimientos);
                    actualizarEstadoNoData();
                    
                    mostrarInfo("Éxito", "Mantenimiento agregado correctamente.");
                } catch (Exception ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error", "Error al agregar el mantenimiento: " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error", "Error al mostrar el formulario: " + ex.getMessage());
        }
    }

    private void mostrarModalEditar(Mantenimiento mantenimiento) {
        try {
            Dialog<Mantenimiento> dialog = new Dialog<>();
            dialog.setTitle("Editar Mantenimiento");
            dialog.setHeaderText("Modifique los datos del mantenimiento");

            ButtonType btnGuardar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

            // Crear campos de formulario y poblarlos con datos existentes
            TextField txtId = new TextField(mantenimiento.getId_mantenimiento().toString());
            TextField txtDescripcion = new TextField(mantenimiento.getDescripcion());
            DatePicker dateFecha = new DatePicker(mantenimiento.getFecha_mantenimiento());
            ComboBox<Integer> comboBus = new ComboBox<>();
            ComboBox<String> comboTipo = new ComboBox<>();

            // Configurar ComboBox
            comboBus.setPromptText("Seleccione ID de bus");
            comboBus.getItems().addAll(100, 101, 102, 103, 104);
            comboBus.setValue(mantenimiento.getId_bus());
            
            comboTipo.setPromptText("Seleccione tipo de mantenimiento");
            comboTipo.getItems().addAll("Preventivo", "Correctivo", "Rutinario", "Programado");
            comboTipo.setValue(mantenimiento.getTipo_mantenimiento());

            try {
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception ex) {
                registrarExcepcion(ex);
            }

            // Deshabilitar edición del ID
            txtId.setDisable(true);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("ID:"), 0, 0);
            grid.add(txtId, 1, 0);

            grid.add(new Label("Descripción:"), 0, 1);
            grid.add(txtDescripcion, 1, 1);

            grid.add(new Label("Fecha:"), 0, 2);
            grid.add(dateFecha, 1, 2);

            grid.add(new Label("ID Bus:"), 0, 3);
            grid.add(comboBus, 1, 3);

            grid.add(new Label("Tipo:"), 0, 4);
            grid.add(comboTipo, 1, 4);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validar campos
                        if (txtDescripcion.getText().trim().isEmpty()) {
                            throw new CampoVacioException("Descripción");
                        }
                        if (dateFecha.getValue() == null) {
                            throw new CampoVacioException("Fecha");
                        }
                        if (comboBus.getValue() == null) {
                            throw new CampoVacioException("ID Bus");
                        }
                        if (comboTipo.getValue() == null) {
                            throw new CampoVacioException("Tipo");
                        }

                        Integer id = Integer.parseInt(txtId.getText().trim());
                        String descripcion = txtDescripcion.getText().trim();
                        LocalDate fecha = dateFecha.getValue();
                        Integer idBus = comboBus.getValue();
                        String tipo = comboTipo.getValue();

                        return new Mantenimiento(id, descripcion, fecha, idBus, tipo);
                    } catch (MantenimientoException | NumberFormatException ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error de validación", ex instanceof MantenimientoException ?
                                ex.getMessage() : "Error en el formato de los datos.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Mantenimiento> resultado = dialog.showAndWait();

            resultado.ifPresent(mantenimientoActualizado -> {
                try {
                    // Actualizar en la lista local
                    int indice = listaMantenimientos.indexOf(mantenimiento);
                    if (indice >= 0) {
                        listaMantenimientos.set(indice, mantenimientoActualizado);
                    }
                    
                    // Refrescar tabla
                    tablaMantenimientos.refresh();
                    
                    mostrarInfo("Éxito", "Mantenimiento actualizado correctamente.");
                } catch (Exception ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error", "Error al actualizar el mantenimiento: " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error", "Error al mostrar el formulario: " + ex.getMessage());
        }
    }

    private void eliminarMantenimiento() throws MantenimientoException {
        Mantenimiento mantenimientoSeleccionado = tablaMantenimientos.getSelectionModel().getSelectedItem();
        if (mantenimientoSeleccionado == null) {
            throw new NoSeleccionException();
        }

        // Confirmación antes de eliminar
        if (confirmarAccion("Confirmar eliminación", "¿Está seguro de eliminar el mantenimiento #" + 
                mantenimientoSeleccionado.getId_mantenimiento() + "?")) {
            
            try {
                // Eliminar de la lista local
                listaMantenimientos.remove(mantenimientoSeleccionado);
                
                // Actualizar estado de la tabla
                actualizarEstadoNoData();
                
                // Mostrar mensaje de éxito
                mostrarInfo("Éxito", "Mantenimiento eliminado correctamente.");
                
            } catch (Exception ex) {
                registrarExcepcion(ex);
                throw new MantenimientoException("Error al eliminar mantenimiento: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}