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
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.Optional;

public class GestionBuses extends Application {

    public static class BusException extends Exception {
        public BusException(String message) {
            super(message);
        }
    }
    
    public static class PlacaDuplicadaException extends BusException {
        public PlacaDuplicadaException() {
            super("La placa del bus ya existe en el sistema.");
        }
    }
    
    public static class CampoVacioException extends BusException {
        public CampoVacioException(String campo) {
            super("El campo " + campo + " no puede estar vacío.");
        }
    }
    
    public static class FormatoIncorrectoException extends BusException {
        public FormatoIncorrectoException(String campo, String formato) {
            super("El formato del campo " + campo + " es incorrecto. " + formato);
        }
    }
    
    public static class NoSeleccionException extends BusException {
        public NoSeleccionException() {
            super("No hay ningún bus seleccionado.");
        }
    }
    
    public static class BaseDatosException extends BusException {
        public BaseDatosException(String operacion) {
            super("Error al " + operacion + " datos en la base de datos.");
        }
    }

    // Modelo de datos para Bus
    public static class Bus {
        private String placa;
        private String marca;
        private String modelo;
        private Integer capacidad;
        private Integer id_tipo_bus;
        private Integer id_empresa;
        private Integer id_empleado;
        private String estado;

        public Bus(String placa, String marca, String modelo, Integer capacidad, 
                  Integer id_tipo_bus, Integer id_empresa, Integer id_empleado, String estado) {
            this.placa = placa;
            this.marca = marca;
            this.modelo = modelo;
            this.capacidad = capacidad;
            this.id_tipo_bus = id_tipo_bus;
            this.id_empresa = id_empresa;
            this.id_empleado = id_empleado;
            this.estado = estado;
        }

        // Getters y setters
        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }

        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }

        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }

        public Integer getCapacidad() { return capacidad; }
        public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

        public Integer getId_tipo_bus() { return id_tipo_bus; }
        public void setId_tipo_bus(Integer id_tipo_bus) { this.id_tipo_bus = id_tipo_bus; }

        public Integer getId_empresa() { return id_empresa; }
        public void setId_empresa(Integer id_empresa) { this.id_empresa = id_empresa; }

        public Integer getId_empleado() { return id_empleado; }
        public void setId_empleado(Integer id_empleado) { this.id_empleado = id_empleado; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }

    // Interfaz para gestionar operaciones con la API/Base de datos
    public interface BusService {
        ObservableList<Bus> obtenerBuses() throws BaseDatosException;
        ObservableList<Bus> obtenerBusesPorEstado(String estado) throws BaseDatosException;
        void agregarBus(Bus bus) throws BaseDatosException;
        void actualizarBus(Bus bus) throws BaseDatosException;
        void eliminarBus(String placa) throws BaseDatosException;
    }

    // Implementación mock del servicio para pruebas
    public static class BusServiceMock implements BusService {
        private ObservableList<Bus> buses = FXCollections.observableArrayList();
        
        public BusServiceMock() {
            // Datos de ejemplo para pruebas
            buses.add(new Bus("ABC123", "Mercedes", "Sprinter", 20, 1, 1, 1, "Activo"));
            buses.add(new Bus("XYZ789", "Volvo", "B12", 45, 2, 2, 2, "Inactivo"));
            buses.add(new Bus("DEF456", "Scania", "K380", 50, 1, 1, 3, "Mantenimiento"));
        }
        
        @Override
        public ObservableList<Bus> obtenerBuses() throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("obtener");
            }
            return buses;
        }
        
        @Override
        public ObservableList<Bus> obtenerBusesPorEstado(String estado) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("filtrar");
            }
            
            ObservableList<Bus> busesFiltrados = FXCollections.observableArrayList();
            for (Bus bus : buses) {
                if (bus.getEstado().equals(estado)) {
                    busesFiltrados.add(bus);
                }
            }
            return busesFiltrados;
        }
        
        @Override
        public void agregarBus(Bus bus) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("agregar");
            }
            buses.add(bus);
        }
        
        @Override
        public void actualizarBus(Bus bus) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("actualizar");
            }
            
            // Buscar y actualizar
            for (int i = 0; i < buses.size(); i++) {
                if (buses.get(i).getPlaca().equals(bus.getPlaca())) {
                    buses.set(i, bus);
                    return;
                }
            }
        }
        
        @Override
        public void eliminarBus(String placa) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("eliminar");
            }
            
            // Buscar y eliminar
            buses.removeIf(b -> b.getPlaca().equals(placa));
        }
    }

    // Constantes para validación
    private static final Pattern PLACA_PATTERN = 
        Pattern.compile("^[A-Z0-9]{6,7}$");

    // Lista observable para almacenar buses
    private ObservableList<Bus> listaBuses;
    
    // Tabla para mostrar buses
    private TableView<Bus> tablaBuses = new TableView<>();
    
    // Label para mostrar cuando no hay datos
    private Label lblNoData = new Label("No hay buses registrados en el sistema");
    
    // Servicio para operaciones con buses
    private BusService busService;
    
    // ComboBox para filtrar por estado
    private ComboBox<String> cbEstadoFiltro = new ComboBox<>();

    public Scene crearEscena(Stage stage) {
        try {
            busService = new BusServiceMock();
            cargarDatos();
    
            VBox root = new VBox(20);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);
    
            Label titulo = new Label("Gestión de Buses");
            titulo.setId("titulo");
    
            // Crear el ComboBox de filtro
            HBox filtroBox = new HBox(10);
            filtroBox.setAlignment(Pos.CENTER_LEFT);
            Label lblFiltro = new Label("Filtrar por estado:");
            cbEstadoFiltro.getItems().addAll("Todos", "Activo", "Inactivo", "Mantenimiento");
            cbEstadoFiltro.setValue("Todos");
            cbEstadoFiltro.setOnAction(e -> filtrarPorEstado());
            filtroBox.getChildren().addAll(lblFiltro, cbEstadoFiltro);
            
            configurarTabla();
            HBox botonesAccion = crearBotonesAccion();
    
            Button volverBtn = new Button("Volver a la Página Principal");
            volverBtn.setOnAction(e -> {
            Scene escenapaginaPrincipal = paginaPrincipal.crearEscena(stage); 
            App.cambiarEscena(escenapaginaPrincipal, "Página Principal");
                
            });
    
            StackPane contenedorTabla = new StackPane(tablaBuses, lblNoData);
            lblNoData.setVisible(false); // Inicialmente oculto
    
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
    

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inicializar servicio
            busService = new BusServiceMock();
            
            // Configurar el escenario
            primaryStage.setTitle("Gestión de Buses");
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

    
    private void configurarTabla() {
        try {
            // Configurar columnas
            TableColumn<Bus, String> colPlaca = new TableColumn<>("Placa");
            colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
            colPlaca.setPrefWidth(80);
            
            TableColumn<Bus, String> colMarca = new TableColumn<>("Marca");
            colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
            colMarca.setPrefWidth(100);
            
            TableColumn<Bus, String> colModelo = new TableColumn<>("Modelo");
            colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
            colModelo.setPrefWidth(100);
            
            TableColumn<Bus, Integer> colCapacidad = new TableColumn<>("Capacidad");
            colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
            colCapacidad.setPrefWidth(80);
            
            TableColumn<Bus, Integer> colTipoBus = new TableColumn<>("Tipo Bus");
            colTipoBus.setCellValueFactory(new PropertyValueFactory<>("id_tipo_bus"));
            colTipoBus.setPrefWidth(80);
            
            TableColumn<Bus, Integer> colEmpresa = new TableColumn<>("Empresa");
            colEmpresa.setCellValueFactory(new PropertyValueFactory<>("id_empresa"));
            colEmpresa.setPrefWidth(80);
            
            TableColumn<Bus, Integer> colEmpleado = new TableColumn<>("Empleado");
            colEmpleado.setCellValueFactory(new PropertyValueFactory<>("id_empleado"));
            colEmpleado.setPrefWidth(80);
            
            TableColumn<Bus, String> colEstado = new TableColumn<>("Estado");
            colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            colEstado.setPrefWidth(100);
            
            // Columna de acciones
            TableColumn<Bus, Void> colAcciones = new TableColumn<>("Acciones");
            colAcciones.setPrefWidth(120);
            colAcciones.setCellFactory(param -> new TableCell<Bus, Void>() {
                private final Button btnEditar = new Button("Editar");
                private final Button btnVer = new Button("Ver");
                
                {
                    btnEditar.setOnAction(event -> {
                        Bus bus = getTableView().getItems().get(getIndex());
                        mostrarModalEditar(bus);
                    });
                    
                    btnVer.setOnAction(event -> {
                        Bus bus = getTableView().getItems().get(getIndex());
                        mostrarModalVerDetalle(bus);
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(5);
                        hbox.getChildren().addAll(btnEditar, btnVer);
                        setGraphic(hbox);
                    }
                }
            });
            
            // Añadir columnas a la tabla
            tablaBuses.getColumns().addAll(colPlaca, colMarca, colModelo, colCapacidad, 
                                         colTipoBus, colEmpresa, colEmpleado, colEstado, colAcciones);
            
            // Permitir que la tabla ocupe todo el ancho disponible
            tablaBuses.setPrefHeight(350);
            tablaBuses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            
            // Estilo para el mensaje de no datos
            lblNoData.setStyle("-fx-font-size: 16px; -fx-text-fill: #757575;");
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al configurar la tabla: " + ex.getMessage());
        }
    }
    
    private HBox crearBotonesAccion() {
        HBox botonesAccion = new HBox(10);
        
        try {
            botonesAccion.setAlignment(Pos.CENTER);
            
            Button btnAgregar = new Button("Crear Bus");
            Button btnEliminar = new Button("Eliminar Bus");
            
            btnAgregar.setOnAction(e -> mostrarModalAgregar());
            btnEliminar.setOnAction(e -> cargarDatos());
            
            botonesAccion.getChildren().addAll(btnAgregar, btnEliminar);
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al crear los botones: " + ex.getMessage());
        }
        
        return botonesAccion;
    }
    
    private void cargarDatos() {
        try {
            // Obtener datos desde el servicio
            listaBuses = busService.obtenerBuses();
            
            // Asignar datos a la tabla
            tablaBuses.setItems(listaBuses);
            
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
    
    private void filtrarPorEstado() {
        try {
            String estadoSeleccionado = cbEstadoFiltro.getValue();
            
            if ("Todos".equals(estadoSeleccionado)) {
                cargarDatos();
            } else {
                listaBuses = busService.obtenerBusesPorEstado(estadoSeleccionado);
                tablaBuses.setItems(listaBuses);
                actualizarEstadoNoData();
            }
            
        } catch (BaseDatosException ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de filtrado", "Error al filtrar datos: " + ex.getMessage());
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error inesperado", "Error al filtrar datos: " + ex.getMessage());
        }
    }
    
    private void actualizarEstadoNoData() {
        if (listaBuses == null || listaBuses.isEmpty()) {
            lblNoData.setVisible(true);
            tablaBuses.setVisible(false);
        } else {
            lblNoData.setVisible(false);
            tablaBuses.setVisible(true);
        }
    }
    
    private void mostrarModalAgregar() {
        try {
            // Crear un diálogo modal
            Dialog<Bus> dialog = new Dialog<>();
            dialog.setTitle("Crear Bus");
            dialog.setHeaderText("Ingrese los datos del nuevo bus");
            
            // Configurar botones
            ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);
            
            // Crear campos de formulario
            TextField txtPlaca = new TextField();
            TextField txtMarca = new TextField();
            TextField txtModelo = new TextField();
            TextField txtCapacidad = new TextField();
            
            ComboBox<Integer> cbTipoBus = new ComboBox<>();
            cbTipoBus.getItems().addAll(1, 2, 3); // Aquí irían los tipos reales de bus
            
            ComboBox<Integer> cbEmpresa = new ComboBox<>();
            cbEmpresa.getItems().addAll(1, 2, 3); // Aquí irían las empresas reales
            
            ComboBox<Integer> cbEmpleado = new ComboBox<>();
            cbEmpleado.getItems().addAll(1, 2, 3, 4, 5); // Aquí irían los empleados reales
            
            ComboBox<String> cbEstado = new ComboBox<>();
            cbEstado.getItems().addAll("Activo", "Inactivo", "Mantenimiento");
            cbEstado.setValue("Activo");

            try {
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception ex) {
                registrarExcepcion(ex);
            }
            
            // Añadir campos al grid
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            
            grid.add(new Label("Placa:"), 0, 0);
            grid.add(txtPlaca, 1, 0);
            txtPlaca.setPromptText("Ej. ABC123");
            
            grid.add(new Label("Marca:"), 0, 1);
            grid.add(txtMarca, 1, 1);
            txtMarca.setPromptText("Ej. Mercedes");
            
            grid.add(new Label("Modelo:"), 0, 2);
            grid.add(txtModelo, 1, 2);
            txtModelo.setPromptText("Ej. Sprinter");
            
            grid.add(new Label("Capacidad:"), 0, 3);
            grid.add(txtCapacidad, 1, 3);
            txtCapacidad.setPromptText("Ej. 20");
            
            grid.add(new Label("Tipo de Bus:"), 0, 4);
            grid.add(cbTipoBus, 1, 4);
            
            grid.add(new Label("Empresa:"), 0, 5);
            grid.add(cbEmpresa, 1, 5);
            
            grid.add(new Label("Empleado:"), 0, 6);
            grid.add(cbEmpleado, 1, 6);
            
            grid.add(new Label("Estado:"), 0, 7);
            grid.add(cbEstado, 1, 7);
            
            dialog.getDialogPane().setContent(grid);
            
            // Convertir el resultado cuando se presiona Guardar
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validaciones
                        validarCampos(txtPlaca.getText(), txtMarca.getText(), txtModelo.getText(), 
                                txtCapacidad.getText(), cbTipoBus.getValue(), cbEmpresa.getValue(), 
                                cbEmpleado.getValue(), cbEstado.getValue());
                        
                        String placa = txtPlaca.getText().trim();
                        String marca = txtMarca.getText().trim();
                        String modelo = txtModelo.getText().trim();
                        Integer capacidad = Integer.parseInt(txtCapacidad.getText().trim());
                        Integer tipoBus = cbTipoBus.getValue();
                        Integer empresa = cbEmpresa.getValue();
                        Integer empleado = cbEmpleado.getValue();
                        String estado = cbEstado.getValue();
                        
                        // Verificar si la placa ya existe
                        boolean placaExiste = listaBuses.stream().anyMatch(b -> b.getPlaca().equals(placa));
                        if (placaExiste) {
                            throw new PlacaDuplicadaException();
                        }
                        
                        return new Bus(placa, marca, modelo, capacidad, tipoBus, empresa, empleado, estado);
                    } catch (BusException | NumberFormatException ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error de validación", ex instanceof BusException ? 
                                ex.getMessage() : "Capacidad debe ser un número entero.");
                        return null;
                    }
                }
                return null;
            });
            
            // Mostrar diálogo y procesar resultado
            Optional<Bus> resultado = dialog.showAndWait();
            
            resultado.ifPresent(bus -> {
                try {
                    // Agregar a la base de datos
                    busService.agregarBus(bus);

                    // Actualizar estado de la tabla
                    cargarDatos();
                    
                    // Mostrar mensaje de éxito
                    mostrarInfo("Éxito", "Bus agregado correctamente.");
                } catch (BaseDatosException ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error de base de datos", ex.getMessage());
                }
            });
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error", "Error al mostrar el formulario: " + ex.getMessage());
        }
    }
    
    private void mostrarModalEditar(Bus bus) {
        try {
            // Crear un diálogo modal
            Dialog<Bus> dialog = new Dialog<>();
            dialog.setTitle("Editar Bus");
            dialog.setHeaderText("Modifique los datos del bus");
            
            // Configurar botones
            ButtonType btnGuardar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);
            
            // Crear campos de formulario y poblarlos con datos existentes
            TextField txtPlaca = new TextField(bus.getPlaca());
            TextField txtMarca = new TextField(bus.getMarca());
            TextField txtModelo = new TextField(bus.getModelo());
            TextField txtCapacidad = new TextField(bus.getCapacidad().toString());
            
            ComboBox<Integer> cbTipoBus = new ComboBox<>();
            cbTipoBus.getItems().addAll(1, 2, 3); // Aquí irían los tipos reales de bus
            cbTipoBus.setValue(bus.getId_tipo_bus());
            
            ComboBox<Integer> cbEmpresa = new ComboBox<>();
            cbEmpresa.getItems().addAll(1, 2, 3); // Aquí irían las empresas reales
            cbEmpresa.setValue(bus.getId_empresa());
            
            ComboBox<Integer> cbEmpleado = new ComboBox<>();
            cbEmpleado.getItems().addAll(1, 2, 3, 4, 5); // Aquí irían los empleados reales
            cbEmpleado.setValue(bus.getId_empleado());
            
            ComboBox<String> cbEstado = new ComboBox<>();
            cbEstado.getItems().addAll("Activo", "Inactivo", "Mantenimiento");
            cbEstado.setValue(bus.getEstado());

            try {
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception ex) {
                registrarExcepcion(ex);
            }
            
            // Deshabilitar edición de la placa
            txtPlaca.setDisable(true);
            
            // Añadir campos al grid
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            
            grid.add(new Label("Placa:"), 0, 0);
            grid.add(txtPlaca, 1, 0);
            
            grid.add(new Label("Marca:"), 0, 1);
            grid.add(txtMarca, 1, 1);
            
            grid.add(new Label("Modelo:"), 0, 2);
            grid.add(txtModelo, 1, 2);
            
            grid.add(new Label("Capacidad:"), 0, 3);
            grid.add(txtCapacidad, 1, 3);
            
            grid.add(new Label("Tipo de Bus:"), 0, 4);
            grid.add(cbTipoBus, 1, 4);
            
            grid.add(new Label("Empresa:"), 0, 5);
            grid.add(cbEmpresa, 1, 5);
            
            grid.add(new Label("Empleado:"), 0, 6);
            grid.add(cbEmpleado, 1, 6);
            
            grid.add(new Label("Estado:"), 0, 7);
            grid.add(cbEstado, 1, 7);
            
            dialog.getDialogPane().setContent(grid);
            
            // Convertir el resultado cuando se presiona Guardar
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validaciones
                        validarCampos(txtPlaca.getText(), txtMarca.getText(), txtModelo.getText(), 
                                txtCapacidad.getText(), cbTipoBus.getValue(), cbEmpresa.getValue(), 
                                cbEmpleado.getValue(), cbEstado.getValue());
                        
                        String placa = txtPlaca.getText().trim();
                        String marca = txtMarca.getText().trim();
                        String modelo = txtModelo.getText().trim();
                        Integer capacidad = Integer.parseInt(txtCapacidad.getText().trim());
                        Integer tipoBus = cbTipoBus.getValue();
                        Integer empresa = cbEmpresa.getValue();
                        Integer empleado = cbEmpleado.getValue();
                        String estado = cbEstado.getValue();
                        
                        return new Bus(placa, marca, modelo, capacidad, tipoBus, empresa, empleado, estado);
                    } catch (BusException | NumberFormatException ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error de validación", ex instanceof BusException ? 
                                ex.getMessage() : "Capacidad debe ser un número entero.");
                        return null;
                    }
                }
                return null;
            });
            
            // Mostrar diálogo y procesar resultado
            Optional<Bus> resultado = dialog.showAndWait();
            
            resultado.ifPresent(busActualizado -> {
                try {
                    // Actualizar en la base de datos
                    busService.actualizarBus(busActualizado);
                    
                    // Actualizar en la lista local
                    int indice = listaBuses.indexOf(bus);
                    if (indice >= 0) {
                        listaBuses.set(indice, busActualizado);
                    }
                    
                    // Refrescar tabla
                    tablaBuses.refresh();
                    
                    // Mostrar mensaje de éxito
                    mostrarInfo("Éxito", "Bus actualizado correctamente.");
                } catch (BaseDatosException ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error de base de datos", ex.getMessage());
                }
            });

        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error", "Error al mostrar el formulario: " + ex.getMessage());
        }
    }

    private void mostrarModalVerDetalle(Bus bus) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles del Bus");
        alert.setHeaderText("Información del bus seleccionado");
        alert.setContentText(
            "Placa: " + bus.getPlaca() + "\n" +
            "Modelo: " + bus.getModelo()+ "\n" +
            "Capacidad: " + bus.getCapacidad()+ "\n" +
            "Tido de Bus: " + bus.getId_tipo_bus()+ "\n" +
            "Empresa: " + bus.getId_empresa()+ "\n" +
            "Empleado: " + bus.getId_empresa()+ 
            "Estado: " + bus.getEstado()
        );
        alert.showAndWait();
    }    

    private void eliminarBus() throws BusException {
        Bus busSeleccionado = tablaBuses.getSelectionModel().getSelectedItem();
        if (busSeleccionado == null) {
            throw new NoSeleccionException();
        }
        
        // Confirmación antes de eliminar
        if (confirmarAccion("Confirmar eliminación", "¿Está seguro de eliminar el bus con placa " + 
                busSeleccionado.getPlaca() + "?")) {
            
            try {
                // Eliminar en la base de datos
                busService.eliminarBus(busSeleccionado.getPlaca());
                
                // Eliminar de la lista local
                listaBuses.remove(busSeleccionado);
                
                // Actualizar estado de la tabla
                actualizarEstadoNoData();
                
                // Mostrar mensaje de éxito
                mostrarInfo("Éxito", "Bus eliminado correctamente.");
                
            } catch (BaseDatosException ex) {
                registrarExcepcion(ex);
                throw new BusException("Error al eliminar bus: " + ex.getMessage());
            }
        }
    }
    
    private void validarCampos(String placa, String marca, String modelo, String capacidad, 
                              Integer tipoBus, Integer empresa, Integer empleado, String estado) throws BusException {
        // Validar que los campos obligatorios no estén vacíos
        if (placa == null || placa.trim().isEmpty()) {
            throw new CampoVacioException("Placa");
        }
        if (marca == null || marca.trim().isEmpty()) {
            throw new CampoVacioException("Marca");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new CampoVacioException("Modelo");
        }
        if (capacidad == null || capacidad.trim().isEmpty()) {
            throw new CampoVacioException("Capacidad");
        }
        if (tipoBus == null) {
            throw new CampoVacioException("Tipo de Bus");
        }
        if (empresa == null) {
            throw new CampoVacioException("Empresa");
        }
        if (empleado == null) {
            throw new CampoVacioException("Empleado");
        }
        if (estado == null || estado.trim().isEmpty()) {
            throw new CampoVacioException("Estado");
        }
        
        // Validar formato de placa
        if (!PLACA_PATTERN.matcher(placa.trim()).matches()) {
            throw new FormatoIncorrectoException("Placa", "Debe contener entre 6 y 7 caracteres alfanuméricos");
        }
        
        // Validar que capacidad sea un número
        try {
            Integer.parseInt(capacidad.trim());
        } catch (NumberFormatException e) {
            throw new FormatoIncorrectoException("Capacidad", "Debe ser un número entero");
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private boolean confirmarAccion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
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