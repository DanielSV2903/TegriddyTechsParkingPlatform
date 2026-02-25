package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.DataManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class RegisterAndParkDialog {

    public record DialogResult(boolean confirmed, Vehicle vehicle, Customer customer) {}

    private TextField             tfPlate;
    private TextField             tfModel;
    private TextField             tfBrand;
    private TextField             tfColor;
    private ComboBox<VehicleType> cbVehicleType;

    private TextField tfCustomerId;
    private TextField tfCustomerName;
    private TextField tfCustomerAge;
    private CheckBox  chkDisability;

    private Label lblError;

    private final DataManager dataManager;
    private final String      prefilledPlate;
    private DialogResult      result = new DialogResult(false, null, null);

    public RegisterAndParkDialog(DataManager dataManager, String prefilledPlate) {
        this.dataManager    = dataManager;
        this.prefilledPlate = prefilledPlate == null ? "" : prefilledPlate.trim().toUpperCase();
    }

    public DialogResult show() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Registrar Cliente y Vehiculo");
        stage.setResizable(false);
        stage.setScene(new Scene(buildLayout(stage), 500, 620));
        stage.showAndWait();
        return result;
    }

    private VBox buildLayout(Stage stage) {
        VBox root = new VBox(14);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #f7fafc;");

        Label title = new Label("Vehiculo no registrado");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2d3748"));

        Label subtitle = new Label(
            "La placa " + prefilledPlate + " no esta en el sistema. " +
            "Complete los datos del cliente y del vehiculo para registrarlos " +
            "y continuar con el parqueo."
        );
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px;");

        root.getChildren().addAll(
            title, subtitle,
            sectionHeader("Datos del Vehiculo"), buildVehicleSection(),
            sectionHeader("Datos del Cliente"),  buildCustomerSection()
        );

        lblError = new Label();
        lblError.setStyle("-fx-text-fill: #c53030; -fx-font-size: 12px;");
        lblError.setWrapText(true);

        root.getChildren().addAll(lblError, buildButtons(stage));
        return root;
    }

    private Label sectionHeader(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        lbl.setStyle(
            "-fx-text-fill: #4a5568;" +
            "-fx-border-color: #cbd5e0;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-padding: 0 0 4 0;"
        );
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }

    private GridPane buildVehicleSection() {
        GridPane g = formGrid();

        tfPlate = new TextField(prefilledPlate);
        tfPlate.setPromptText("Ej: ABC123");

        cbVehicleType = new ComboBox<>();
        List<VehicleType> types = dataManager.getVehicleTypeController().getAllVehicleTypes();
        if (types != null) cbVehicleType.getItems().addAll(types);
        cbVehicleType.setMaxWidth(Double.MAX_VALUE);
        cbVehicleType.setPromptText("Seleccione tipo");
        cbVehicleType.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(VehicleType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleccione tipo" : item.getDescription());
            }
        });
        cbVehicleType.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(VehicleType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getDescription());
            }
        });
        if (!cbVehicleType.getItems().isEmpty()) cbVehicleType.getSelectionModel().selectFirst();

        tfBrand = field("Ej: Toyota");
        tfModel = field("Ej: Corolla");
        tfColor = field("Ej: Rojo");

        addRow(g, 0, "Placa *",   tfPlate);
        addRow(g, 1, "Tipo *",    cbVehicleType);
        addRow(g, 2, "Marca",     tfBrand);
        addRow(g, 3, "Modelo",    tfModel);
        addRow(g, 4, "Color",     tfColor);
        return g;
    }

    private GridPane buildCustomerSection() {
        GridPane g = formGrid();

        tfCustomerId   = field("Numero entero (ej: 112345678)");
        tfCustomerName = field("Nombre completo");
        tfCustomerAge  = field("Ej: 30");
        chkDisability  = new CheckBox();

        addRow(g, 0, "ID de cliente *", tfCustomerId);
        addRow(g, 1, "Nombre *",        tfCustomerName);
        addRow(g, 2, "Edad *",          tfCustomerAge);
        addRow(g, 3, "Discapacidad",    chkDisability);
        return g;
    }

    private HBox buildButtons(Stage stage) {
        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefWidth(120);
        btnCancel.setStyle(
            "-fx-background-color: #e2e8f0; -fx-text-fill: #4a5568;" +
            "-fx-font-weight: bold; -fx-cursor: hand;"
        );
        btnCancel.setOnAction(e -> stage.close());

        Button btnConfirm = new Button("Registrar y Parquear");
        btnConfirm.setPrefWidth(190);
        btnConfirm.setStyle(
            "-fx-background-color: #3182ce; -fx-text-fill: white;" +
            "-fx-font-weight: bold; -fx-cursor: hand;"
        );
        btnConfirm.setOnAction(e -> onConfirm(stage));

        HBox box = new HBox(12, btnCancel, btnConfirm);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    private void onConfirm(Stage stage) {
        lblError.setText("");

        String plate = tfPlate.getText() == null ? "" : tfPlate.getText().trim().toUpperCase();
        if (plate.isBlank()) { lblError.setText("La placa es obligatoria."); return; }

        VehicleType vtype = cbVehicleType.getValue();
        if (vtype == null) { lblError.setText("Seleccione el tipo de vehiculo."); return; }

        if (dataManager.getVehicleController().findVehicleByPlate(plate) != null) {
            lblError.setText("Ya existe un vehiculo registrado con la placa " + plate + ".");
            return;
        }

        String idText  = tfCustomerId.getText()  == null ? "" : tfCustomerId.getText().trim();
        String name    = tfCustomerName.getText() == null ? "" : tfCustomerName.getText().trim();
        String ageText = tfCustomerAge.getText()  == null ? "" : tfCustomerAge.getText().trim();

        if (idText.isBlank())  { lblError.setText("El ID del cliente es obligatorio."); return; }
        if (name.isBlank())    { lblError.setText("El nombre del cliente es obligatorio."); return; }
        if (ageText.isBlank()) { lblError.setText("La edad del cliente es obligatoria."); return; }

        int customerId;
        try { customerId = Integer.parseInt(idText); }
        catch (NumberFormatException ex) { lblError.setText("El ID debe ser un numero entero."); return; }

        int age;
        try { age = Integer.parseInt(ageText); }
        catch (NumberFormatException ex) { lblError.setText("La edad debe ser un numero entero."); return; }

        Customer customer = dataManager.getCustomerController().findCustomerById(customerId);

        if (customer != null) {
            String vehicleInfo = "";
            if (customer.getVehicles() != null && !customer.getVehicles().isEmpty()) {
                vehicleInfo = "\nVehiculos registrados: " + customer.getVehicles().size();
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cliente ya registrado");
            alert.setHeaderText("Ya existe un cliente con ese ID");
            alert.setContentText(
                "ID: " + customer.getId() + "\n" +
                "Nombre: " + customer.getName() + "\n" +
                "Edad: " + customer.getAge() + "\n" +
                "Discapacidad: " + (customer.isDisability() ? "Si" : "No") +
                vehicleInfo + "\n\n" +
                "Desea asociar el nuevo vehiculo a este cliente?"
            );

            ButtonType btnUsar     = new ButtonType("Usar este cliente", ButtonBar.ButtonData.YES);
            ButtonType btnCancelar = new ButtonType("Cancelar",          ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnUsar, btnCancelar);

            alert.showAndWait().ifPresent(response -> {
                if (response != btnUsar) {
                    lblError.setText("Ingrese un ID de cliente diferente.");
                    tfCustomerId.clear();
                    tfCustomerId.requestFocus();
                }
            });

            if (!lblError.getText().isBlank()) return;

        } else {
            customer = new Customer(customerId, name, chkDisability.isSelected(), age, null);
            try {
                OperationResult r = dataManager.getCustomerController().registerCustomer(customer);
                if (!r.isSuccessfull()) {
                    lblError.setText("Error al registrar cliente: " + r.getMessage());
                    return;
                }
            } catch (IOException ex) {
                lblError.setText("Error al guardar cliente: " + ex.getMessage());
                return;
            }
        }

        Vehicle vehicle = buildVehicle(plate, vtype, customer);
        vehicle.setVehicleStatus(VehicleStatus.EXITED);

        try {
            OperationResult r = dataManager.getVehicleController().registerVehicle(vehicle);
            if (!r.isSuccessfull()) {
                lblError.setText("Error al registrar vehiculo: " + r.getMessage());
                return;
            }
        } catch (IOException ex) {
            lblError.setText("Error al guardar vehiculo: " + ex.getMessage());
            return;
        }

        try { dataManager.connectAll(); } catch (Exception ignored) {}

        result = new DialogResult(true, vehicle, customer);
        stage.close();
    }

    private Vehicle buildVehicle(String plate, VehicleType vtype, Customer owner) {
        String model = tfModel.getText() == null ? "" : tfModel.getText().trim();
        String brand = tfBrand.getText() == null ? "" : tfBrand.getText().trim();
        String color = tfColor.getText() == null ? "" : tfColor.getText().trim();

        if (!model.isBlank() || !brand.isBlank() || !color.isBlank()) {
            return new Vehicle(plate, model, color, brand, vtype, owner);
        } else {
            return new Vehicle(plate, vtype, VehicleStatus.EXITED, owner, null);
        }
    }

    private static GridPane formGrid() {
        GridPane g = new GridPane();
        g.setHgap(12);
        g.setVgap(8);
        ColumnConstraints labelCol = new ColumnConstraints(140);
        ColumnConstraints inputCol = new ColumnConstraints();
        inputCol.setHgrow(Priority.ALWAYS);
        g.getColumnConstraints().addAll(labelCol, inputCol);
        return g;
    }

    private static void addRow(GridPane g, int row, String labelText, javafx.scene.Node input) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #4a5568;");
        g.add(lbl, 0, row);
        g.add(input, 1, row);
    }

    private static TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        return tf;
    }
}
