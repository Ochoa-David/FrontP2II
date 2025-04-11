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

public class GestionEmpresas extends Application {

    // Definición de excepciones personalizadas
    public static class EmpresaException extends Exception {
        public EmpresaException(String message) {
            super(message);
        }
    }
    
    public static class IdDuplicadoException extends EmpresaException {
        public IdDuplicadoException() {
            super("El ID de empresa ya existe en el sistema.");
        }
    }
    
    public static class CampoVacioException extends EmpresaException {
        public CampoVacioException(String campo) {
            super("El campo " + campo + " no puede estar vacío.");
        }
    }
    
    public static class FormatoIncorrectoException extends EmpresaException {
        public FormatoIncorrectoException(String campo, String formato) {
            super("El formato del campo " + campo + " es incorrecto. " + formato);
        }
    }
    
    public static class NoSeleccionException extends EmpresaException {
        public NoSeleccionException() {
            super("No hay ninguna empresa seleccionada.");
        }
    }
    
    public static class BaseDatosException extends EmpresaException {
        public BaseDatosException(String operacion) {
            super("Error al " + operacion + " datos en la base de datos.");
        }
    }

    // Modelo de datos para Empresa
    public static class Empresa {
        private Integer id_empresa;
        private String nombre;
        private String telefono;
        private String correo;
        private String direccion;
        private Integer id_tipo_empresa;

        public Empresa(Integer id_empresa, String nombre, String telefono, String correo, String direccion, Integer id_tipo_empresa) {
            this.id_empresa = id_empresa;
            this.nombre = nombre;
            this.telefono = telefono;
            this.correo = correo;
            this.direccion = direccion;
            this.id_tipo_empresa = id_tipo_empresa;
        }

        // Getters y setters
        public Integer getId_empresa() { return id_empresa; }
        public void setId_empresa(Integer id_empresa) { this.id_empresa = id_empresa; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }

        public Integer getId_tipo_empresa() { return id_tipo_empresa; }
        public void setId_tipo_empresa(Integer id_tipo_empresa) { this.id_tipo_empresa = id_tipo_empresa; }
    }

    // Interfaz para gestionar operaciones con la API/Base de datos
    public interface EmpresaService {
        ObservableList<Empresa> obtenerEmpresas() throws BaseDatosException;
        void agregarEmpresa(Empresa empresa) throws BaseDatosException;
        void actualizarEmpresa(Empresa empresa) throws BaseDatosException;
        void eliminarEmpresa(Integer idEmpresa) throws BaseDatosException;
    }

    // Implementación mock del servicio para pruebas (será reemplazada por implementación real)
    public static class EmpresaServiceMock implements EmpresaService {
        private ObservableList<Empresa> empresas = FXCollections.observableArrayList();
        
        @Override
        public ObservableList<Empresa> obtenerEmpresas() throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("obtener");
            }
            return empresas;
        }
        
        @Override
        public void agregarEmpresa(Empresa empresa) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("agregar");
            }
            empresas.add(empresa);
        }
        
        @Override
        public void actualizarEmpresa(Empresa empresa) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("actualizar");
            }
            
            // Buscar y actualizar
            for (int i = 0; i < empresas.size(); i++) {
                if (empresas.get(i).getId_empresa().equals(empresa.getId_empresa())) {
                    empresas.set(i, empresa);
                    return;
                }
            }
        }
        
        @Override
        public void eliminarEmpresa(Integer idEmpresa) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("eliminar");
            }
            
            // Buscar y eliminar
            empresas.removeIf(e -> e.getId_empresa().equals(idEmpresa));
        }
    }

    // Constantes para validación
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern TELEFONO_PATTERN = 
        Pattern.compile("^[0-9()-]{6,50}$");

    // Lista observable para almacenar empresas
    private ObservableList<Empresa> listaEmpresas;
    
    // Tabla para mostrar empresas
    private TableView<Empresa> tablaEmpresas = new TableView<>();
    
    // Label para mostrar cuando no hay datos
    private Label lblNoData = new Label("No hay empresas registradas en el sistema");
    
    // Servicio para operaciones con empresas
    private EmpresaService empresaService;

    public Scene crearEscena(Stage stage) {
        try {
            empresaService = new EmpresaServiceMock(); // Usa tu servicio real si ya lo tienes implementado
            cargarDatos(); // Cargar los datos
    
            VBox root = new VBox(20);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);
    
            Label titulo = new Label("Gestión de Empresas");
            titulo.setId("titulo");
    
            configurarTabla();
            HBox botonesAccion = crearBotonesAccion();
    
            Button volverBtn = new Button("Volver al menú principal");
            volverBtn.setOnAction(e -> {
                Scene escenaPrincipal = paginaPrincipal.crearEscena(stage); 
                App.cambiarEscena(escenaPrincipal, "Página Principal");
            });
    
            StackPane contenedorTabla = new StackPane(tablaEmpresas, lblNoData);
            lblNoData.setVisible(false); // Inicialmente oculto
    
            root.getChildren().addAll(titulo, contenedorTabla, botonesAccion, volverBtn);
    
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
            empresaService = new EmpresaServiceMock();
            
            // Configurar el escenario
            primaryStage.setTitle("Gestión de Empresas");
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
            TableColumn<Empresa, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(new PropertyValueFactory<>("id_empresa"));
            colId.setPrefWidth(50);
            
            TableColumn<Empresa, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colNombre.setPrefWidth(150);
            
            TableColumn<Empresa, String> colTelefono = new TableColumn<>("Teléfono");
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            colTelefono.setPrefWidth(100);
            
            TableColumn<Empresa, String> colCorreo = new TableColumn<>("Correo");
            colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
            colCorreo.setPrefWidth(150);
            
            TableColumn<Empresa, String> colDireccion = new TableColumn<>("Dirección");
            colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
            colDireccion.setPrefWidth(150);
            
            TableColumn<Empresa, Integer> colTipoEmpresa = new TableColumn<>("Tipo");
            colTipoEmpresa.setCellValueFactory(new PropertyValueFactory<>("id_tipo_empresa"));
            colTipoEmpresa.setPrefWidth(50);
            
            // Añadir columnas a la tabla
            tablaEmpresas.getColumns().addAll(colId, colNombre, colTelefono, colCorreo, colDireccion, colTipoEmpresa);
            
            // Permitir que la tabla ocupe todo el ancho disponible
            tablaEmpresas.setPrefHeight(350);
            tablaEmpresas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            
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
            
            Button btnAgregar = new Button("Agregar Empresa");
            Button btnEditar = new Button("Editar Empresa");
            Button btnEliminar = new Button("Eliminar Empresa");
            Button btnActualizar = new Button("Actualizar Datos");
            
            btnAgregar.setOnAction(e -> mostrarModalAgregar());
            
            btnEditar.setOnAction(e -> {
                try {
                    Empresa empresaSeleccionada = tablaEmpresas.getSelectionModel().getSelectedItem();
                    if (empresaSeleccionada == null) {
                        throw new NoSeleccionException();
                    }
                    mostrarModalEditar(empresaSeleccionada);
                } catch (EmpresaException ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error", ex.getMessage());
                }
            });
            
            btnEliminar.setOnAction(e -> {
                try {
                    eliminarEmpresa();
                } catch (EmpresaException ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error al eliminar", ex.getMessage());
                } catch (Exception ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error inesperado", "Error al eliminar empresa: " + ex.getMessage());
                }
            });
            
            btnActualizar.setOnAction(e -> cargarDatos());
            
            botonesAccion.getChildren().addAll(btnAgregar, btnEditar, btnEliminar, btnActualizar);
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al crear los botones: " + ex.getMessage());
        }
        
        return botonesAccion;
    }
    
    private void cargarDatos() {
        try {
            // Obtener datos desde el servicio
            listaEmpresas = empresaService.obtenerEmpresas();
            
            // Asignar datos a la tabla
            tablaEmpresas.setItems(listaEmpresas);
            
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
    
    private void actualizarEstadoNoData() {
        if (listaEmpresas == null || listaEmpresas.isEmpty()) {
            lblNoData.setVisible(true);
            tablaEmpresas.setVisible(false);
        } else {
            lblNoData.setVisible(false);
            tablaEmpresas.setVisible(true);
        }
    }
    
    private void mostrarModalAgregar() {
        try {
            // Crear un diálogo modal
            Dialog<Empresa> dialog = new Dialog<>();
            dialog.setTitle("Agregar Empresa");
            dialog.setHeaderText("Ingrese los datos de la nueva empresa");
            
            // Configurar botones
            ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);
            
            // Crear campos de formulario
            TextField txtId = new TextField();
            TextField txtNombre = new TextField();
            TextField txtTelefono = new TextField();
            TextField txtCorreo = new TextField();
            TextField txtDireccion = new TextField();
            TextField txtTipoEmpresa = new TextField();

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
            
            grid.add(new Label("ID:"), 0, 0);
            grid.add(txtId, 1, 0);
            txtId.setPromptText("ID empresa (solo números)");
            
            grid.add(new Label("Nombre:"), 0, 1);
            grid.add(txtNombre, 1, 1);
            txtNombre.setPromptText("Nombre de la empresa");
            
            grid.add(new Label("Teléfono:"), 0, 2);
            grid.add(txtTelefono, 1, 2);
            txtTelefono.setPromptText("Teléfono de contacto");
            
            grid.add(new Label("Correo:"), 0, 3);
            grid.add(txtCorreo, 1, 3);
            txtCorreo.setPromptText("Correo electrónico");
            
            grid.add(new Label("Dirección:"), 0, 4);
            grid.add(txtDireccion, 1, 4);
            txtDireccion.setPromptText("Dirección completa");
            
            grid.add(new Label("Tipo Empresa:"), 0, 5);
            grid.add(txtTipoEmpresa, 1, 5);
            txtTipoEmpresa.setPromptText("ID del tipo de empresa");
            
            dialog.getDialogPane().setContent(grid);
            
            // Convertir el resultado cuando se presiona Guardar
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validaciones
                        validarCampos(txtId.getText(), txtNombre.getText(), txtTelefono.getText(), 
                                txtCorreo.getText(), txtDireccion.getText(), txtTipoEmpresa.getText());
                        
                        Integer id = Integer.parseInt(txtId.getText().trim());
                        String nombre = txtNombre.getText().trim();
                        String telefono = txtTelefono.getText().trim();
                        String correo = txtCorreo.getText().trim();
                        String direccion = txtDireccion.getText().trim();
                        Integer tipoEmpresa = Integer.parseInt(txtTipoEmpresa.getText().trim());
                        
                        // Verificar si el ID ya existe
                        boolean idExiste = listaEmpresas.stream().anyMatch(e -> e.getId_empresa().equals(id));
                        if (idExiste) {
                            throw new IdDuplicadoException();
                        }
                        
                        return new Empresa(id, nombre, telefono, correo, direccion, tipoEmpresa);
                    } catch (EmpresaException | NumberFormatException ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error de validación", ex instanceof EmpresaException ? 
                                ex.getMessage() : "ID y Tipo Empresa deben ser números enteros.");
                        return null;
                    }
                }
                return null;
            });
            
            // Mostrar diálogo y procesar resultado
            Optional<Empresa> resultado = dialog.showAndWait();
            
            resultado.ifPresent(empresa -> {
                try {
                    // Agregar a la base de datos
                    empresaService.agregarEmpresa(empresa);

                    // Actualizar estado de la tabla
                    actualizarEstadoNoData();
                    
                    // Mostrar mensaje de éxito
                    mostrarInfo("Éxito", "Empresa agregada correctamente.");
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
    
    private void mostrarModalEditar(Empresa empresa) {
        try {
            // Crear un diálogo modal
            Dialog<Empresa> dialog = new Dialog<>();
            dialog.setTitle("Editar Empresa");
            dialog.setHeaderText("Modifique los datos de la empresa");
            
            // Configurar botones
            ButtonType btnGuardar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);
            
            // Crear campos de formulario y poblarlos con datos existentes
            TextField txtId = new TextField(empresa.getId_empresa().toString());
            TextField txtNombre = new TextField(empresa.getNombre());
            TextField txtTelefono = new TextField(empresa.getTelefono());
            TextField txtCorreo = new TextField(empresa.getCorreo());
            TextField txtDireccion = new TextField(empresa.getDireccion());
            TextField txtTipoEmpresa = new TextField(empresa.getId_tipo_empresa().toString());

            try {
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception ex) {
                registrarExcepcion(ex);
            }
            
            // Deshabilitar edición del ID si es necesario preservarlo
            txtId.setDisable(true); // Opcionalmente se puede habilitar
            
            // Añadir campos al grid
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            
            grid.add(new Label("ID:"), 0, 0);
            grid.add(txtId, 1, 0);
            
            grid.add(new Label("Nombre:"), 0, 1);
            grid.add(txtNombre, 1, 1);
            
            grid.add(new Label("Teléfono:"), 0, 2);
            grid.add(txtTelefono, 1, 2);
            
            grid.add(new Label("Correo:"), 0, 3);
            grid.add(txtCorreo, 1, 3);
            
            grid.add(new Label("Dirección:"), 0, 4);
            grid.add(txtDireccion, 1, 4);
            
            grid.add(new Label("Tipo Empresa:"), 0, 5);
            grid.add(txtTipoEmpresa, 1, 5);
            
            dialog.getDialogPane().setContent(grid);
            
            // Convertir el resultado cuando se presiona Guardar
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validaciones
                        validarCampos(txtId.getText(), txtNombre.getText(), txtTelefono.getText(), 
                                txtCorreo.getText(), txtDireccion.getText(), txtTipoEmpresa.getText());
                        
                        Integer id = Integer.parseInt(txtId.getText().trim());
                        String nombre = txtNombre.getText().trim();
                        String telefono = txtTelefono.getText().trim();
                        String correo = txtCorreo.getText().trim();
                        String direccion = txtDireccion.getText().trim();
                        Integer tipoEmpresa = Integer.parseInt(txtTipoEmpresa.getText().trim());
                        
                        return new Empresa(id, nombre, telefono, correo, direccion, tipoEmpresa);
                    } catch (EmpresaException | NumberFormatException ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error de validación", ex instanceof EmpresaException ? 
                                ex.getMessage() : "ID y Tipo Empresa deben ser números enteros.");
                        return null;
                    }
                }
                return null;
            });
            
            // Mostrar diálogo y procesar resultado
            Optional<Empresa> resultado = dialog.showAndWait();
            
            resultado.ifPresent(empresaActualizada -> {
                try {
                    // Actualizar en la base de datos
                    empresaService.actualizarEmpresa(empresaActualizada);
                    
                    // Actualizar en la lista local
                    int indice = listaEmpresas.indexOf(empresa);
                    if (indice >= 0) {
                        listaEmpresas.set(indice, empresaActualizada);
                    }
                    
                    // Refrescar tabla
                    tablaEmpresas.refresh();
                    
                    // Mostrar mensaje de éxito
                    mostrarInfo("Éxito", "Empresa actualizada correctamente.");
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
    
    private void eliminarEmpresa() throws EmpresaException {
        Empresa empresaSeleccionada = tablaEmpresas.getSelectionModel().getSelectedItem();
        if (empresaSeleccionada == null) {
            throw new NoSeleccionException();
        }
        
        // Confirmación antes de eliminar
        if (confirmarAccion("Confirmar eliminación", "¿Está seguro de eliminar la empresa " + 
                empresaSeleccionada.getNombre() + "?")) {
            
            try {
                // Eliminar en la base de datos
                empresaService.eliminarEmpresa(empresaSeleccionada.getId_empresa());
                
                // Eliminar de la lista local
                listaEmpresas.remove(empresaSeleccionada);
                
                // Actualizar estado de la tabla
                actualizarEstadoNoData();
                
                // Mostrar mensaje de éxito
                mostrarInfo("Éxito", "Empresa eliminada correctamente.");
                
            } catch (BaseDatosException ex) {
                registrarExcepcion(ex);
                throw new EmpresaException("Error al eliminar empresa: " + ex.getMessage());
            }
        }
    }
    
    private void validarCampos(String id, String nombre, String telefono, String correo, 
                               String direccion, String tipoEmpresa) throws EmpresaException {
        // Validar que los campos obligatorios no estén vacíos
        if (id.trim().isEmpty()) {
            throw new CampoVacioException("ID");
        }
        if (nombre.trim().isEmpty()) {
            throw new CampoVacioException("Nombre");
        }
        if (telefono.trim().isEmpty()) {
            throw new CampoVacioException("Teléfono");
        }
        if (correo.trim().isEmpty()) {
            throw new CampoVacioException("Correo");
        }
        if (direccion.trim().isEmpty()) {
            throw new CampoVacioException("Dirección");
        }
        if (tipoEmpresa.trim().isEmpty()) {
            throw new CampoVacioException("Tipo Empresa");
        }
        
        // Validar formato de correo electrónico
        if (!EMAIL_PATTERN.matcher(correo.trim()).matches()) {
            throw new FormatoIncorrectoException("Correo", "Formato de correo electrónico inválido.");
        }
        
        // Validar formato de teléfono
        if (!TELEFONO_PATTERN.matcher(telefono.trim()).matches()) {
            throw new FormatoIncorrectoException("Teléfono", "Debe contener entre 6 y 50 caracteres, solo números y símbolos ()-.");
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