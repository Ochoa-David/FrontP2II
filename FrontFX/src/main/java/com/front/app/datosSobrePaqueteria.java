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
        private Integer idViaje;
        private String nombreRemitente;
        private Integer idRemitente;
        private String nombreDestinatario;
        private Integer idDestinatario;
        private Double peso;
        private Integer id_Paquete;
        private String nombre;
        private String telefono;
        private String correo;
        private String direccion;
        private Integer id_tipo_Paquete;

        public Integer getId_Paquete() { return id_Paquete; }
        public String getNombre() { return nombre; }
        public String getTelefono() { return telefono; }
        public String getCorreo() { return correo; }
        public String getDireccion() { return direccion; }
        public Integer getId_tipo_Paquete() { return id_tipo_Paquete; }

        public Paquete(Integer idViaje, String nombreRemitente, Integer idRemitente,
            String nombreDestinatario, Integer idDestinatario, Double peso) {
            this.idViaje = idViaje;
            this.nombreRemitente = nombreRemitente;
            this.idRemitente = idRemitente;
            this.nombreDestinatario = nombreDestinatario;
            this.idDestinatario = idDestinatario;
            this.peso = peso;
        }
        public Paquete(Integer id_Paquete, String nombre, String telefono, String correo, String direccion, Integer id_tipo_Paquete) {
            this.id_Paquete = id_Paquete;
            this.nombre = nombre;
            this.telefono = telefono;
            this.correo = correo;
            this.direccion = direccion;
            this.id_tipo_Paquete = id_tipo_Paquete;
        }


        public Integer getIdViaje() { return idViaje; }
        public String getNombreRemitente() { return nombreRemitente; }
        public Integer getIdRemitente() { return idRemitente; }
        public String getNombreDestinatario() { return nombreDestinatario; }
        public Integer getIdDestinatario() { return idDestinatario; }
        public Double getPeso() { return peso; }
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
                new Paquete(1001, "Carlos Pérez", 201, "Laura Gómez", 301, 2.5),
                new Paquete(1002, "Ana Torres", 202, "Miguel Díaz", 302, 5.75),
                new Paquete(1003, "José Ramírez", 203, "Sofía López", 303, 3.9)
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
                if (Paquetes.get(i).getId_Paquete() != null && Paquetes.get(i).getId_Paquete().equals(Paquete.getId_Paquete())) {
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
        try {
            tablaPaquetes.getColumns().clear(); // Limpiar columnas existentes

            TableColumn<Paquete, Integer> colIdViaje = new TableColumn<>("ID Viaje");
            colIdViaje.setCellValueFactory(new PropertyValueFactory<>("idViaje"));
            colIdViaje.setPrefWidth(80);

            TableColumn<Paquete, String> colNombreRemitente = new TableColumn<>("Nombre Remitente");
            colNombreRemitente.setCellValueFactory(new PropertyValueFactory<>("nombreRemitente"));
            colNombreRemitente.setPrefWidth(150);

            TableColumn<Paquete, Integer> colIdRemitente = new TableColumn<>("ID Remitente");
            colIdRemitente.setCellValueFactory(new PropertyValueFactory<>("idRemitente"));
            colIdRemitente.setPrefWidth(100);

            TableColumn<Paquete, String> colNombreDestinatario = new TableColumn<>("Nombre Destinatario");
            colNombreDestinatario.setCellValueFactory(new PropertyValueFactory<>("nombreDestinatario"));
            colNombreDestinatario.setPrefWidth(150);

            TableColumn<Paquete, Integer> colIdDestinatario = new TableColumn<>("ID Destinatario");
            colIdDestinatario.setCellValueFactory(new PropertyValueFactory<>("idDestinatario"));
            colIdDestinatario.setPrefWidth(100);

            TableColumn<Paquete, Double> colPeso = new TableColumn<>("Peso");
            colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
            colPeso.setPrefWidth(80);

            tablaPaquetes.getColumns().addAll(colIdViaje, colNombreRemitente, colIdRemitente,
                    colNombreDestinatario, colIdDestinatario, colPeso);

            tablaPaquetes.setPrefHeight(350);
            tablaPaquetes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception ex) {
            registrarExcepcion(ex);
            mostrarAlerta("Error de configuración", "Error al configurar la tabla: " + ex.getMessage());
        }
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
        try {
            Dialog<Paquete> dialog = new Dialog<>();
            dialog.setTitle("Agregar Nuevo Paquete");
            dialog.setHeaderText("Ingrese los datos del nuevo paquete");

            ButtonType btnGuardar = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

            // Campos vacíos
            TextField txtIdViaje = new TextField();
            TextField txtNombreRemitente = new TextField();
            TextField txtIdRemitente = new TextField();
            TextField txtNombreDestinatario = new TextField();
            TextField txtIdDestinatario = new TextField();
            TextField txtPeso = new TextField();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("ID Viaje:"), 0, 0);
            grid.add(txtIdViaje, 1, 0);
            grid.add(new Label("Nombre Remitente:"), 0, 1);
            grid.add(txtNombreRemitente, 1, 1);
            grid.add(new Label("ID Remitente:"), 0, 2);
            grid.add(txtIdRemitente, 1, 2);
            grid.add(new Label("Nombre Destinatario:"), 0, 3);
            grid.add(txtNombreDestinatario, 1, 3);
            grid.add(new Label("ID Destinatario:"), 0, 4);
            grid.add(txtIdDestinatario, 1, 4);
            grid.add(new Label("Peso:"), 0, 5);
            grid.add(txtPeso, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        validarCampos(txtIdViaje.getText(), txtNombreRemitente.getText(), txtIdRemitente.getText(),
                                txtNombreDestinatario.getText(), txtIdDestinatario.getText(), txtPeso.getText());

                        Integer idViaje = Integer.parseInt(txtIdViaje.getText().trim());
                        String nombreRemitente = txtNombreRemitente.getText().trim();
                        Integer idRemitente = Integer.parseInt(txtIdRemitente.getText().trim());
                        String nombreDestinatario = txtNombreDestinatario.getText().trim();
                        Integer idDestinatario = Integer.parseInt(txtIdDestinatario.getText().trim());
                        Double peso = Double.parseDouble(txtPeso.getText().trim());

                        return new Paquete(idViaje, nombreRemitente, idRemitente, nombreDestinatario, idDestinatario, peso);
                    } catch (PaqueteException | NumberFormatException ex) {
                        mostrarAlerta("Error de validación", ex.getMessage());
                        return null;
                    }
                }
                return null;
            });

            Optional<Paquete> resultado = dialog.showAndWait();

            resultado.ifPresent(paqueteNuevo -> {
                listaPaquetes.add(paqueteNuevo);
                tablaPaquetes.refresh();
                mostrarInfo("Éxito", "Paquete agregado correctamente.");
            });

        } catch (Exception ex) {
            mostrarAlerta("Error", "Error al mostrar el formulario: " + ex.getMessage());
        }
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

    private void mostrarModalEditar(Paquete Paquete) {
        try {
            // Crear un diálogo modal
            Dialog<Paquete> dialog = new Dialog<>();
            dialog.setTitle("Editar Paquete");
            dialog.setHeaderText("Modifique los datos del Paquete");

            // Configurar botones
            ButtonType btnGuardar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

            // Crear campos de formulario y poblarlos con datos existentes
            TextField txtIdViaje = new TextField(Paquete.getIdViaje() != null ? Paquete.getIdViaje().toString() : "");
            TextField txtNombreRemitente = new TextField(Paquete.getNombreRemitente() != null ? Paquete.getNombreRemitente() : "");
            TextField txtIdRemitente = new TextField(Paquete.getIdRemitente() != null ? Paquete.getIdRemitente().toString() : "");
            TextField txtNombreDestinatario = new TextField(Paquete.getNombreDestinatario() != null ? Paquete.getNombreDestinatario() : "");
            TextField txtIdDestinatario = new TextField(Paquete.getIdDestinatario() != null ? Paquete.getIdDestinatario().toString() : "");
            TextField txtPeso = new TextField(Paquete.getPeso() != null ? Paquete.getPeso().toString() : "");

            // Añadir campos al grid
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("ID Viaje:"), 0, 0);
            grid.add(txtIdViaje, 1, 0);

            grid.add(new Label("Nombre Remitente:"), 0, 1);
            grid.add(txtNombreRemitente, 1, 1);

            grid.add(new Label("ID Remitente:"), 0, 2);
            grid.add(txtIdRemitente, 1, 2);

            grid.add(new Label("Nombre Destinatario:"), 0, 3);
            grid.add(txtNombreDestinatario, 1, 3);

            grid.add(new Label("ID Destinatario:"), 0, 4);
            grid.add(txtIdDestinatario, 1, 4);

            grid.add(new Label("Peso:"), 0, 5);
            grid.add(txtPeso, 1, 5);

            dialog.getDialogPane().setContent(grid);

            // Convertir el resultado cuando se presiona Guardar
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnGuardar) {
                    try {
                        // Validaciones
                        validarCampos(txtIdViaje.getText(), txtNombreRemitente.getText(), txtIdRemitente.getText(),
                                txtNombreDestinatario.getText(), txtIdDestinatario.getText(), txtPeso.getText());

                        Integer idViaje = Integer.parseInt(txtIdViaje.getText().trim());
                        String nombreRemitente = txtNombreRemitente.getText().trim();
                        Integer idRemitente = Integer.parseInt(txtIdRemitente.getText().trim());
                        String nombreDestinatario = txtNombreDestinatario.getText().trim();
                        Integer idDestinatario = Integer.parseInt(txtIdDestinatario.getText().trim());
                        Double peso = Double.parseDouble(txtPeso.getText().trim());

                        return new Paquete(idViaje, nombreRemitente, idRemitente, nombreDestinatario, idDestinatario, peso);
                    } catch (PaqueteException | NumberFormatException ex) {
                        mostrarAlerta("Error de validación", ex.getMessage());
                        return null;
                    }
                }
                return null;
            });

            // Mostrar diálogo y procesar resultado
            Optional<Paquete> resultado = dialog.showAndWait();

            resultado.ifPresent(PaqueteActualizada -> {
                try {
                    // Actualizar en la base de datos
                    PaqueteService.actualizarPaquete(PaqueteActualizada);

                    // Actualizar en la lista local
                    int indice = listaPaquetes.indexOf(Paquete);
                    if (indice >= 0) {
                        listaPaquetes.set(indice, PaqueteActualizada);
                    }

                    // Refrescar tabla
                    tablaPaquetes.refresh();

                    // Mostrar mensaje de éxito
                    mostrarInfo("Éxito", "Paquete actualizado correctamente.");
                } catch (BaseDatosException ex) {
                    mostrarAlerta("Error de base de datos", ex.getMessage());
                }
            });

        } catch (Exception ex) {
            mostrarAlerta("Error", "Error al mostrar el formulario: " + ex.getMessage());
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
