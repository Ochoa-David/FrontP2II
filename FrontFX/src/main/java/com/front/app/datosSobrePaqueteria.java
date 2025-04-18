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
import javafx.stage.Stage;
import java.util.regex.Pattern;
import java.util.Optional;

public class datosSobrePaqueteria extends Application {

    public static class PaqueteException extends Exception {
        public PaqueteException(String message) {
            super(message);
        }
    }

    public static class IdDuplicadoException extends PaqueteException {
        public IdDuplicadoException() {
            super("El ID de Paquete ya existe en el sistema.");
        }
    }

    public static class CampoVacioException extends PaqueteException {
        public CampoVacioException(String campo) {
            super("El campo " + campo + " no puede estar vacío.");
        }
    }

    public static class FormatoIncorrectoException extends PaqueteException {
        public FormatoIncorrectoException(String campo, String formato) {
            super("El formato del campo " + campo + " es incorrecto. " + formato);
        }
    }

    public static class NoSeleccionException extends PaqueteException {
        public NoSeleccionException() {
            super("No hay ningun Paquete seleccionado.");
        }
    }

    public static class BaseDatosException extends PaqueteException {
        public BaseDatosException(String operacion) {
            super("Error al " + operacion + " datos en la base de datos.");
        }
    }

    // Modelo de datos para Paquete
    public static class Paquete {
        private Integer idMercancia;
        private String descripcion;
        private Double peso;
        private Double valorEstimado;
        private Integer idCliente;
        private Integer idViaje;

        public Paquete(Integer idMercancia, String descripcion, Double peso, Double valorEstimado, Integer idCliente, Integer idViaje) {
            this.idMercancia = idMercancia;
            this.descripcion = descripcion;
            this.peso = peso;
            this.valorEstimado = valorEstimado;
            this.idCliente = idCliente;
            this.idViaje = idViaje;
        }

        public Integer getIdMercancia() { return idMercancia; }
        public String getDescripcion() { return descripcion; }
        public Double getPeso() { return peso; }
        public Double getValorEstimado() { return valorEstimado; }
        public Integer getIdCliente() { return idCliente; }
        public Integer getIdViaje() { return idViaje; }

        public void setIdMercancia(Integer idMercancia) { this.idMercancia = idMercancia; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public void setPeso(Double peso) { this.peso = peso; }
        public void setValorEstimado(Double valorEstimado) { this.valorEstimado = valorEstimado; }
        public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
        public void setIdViaje(Integer idViaje) { this.idViaje = idViaje; }
    }

    // Interfaz para gestionar operaciones con la API/Base de datos
    public interface PaqueteService {
        ObservableList<Paquete> obtenerPaquetes() throws BaseDatosException;
        void actualizarPaquete(Paquete Paquete) throws BaseDatosException;
    }

    // Implementación mock del servicio para pruebas (será reemplazada por implementación real)
    public static class PaqueteServiceMock implements PaqueteService {
        private ObservableList<Paquete> Paquetes = FXCollections.observableArrayList();
        public PaqueteServiceMock() {
            Paquetes = FXCollections.observableArrayList(
                new Paquete(1, "Electrodoméstico", 15.5, 2500.0, 101, 5001),
                new Paquete(2, "Libros", 5.0, 800.0, 102, 5002),
                new Paquete(3, "Ropa", 3.2, 1200.0, 103, 5003)
            );
        }


        @Override
        public ObservableList<Paquete> obtenerPaquetes() throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("obtener");
            }
            return Paquetes;
        }

        @Override
        public void actualizarPaquete(Paquete Paquete) throws BaseDatosException {
            // Simular posible error de conexión
            if (Math.random() < 0.01) { // 1% de probabilidad de error
                throw new BaseDatosException("actualizar");
            }

            // Buscar y actualizar
            for (int i = 0; i < Paquetes.size(); i++) {
                // Verifica que ambos ID de Paquete no sean null antes de comparar
                if (Paquetes.get(i).getIdMercancia() != null && Paquetes.get(i).getIdMercancia().equals(Paquete.getIdMercancia())) {
                    Paquetes.set(i, Paquete);
                    return;
                }
            }
        }
    }

    // Constantes para validación
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern TELEFONO_PATTERN =
        Pattern.compile("^[0-9()-]{6,50}$");

    // Lista observable para almacenar Paquetes
    private ObservableList<Paquete> listaPaquetes;

    // Tabla para mostrar Paquetes
    private TableView<Paquete> tablaPaquetes = new TableView<>();

    // Servicio para operaciones con Paquetes
    private PaqueteService PaqueteService;

    public Scene crearEscena(Stage stage) {
        try {
            PaqueteService = new PaqueteServiceMock(); // Usa tu servicio real si ya lo tienes implementado
            cargarDatos(); // Cargar los datos

            VBox root = new VBox(20);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);

            Label titulo = new Label("Paquetes Registrados");
            titulo.setId("titulo");

            configurarTabla();
            HBox botonesAccion = crearBotonesAccion();

            Button volverBtn = new Button("Volver al menú principal");
            volverBtn.setOnAction(e -> {
                Scene escenaPrincipal = paginaPrincipal.crearEscena(stage);
                App.cambiarEscena(escenaPrincipal, "Página Principal");
            });

            StackPane contenedorTabla = new StackPane(tablaPaquetes);

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
            PaqueteService = new PaqueteServiceMock();

            // Configurar el escenario
            primaryStage.setTitle("Gestión de Paquetes");
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
        tablaPaquetes.getColumns().clear();

        TableColumn<Paquete, Integer> colIdMercancia = new TableColumn<>("ID Mercancía");
        colIdMercancia.setCellValueFactory(new PropertyValueFactory<>("idMercancia"));

        TableColumn<Paquete, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Paquete, Double> colPeso = new TableColumn<>("Peso");
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        TableColumn<Paquete, Double> colValorEstimado = new TableColumn<>("Valor Estimado");
        colValorEstimado.setCellValueFactory(new PropertyValueFactory<>("valorEstimado"));

        TableColumn<Paquete, Integer> colIdCliente = new TableColumn<>("ID Cliente");
        colIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<Paquete, Integer> colIdViaje = new TableColumn<>("ID Viaje");
        colIdViaje.setCellValueFactory(new PropertyValueFactory<>("idViaje"));

        tablaPaquetes.getColumns().addAll(colIdMercancia, colDescripcion, colPeso, colValorEstimado, colIdCliente, colIdViaje);
        tablaPaquetes.setPrefHeight(350);
        tablaPaquetes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    private HBox crearBotonesAccion() {
        HBox botonesAccion = new HBox(10);

        try {
            botonesAccion.setAlignment(Pos.CENTER);

            Button btnAgregar = new Button("Agregar Paquete");
            Button btnEditar = new Button("Editar Paquete");

            btnAgregar.setOnAction(e -> mostrarModalAgregar());


            btnEditar.setOnAction(e -> {
                try {
                    Paquete PaqueteSeleccionada = tablaPaquetes.getSelectionModel().getSelectedItem();
                    if (PaqueteSeleccionada == null) {
                        throw new NoSeleccionException();
                    }
                    mostrarModalEditar(PaqueteSeleccionada);
                } catch (PaqueteException ex) {
                    registrarExcepcion(ex);
                    mostrarAlerta("Error", ex.getMessage());
                }
            });


            botonesAccion.getChildren().addAll(btnAgregar, btnEditar);

        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al crear los botones: " + ex.getMessage());
        }

        return botonesAccion;
    }

    private void mostrarModalAgregar() {
        Dialog<Paquete> dialog = new Dialog<>();
        dialog.setTitle("Agregar Mercancía");
        dialog.setHeaderText("Ingrese los datos de la nueva mercancía");

        ButtonType btnGuardar = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField txtIdMercancia = new TextField();
        TextField txtDescripcion = new TextField();
        TextField txtPeso = new TextField();
        TextField txtValorEstimado = new TextField();
        TextField txtIdCliente = new TextField();
        TextField txtIdViaje = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("ID Mercancía:"), 0, 0); grid.add(txtIdMercancia, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1); grid.add(txtDescripcion, 1, 1);
        grid.add(new Label("Peso:"), 0, 2); grid.add(txtPeso, 1, 2);
        grid.add(new Label("Valor Estimado:"), 0, 3); grid.add(txtValorEstimado, 1, 3);
        grid.add(new Label("ID Cliente:"), 0, 4); grid.add(txtIdCliente, 1, 4);
        grid.add(new Label("ID Viaje:"), 0, 5); grid.add(txtIdViaje, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    Integer idMercancia = Integer.parseInt(txtIdMercancia.getText());
                    String descripcion = txtDescripcion.getText();
                    Double peso = Double.parseDouble(txtPeso.getText());
                    Double valorEstimado = Double.parseDouble(txtValorEstimado.getText());
                    Integer idCliente = Integer.parseInt(txtIdCliente.getText());
                    Integer idViaje = Integer.parseInt(txtIdViaje.getText());

                    return new Paquete(idMercancia, descripcion, peso, valorEstimado, idCliente, idViaje);
                } catch (Exception e) {
                    mostrarAlerta("Error", "Por favor, verifique los datos ingresados.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(paquete -> {
            listaPaquetes.add(paquete);
            tablaPaquetes.refresh();
        });
    }

    private void cargarDatos() {
        try {
            // Obtener datos desde el servicio
            listaPaquetes = PaqueteService.obtenerPaquetes();

            // Asignar datos a la tabla
            tablaPaquetes.setItems(listaPaquetes);

        } catch (BaseDatosException ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de conexión", "No se pudieron cargar los datos: " + ex.getMessage());
        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error inesperado", "Error al cargar datos: " + ex.getMessage());
        }
    }

    private void validarCampos(String idViaje, String nombreRemitente, String idRemitente,
                                String nombreDestinatario, String idDestinatario, String peso) throws PaqueteException {

        // Validar ID de Viaje
        if (idViaje == null || idViaje.trim().isEmpty()) {
            throw new CampoVacioException("ID Viaje");
        }

        // Validar Nombre Remitente
        if (nombreRemitente == null || nombreRemitente.trim().isEmpty()) {
            throw new CampoVacioException("Nombre Remitente");
        }

        // Validar ID de Remitente
        if (idRemitente == null || idRemitente.trim().isEmpty()) {
            throw new CampoVacioException("ID Remitente");
        }

        // Validar Nombre Destinatario
        if (nombreDestinatario == null || nombreDestinatario.trim().isEmpty()) {
            throw new CampoVacioException("Nombre Destinatario");
        }

        // Validar ID de Destinatario
        if (idDestinatario == null || idDestinatario.trim().isEmpty()) {
            throw new CampoVacioException("ID Destinatario");
        }

        // Validar Peso
        if (peso == null || peso.trim().isEmpty()) {
            throw new CampoVacioException("Peso");
        }

        // Validar formato de ID Viaje, ID Remitente, ID Destinatario (por ejemplo, números enteros)
        try {
            Integer.parseInt(idViaje);
        } catch (NumberFormatException e) {
            throw new FormatoIncorrectoException("ID Viaje", "Debe ser un número entero.");
        }

        try {
            Integer.parseInt(idRemitente);
        } catch (NumberFormatException e) {
            throw new FormatoIncorrectoException("ID Remitente", "Debe ser un número entero.");
        }

        try {
            Integer.parseInt(idDestinatario);
        } catch (NumberFormatException e) {
            throw new FormatoIncorrectoException("ID Destinatario", "Debe ser un número entero.");
        }

        // Validar formato de Peso (por ejemplo, número decimal)
        try {
            Double.parseDouble(peso);
        } catch (NumberFormatException e) {
            throw new FormatoIncorrectoException("Peso", "Debe ser un número decimal.");
        }
    }

    private void mostrarModalEditar(Paquete paquete) {
        try {
            Dialog<Paquete> dialog = new Dialog<>();
            dialog.setTitle("Editar Mercancía");
            dialog.setHeaderText("Modifique los datos de la mercancía");

            ButtonType btnGuardar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

            // Crear campos con los valores actuales
            TextField txtIdMercancia = new TextField(paquete.idMercancia != null ? paquete.idMercancia.toString() : "");
            TextField txtDescripcion = new TextField(paquete.descripcion != null ? paquete.descripcion : "");
            TextField txtPeso = new TextField(paquete.peso != null ? paquete.peso.toString() : "");
            TextField txtValorEstimado = new TextField(paquete.valorEstimado != null ? paquete.valorEstimado.toString() : "");
            TextField txtIdCliente = new TextField(paquete.idCliente != null ? paquete.idCliente.toString() : "");
            TextField txtIdViaje = new TextField(paquete.idViaje != null ? paquete.idViaje.toString() : "");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("ID Mercancía:"), 0, 0);
            grid.add(txtIdMercancia, 1, 0);

            grid.add(new Label("Descripción:"), 0, 1);
            grid.add(txtDescripcion, 1, 1);

            grid.add(new Label("Peso:"), 0, 2);
            grid.add(txtPeso, 1, 2);

            grid.add(new Label("Valor Estimado:"), 0, 3);
            grid.add(txtValorEstimado, 1, 3);

            grid.add(new Label("ID Cliente:"), 0, 4);
            grid.add(txtIdCliente, 1, 4);

            grid.add(new Label("ID Viaje:"), 0, 5);
            grid.add(txtIdViaje, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        paquete.setIdMercancia(Integer.parseInt(txtIdMercancia.getText().trim()));
                        paquete.setDescripcion(txtDescripcion.getText().trim());
                        paquete.setPeso(Double.parseDouble(txtPeso.getText().trim()));
                        paquete.setValorEstimado(Double.parseDouble(txtValorEstimado.getText().trim()));
                        paquete.setIdCliente(Integer.parseInt(txtIdCliente.getText().trim())); // <-- esta es la línea problemática
                        paquete.setIdViaje(Integer.parseInt(txtIdViaje.getText().trim()));

                        return paquete;

                    } catch (NumberFormatException ex) {
                        mostrarAlerta("Error de validación", "Por favor, ingrese datos válidos.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Paquete> resultado = dialog.showAndWait();

            resultado.ifPresent(paqueteActualizado -> {
                tablaPaquetes.refresh();
                mostrarInfo("Éxito", "Mercancía actualizada correctamente.");
            });

        } catch (Exception ex) {
            mostrarAlerta("Error", "Error al mostrar el formulario de edición: " + ex.getMessage());
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
