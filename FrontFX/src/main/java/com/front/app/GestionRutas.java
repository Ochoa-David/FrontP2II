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

public class GestionRutas extends Application {

    public static class RutaException extends Exception {
        public RutaException(String message) {
            super(message);
        }
    }
    
    public static class IdRutaDuplicadoException extends RutaException {
        public IdRutaDuplicadoException() {
            super("El ID de la ruta ya existe en el sistema.");
        }
    }
    
    public static class CampoVacioException extends RutaException {
        public CampoVacioException(String campo) {
            super("El campo " + campo + " no puede estar vacío.");
        }
    }
    
    public static class FormatoIncorrectoException extends RutaException {
        public FormatoIncorrectoException(String campo, String formato) {
            super("El formato del campo " + campo + " es incorrecto. " + formato);
        }
    }
    
    public static class NoSeleccionException extends RutaException {
        public NoSeleccionException() {
            super("No hay ninguna ruta seleccionada.");
        }
    }
    
    public static class BaseDatosException extends RutaException {
        public BaseDatosException(String operacion) {
            super("Error al " + operacion + " datos en la base de datos.");
        }
    }

    // Modelo de datos para Ruta
    public static class Ruta {
        private Integer id_ruta;
        private String nombre;
        private String destino;
        private String paradas_intermedias;

        public Ruta(Integer id_ruta, String nombre, String destino, String paradas_intermedias) {
            this.id_ruta = id_ruta;
            this.nombre = nombre;
            this.destino = destino;
            this.paradas_intermedias = paradas_intermedias;
        }

        // Getters y setters
        public Integer getId_ruta() { return id_ruta; }
        public void setId_ruta(Integer id_ruta) { this.id_ruta = id_ruta; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getDestino() { return destino; }
        public void setDestino(String destino) { this.destino = destino; }

        public String getParadas_intermedias() { return paradas_intermedias; }
        public void setParadas_intermedias(String paradas_intermedias) { this.paradas_intermedias = paradas_intermedias; }
    }

    // Interfaz para gestionar operaciones con la API/Base de datos
    public interface RutaService {
        ObservableList<Ruta> obtenerRutas() throws BaseDatosException;
        void agregarRuta(Ruta ruta) throws BaseDatosException;
        void actualizarRuta(Ruta ruta) throws BaseDatosException;
        void eliminarRuta(Integer id_ruta) throws BaseDatosException;
    }

    // Implementación mock del servicio para pruebas
    public static class RutaServiceMock implements RutaService {
        private ObservableList<Ruta> rutas = FXCollections.observableArrayList();
        
        public RutaServiceMock() {
            // Datos de ejemplo para pruebas
            rutas.add(new Ruta(1, "Ruta Centro", "Plaza Principal", "Avenida Central, Parque Municipal"));
            rutas.add(new Ruta(2, "Ruta Norte", "Terminal Norte", "Carretera 45, Gasolinera"));
            rutas.add(new Ruta(3, "Ruta Sur", "Terminal Sur", "Boulevard Sur, Centro Comercial"));
        }
        
        @Override
        public ObservableList<Ruta> obtenerRutas() throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("obtener");
            }
            return rutas;
        }
        
        @Override
        public void agregarRuta(Ruta ruta) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("agregar");
            }
            rutas.add(ruta);
        }
        
        @Override
        public void actualizarRuta(Ruta ruta) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("actualizar");
            }
            
            // Buscar y actualizar
            for (int i = 0; i < rutas.size(); i++) {
                if (rutas.get(i).getId_ruta().equals(ruta.getId_ruta())) {
                    rutas.set(i, ruta);
                    return;
                }
            }
        }
        
        @Override
        public void eliminarRuta(Integer id_ruta) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("eliminar");
            }
            
            // Buscar y eliminar
            rutas.removeIf(r -> r.getId_ruta().equals(id_ruta));
        }
    }

    // Lista observable para almacenar rutas
    private ObservableList<Ruta> listaRutas;
    
    // Tabla para mostrar rutas
    private TableView<Ruta> tablaRutas = new TableView<>();
    
    // Label para mostrar cuando no hay datos
    private Label lblNoData = new Label("No hay rutas registradas en el sistema");
    
    // Servicio para operaciones con rutas
    private RutaService rutaService;

    public Scene crearEscena(Stage stage) {
        try {
            rutaService = new RutaServiceMock();
            cargarDatos();
    
            VBox root = new VBox(20);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);
    
            Label titulo = new Label("Gestión de Rutas");
            titulo.setId("titulo");
    
            configurarTabla();
            HBox botonesAccion = crearBotonesAccion();
    
            Button volverBtn = new Button("Volver a la Página Principal");
            volverBtn.setOnAction(e -> {
                Scene escenapaginaPrincipal = paginaPrincipal.crearEscena(stage); 
                App.cambiarEscena(escenapaginaPrincipal, "Página Principal");
            });
    
            StackPane contenedorTabla = new StackPane(tablaRutas, lblNoData);
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
            rutaService = new RutaServiceMock();
            
            // Configurar el escenario
            primaryStage.setTitle("Gestión de Rutas");
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
            TableColumn<Ruta, Integer> colIdRuta = new TableColumn<>("ID Ruta");
            colIdRuta.setCellValueFactory(new PropertyValueFactory<>("id_ruta"));
            colIdRuta.setPrefWidth(80);
            
            TableColumn<Ruta, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colNombre.setPrefWidth(150);
            
            TableColumn<Ruta, String> colDestino = new TableColumn<>("Destino");
            colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
            colDestino.setPrefWidth(150);
            
            TableColumn<Ruta, String> colParadas = new TableColumn<>("Paradas Intermedias");
            colParadas.setCellValueFactory(new PropertyValueFactory<>("paradas_intermedias"));
            colParadas.setPrefWidth(250);
            
            // Columna de acciones
            TableColumn<Ruta, Void> colAcciones = new TableColumn<>("Acciones");
            colAcciones.setPrefWidth(180);
            colAcciones.setCellFactory(param -> new TableCell<Ruta, Void>() {
                private final Button btnEditar = new Button("Editar");
                private final Button btnEliminar = new Button("Eliminar");
                private final Button btnVer = new Button("Ver");
                
                {
                    btnEditar.setOnAction(event -> {
                        Ruta ruta = getTableView().getItems().get(getIndex());
                        mostrarModalEditar(ruta);
                    });
                    
                    btnEliminar.setOnAction(event -> {
                        Ruta ruta = getTableView().getItems().get(getIndex());
                        try {
                            if (confirmarAccion("Confirmar eliminación", "¿Está seguro de eliminar la ruta " + 
                                    ruta.getNombre() + "?")) {
                                rutaService.eliminarRuta(ruta.getId_ruta());
                                listaRutas.remove(ruta);
                                actualizarEstadoNoData();
                                mostrarInfo("Éxito", "Ruta eliminada correctamente.");
                            }
                        } catch (BaseDatosException ex) {
                            registrarExcepcion(ex);
                            mostrarAlerta("Error de base de datos", ex.getMessage());
                        }
                    });
                    
                    btnVer.setOnAction(event -> {
                        Ruta ruta = getTableView().getItems().get(getIndex());
                        mostrarModalVerDetalle(ruta);
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(5);
                        hbox.getChildren().addAll(btnEditar, btnEliminar, btnVer);
                        setGraphic(hbox);
                    }
                }
            });
            
            // Añadir columnas a la tabla
            tablaRutas.getColumns().addAll(colIdRuta, colNombre, colDestino, colParadas, colAcciones);
            
            // Permitir que la tabla ocupe todo el ancho disponible
            tablaRutas.setPrefHeight(350);
            tablaRutas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            
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
            
            Button btnAgregar = new Button("Crear Ruta");
            
            btnAgregar.setOnAction(e -> mostrarModalAgregar());
            
            botonesAccion.getChildren().addAll(btnAgregar);
            
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al crear los botones: " + ex.getMessage());
        }
        
        return botonesAccion;
    }
    
    private void cargarDatos() {
        try {
            // Obtener datos desde el servicio
            listaRutas = rutaService.obtenerRutas();
            
            // Asignar datos a la tabla
            tablaRutas.setItems(listaRutas);
            
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
        if (listaRutas == null || listaRutas.isEmpty()) {
            lblNoData.setVisible(true);
            tablaRutas.setVisible(false);
        } else {
            lblNoData.setVisible(false);
            tablaRutas.setVisible(true);
        }
    }
    
    private void mostrarModalAgregar() {
        try {
            // Crear un diálogo modal
            Dialog<Ruta> dialog = new Dialog<>();
            dialog.setTitle("Crear Ruta");
            dialog.setHeaderText("Ingrese los datos de la nueva ruta");
            
            // Configurar botones
            ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);
            
            // Crear campos de formulario
            TextField txtIdRuta = new TextField();
            TextField txtNombre = new TextField();
            TextField txtDestino = new TextField();
            TextArea txtParadas = new TextArea();
            txtParadas.setPrefRowCount(3);
            
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
            
            grid.add(new Label("ID Ruta:"), 0, 0);
            grid.add(txtIdRuta, 1, 0);
            txtIdRuta.setPromptText("Ej. 1");
            
            grid.add(new Label("Nombre:"), 0, 1);
            grid.add(txtNombre, 1, 1);
            txtNombre.setPromptText("Ej. Ruta Centro");
            
            grid.add(new Label("Destino:"), 0, 2);
            grid.add(txtDestino, 1, 2);
            txtDestino.setPromptText("Ej. Plaza Principal");
            
            grid.add(new Label("Paradas Intermedias:"), 0, 3);
            grid.add(txtParadas, 1, 3);
            txtParadas.setPromptText("Ej. Avenida Central, Parque Municipal");
            
            dialog.getDialogPane().setContent(grid);
            
            // Convertir el resultado cuando se presiona Guardar
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validaciones
                        validarCampos(txtIdRuta.getText(), txtNombre.getText(), txtDestino.getText(), 
                                txtParadas.getText());
                        
                        Integer idRuta = Integer.parseInt(txtIdRuta.getText().trim());
                        String nombre = txtNombre.getText().trim();
                        String destino = txtDestino.getText().trim();
                        String paradas = txtParadas.getText().trim();
                        
                        // Verificar si el ID ya existe
                        boolean idExiste = listaRutas.stream().anyMatch(r -> r.getId_ruta().equals(idRuta));
                        if (idExiste) {
                            throw new IdRutaDuplicadoException();
                        }
                        
                        return new Ruta(idRuta, nombre, destino, paradas);
                    } catch (RutaException | NumberFormatException ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error de validación", ex instanceof RutaException ? 
                                ex.getMessage() : "ID de ruta debe ser un número entero.");
                        return null;
                    }
                }
                return null;
            });
            
            // Mostrar diálogo y procesar resultado
            Optional<Ruta> resultado = dialog.showAndWait();
            
            resultado.ifPresent(ruta -> {
                try {
                    // Agregar a la base de datos
                    rutaService.agregarRuta(ruta);

                    // Actualizar estado de la tabla
                    cargarDatos();
                    
                    // Mostrar mensaje de éxito
                    mostrarInfo("Éxito", "Ruta agregada correctamente.");
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
    
    private void mostrarModalEditar(Ruta ruta) {
        try {
            // Crear un diálogo modal
            Dialog<Ruta> dialog = new Dialog<>();
            dialog.setTitle("Editar Ruta");
            dialog.setHeaderText("Modifique los datos de la ruta");
            
            // Configurar botones
            ButtonType btnGuardar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);
            
            // Crear campos de formulario y poblarlos con datos existentes
            TextField txtIdRuta = new TextField(ruta.getId_ruta().toString());
            TextField txtNombre = new TextField(ruta.getNombre());
            TextField txtDestino = new TextField(ruta.getDestino());
            TextArea txtParadas = new TextArea(ruta.getParadas_intermedias());
            txtParadas.setPrefRowCount(3);
            
            try {
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception ex) {
                registrarExcepcion(ex);
            }
            
            // Deshabilitar edición del ID
            txtIdRuta.setDisable(true);
            
            // Añadir campos al grid
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            
            grid.add(new Label("ID Ruta:"), 0, 0);
            grid.add(txtIdRuta, 1, 0);
            
            grid.add(new Label("Nombre:"), 0, 1);
            grid.add(txtNombre, 1, 1);
            
            grid.add(new Label("Destino:"), 0, 2);
            grid.add(txtDestino, 1, 2);
            
            grid.add(new Label("Paradas Intermedias:"), 0, 3);
            grid.add(txtParadas, 1, 3);
            
            dialog.getDialogPane().setContent(grid);
            
            // Convertir el resultado cuando se presiona Guardar
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validaciones
                        validarCampos(txtIdRuta.getText(), txtNombre.getText(), txtDestino.getText(), 
                                txtParadas.getText());
                        
                        Integer idRuta = Integer.parseInt(txtIdRuta.getText().trim());
                        String nombre = txtNombre.getText().trim();
                        String destino = txtDestino.getText().trim();
                        String paradas = txtParadas.getText().trim();
                        
                        return new Ruta(idRuta, nombre, destino, paradas);
                    } catch (RutaException | NumberFormatException ex) {
                        registrarExcepcion(ex);
                        mostrarAlerta("Error de validación", ex instanceof RutaException ? 
                                ex.getMessage() : "ID de ruta debe ser un número entero.");
                        return null;
                    }
                }
                return null;
            });
            
            // Mostrar diálogo y procesar resultado
            Optional<Ruta> resultado = dialog.showAndWait();
            
            resultado.ifPresent(rutaActualizada -> {
                try {
                    // Actualizar en la base de datos
                    rutaService.actualizarRuta(rutaActualizada);
                    
                    // Actualizar en la lista local
                    int indice = listaRutas.indexOf(ruta);
                    if (indice >= 0) {
                        listaRutas.set(indice, rutaActualizada);
                    }
                    
                    // Refrescar tabla
                    tablaRutas.refresh();
                    
                    // Mostrar mensaje de éxito
                    mostrarInfo("Éxito", "Ruta actualizada correctamente.");
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

    private void mostrarModalVerDetalle(Ruta ruta) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de la Ruta");
        alert.setHeaderText("Información de la ruta seleccionada");
        alert.setContentText(
            "ID Ruta: " + ruta.getId_ruta() + "\n" +
            "Nombre: " + ruta.getNombre() + "\n" +
            "Destino: " + ruta.getDestino() + "\n" +
            "Paradas Intermedias: " + ruta.getParadas_intermedias()
        );
        alert.showAndWait();
    }    
    
    private void validarCampos(String idRuta, String nombre, String destino, String paradas) throws RutaException {
        // Validar que los campos obligatorios no estén vacíos
        if (idRuta == null || idRuta.trim().isEmpty()) {
            throw new CampoVacioException("ID Ruta");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new CampoVacioException("Nombre");
        }
        if (destino == null || destino.trim().isEmpty()) {
            throw new CampoVacioException("Destino");
        }
        
        // Validar que ID sea un número
        try {
            Integer.parseInt(idRuta.trim());
        } catch (NumberFormatException e) {
            throw new FormatoIncorrectoException("ID Ruta", "Debe ser un número entero");
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