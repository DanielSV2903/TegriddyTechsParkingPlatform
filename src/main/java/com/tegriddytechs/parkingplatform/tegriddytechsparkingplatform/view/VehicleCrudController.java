package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehicleCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfPlate;
    @FXML
    private ComboBox<VehicleType> cbVehicleType;

    // Added fields from improved FXML
    @FXML
    private TextField tfBrand;
    @FXML
    private TextField tfModel;
    @FXML
    private TextField tfColor;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<Vehicle> tableVehicles;
    @FXML
    private TableColumn<Vehicle, String> colPlate;
    @FXML
    private TableColumn<Vehicle, String> colBrand;
    @FXML
    private TableColumn<Vehicle, String> colModel;
    @FXML
    private TableColumn<Vehicle, String> colColor;
    @FXML
    private TableColumn<Vehicle, String> colType;
    @FXML
    private Label lblTotalRecords;
    @FXML private Button btnConfigureOwners;
    @FXML private Label lblOwnersSummary;
    private final ObservableList<Customer> selectedOwners = FXCollections.observableArrayList();
    private ObservableList<Vehicle> masterList = FXCollections.observableArrayList();
    private FilteredList<Vehicle> filteredList = new FilteredList<>(masterList, p -> true);

    public VehicleCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        try {
            loadData();
            fillCbox();
//            cbCustomer.getItems().setAll(mainMenuController.getAllCustomers());
            colPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));
            colBrand.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getVehicleType() != null ? c.getValue().getBrand() : ""));
            colModel.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicleType() != null ? data.getValue().getModel() : ""));
            colColor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicleType() != null ? data.getValue().getColor() : ""));
            colType.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getVehicleType() != null ? c.getValue().getVehicleType().getDescription()+" | "+c.getValue().getVehicleType().getSpaceType().name() : ""));

            tableVehicles.setItems(filteredList);

            if (tfSearch != null) {
                tfSearch.textProperty().addListener((obs, oldV, newV) -> {
                    String lower = newV == null ? "" : newV.toLowerCase();
                    filteredList.setPredicate(v -> {
                        if (lower.isBlank()) return true;
                        return (v.getPlate() != null && v.getPlate().toLowerCase().contains(lower))
                                || (v.getVehicleType() != null && v.getVehicleType().getDescription() != null && v.getVehicleType().getDescription().toLowerCase().contains(lower));
                    });
                    updateRecordCount();
                });
            }
            refreshOwnersSummary();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onConfigureOwners() {
        // Carga clientes registrados del sistema
        List<Customer> allCustomers = mainMenuController.getAllCustomers();
        allCustomers.sort(Comparator.comparingInt(Customer::getId));

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Configurar dueños del vehículo");

        // Tabla de clientes registrados
        TableView<Customer> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Customer, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> colName = new TableColumn<>("Nombre");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(colId, colName);
        ObservableList<Customer> available = FXCollections.observableArrayList(allCustomers);
        table.setItems(available);

        // Dueños seleccionados
        ListView<Customer> ownersList = new ListView<>(FXCollections.observableArrayList(selectedOwners));
        ownersList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String name = item.getName() != null ? item.getName() : "Cliente";
                    setText(name + " (" + item.getId() + ")");
                }
            }
        });

        Button btnAdd = new Button("Agregar →");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(e -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            if (containsCustomerById(ownersList.getItems(), selected.getId())) {
                return;
            }
            ownersList.getItems().add(selected);
        });

        Button btnRemove = new Button("← Quitar");
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        btnRemove.setOnAction(e -> {
            Customer selected = ownersList.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            ownersList.getItems().removeIf(c -> c != null && c.getId() == selected.getId());
        });

        Button btnNewCustomer = new Button("＋ Registrar nuevo cliente");
        btnNewCustomer.setMaxWidth(Double.MAX_VALUE);
        btnNewCustomer.setOnAction(e -> registerNewCustomerAndRefresh(available, table));

        Button btnOk = new Button("OK");
        btnOk.setDefaultButton(true);
        btnOk.setOnAction(e -> {
            selectedOwners.setAll(ownersList.getItems());
            refreshOwnersSummary();
            dialog.close();
        });

        Button btnCancel = new Button("Cancelar");
        btnCancel.setCancelButton(true);
        btnCancel.setOnAction(e -> dialog.close());

        VBox centerButtons = new VBox(10, btnAdd, btnRemove);
        centerButtons.setFillWidth(true);

        VBox left = new VBox(8, new Label("Clientes registrados"), table, btnNewCustomer);
        VBox right = new VBox(8, new Label("Dueños del vehículo"), ownersList);

        HBox content = new HBox(12, left, centerButtons, right);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        left.setPrefWidth(420);
        right.setPrefWidth(340);

        HBox footer = new HBox(10);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        footer.getChildren().addAll(spacer, btnCancel, btnOk);

        BorderPane root = new BorderPane();
        root.setCenter(content);
        root.setBottom(footer);
        BorderPane.setMargin(content, new javafx.geometry.Insets(12));
        BorderPane.setMargin(footer, new javafx.geometry.Insets(12));

        dialog.setScene(new Scene(root, 860, 520));
        dialog.showAndWait();
    }

    private void registerNewCustomerAndRefresh(ObservableList<Customer> available, TableView<Customer> table) {
        if (mainMenuController == null) {
            showAlert("Clientes", "No disponible", "No hay acceso al controlador de clientes desde esta vista.", Alert.AlertType.WARNING);
            return;
        }

        Integer id = promptInt("Clientes", "Registrar cliente", "ID");
        String name = promptText("Clientes", "Registrar cliente", "Nombre");
        Integer age = promptInt("Clientes", "Registrar cliente", "Edad");
        Boolean disability = promptBoolean("Clientes", "Registrar cliente", "Discapacidad");
        if (id == null || name == null || age == null || disability == null) return;

        Customer c = new Customer();
        c.setId(id);
        c.setName(name);
        c.setAge(age);
        c.setDisability(disability);

        OperationResult result = mainMenuController.createCustomer(c);
        if (result == null || !result.isSuccessfull()) {
            String msg = (result != null) ? result.getMessage() : "No se pudo registrar el cliente";
            showAlert("Clientes", "Error", msg, Alert.AlertType.ERROR);
            return;
        }

        // Recargar lista desde el sistema para quedar consistente
        List<Customer> refreshed = mainMenuController.getAllCustomers();
        refreshed.sort(Comparator.comparingInt(Customer::getId));
        available.setAll(refreshed);

        // Seleccionar el recién creado
        Optional<Customer> created = refreshed.stream().filter(x -> x != null && x.getId() == id).findFirst();
        created.ifPresent(x -> table.getSelectionModel().select(x));

        showAlert("Clientes", "OK", "Cliente registrado.", Alert.AlertType.INFORMATION);
    }

    private void refreshOwnersSummary() {
        if (lblOwnersSummary == null) return;

        if (selectedOwners.isEmpty()) {
            lblOwnersSummary.setText("Sin dueños seleccionados");
            return;
        }

        String summary = selectedOwners.stream()
                .filter(c -> c != null)
                .map(c -> {
                    String name = c.getName() != null ? c.getName() : "Cliente";
                    return name + " (" + c.getId() + ")";
                })
                .collect(Collectors.joining(", "));

        lblOwnersSummary.setText(summary);
    }

    private static boolean containsCustomerById(List<Customer> list, int id) {
        if (list == null) return false;
        for (Customer c : list) {
            if (c != null && c.getId() == id) return true;
        }
        return false;
    }

    // Reusa tus helpers existentes si ya los tienes; si no, deja estos o enlaza a CrudAlertHelper
    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    private String promptText(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return result.filter(value -> !value.trim().isEmpty()).orElse(null);
    }

    private Integer promptInt(String title, String header, String content) {
        String value = promptText(title, header, content);
        if (value == null) return null;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            showAlert(title, header, "Valor inválido: " + value, Alert.AlertType.WARNING);
            return null;
        }
    }

    private Boolean promptBoolean(String title, String header, String content) {
        ChoiceDialog<Boolean> dialog = new ChoiceDialog<>(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        Optional<Boolean> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void fillCbox() {
        cbVehicleType.setCellFactory(param -> new ListCell<VehicleType>() {
            @Override
            protected void updateItem(VehicleType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription()+" | "+item.getSpaceType().name());
                }
            }
        });
        cbVehicleType.setButtonCell(new ListCell<VehicleType>() {
            @Override
            protected void updateItem(VehicleType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription()+" | "+item.getSpaceType().name());
                }
            }
        });
        cbVehicleType.getItems().setAll(mainMenuController.getAllVehicleTypes());
    }

    private void loadData() {
        masterList.setAll(mainMenuController.getAllVehicles());
        updateRecordCount();
    }

    private void updateRecordCount() {
        if (lblTotalRecords != null) lblTotalRecords.setText(String.valueOf(filteredList.size()));
    }

    @FXML
    private void onCreate(ActionEvent actionEvent) {

        String plate = CrudFormUtils.readRequired(tfPlate, "Vehículo", "Placa");
        VehicleType vehicleSpaceType = (VehicleType) CrudFormUtils.readSelection(cbVehicleType, "Vehículo", "Tipo de vehículo");
        boolean disabledPermit =findIfIsDisability(selectedOwners);

        if (plate == null || vehicleSpaceType == null || selectedOwners == null) {
            return;
        }

        Rate rate = mainMenuController.getRateController().findBySpaceType(vehicleSpaceType.getSpaceType());

        if (rate == null) {
            CrudAlertHelper.showWarning(
                    "Vehículo",
                    "No existe una tarifa registrada para el tipo: " + vehicleSpaceType
            );
            return;
        }


        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setBrand(tfBrand.getText());
        vehicle.setModel(tfModel.getText());
        vehicle.setColor(tfColor.getText());
        vehicle.setVehicleType(vehicleSpaceType);
        vehicle.setVehicleStatus(VehicleStatus.EXITED);
        vehicle.setOwners(selectedOwners);
        vehicle.setTicket(null);
        vehicle.setDisabledPermit(disabledPermit);
        vehicle.setOwners(new ArrayList<>(selectedOwners));

        CrudAlertHelper.showResult(
                "Vehículo",
                mainMenuController.createVehicle(vehicle)
        );
        loadData();
    }

    private boolean findIfIsDisability(ObservableList<Customer> selectedOwners) {
        for (Customer customer : selectedOwners) {
            if (customer.isDisability())
                return true;
        }
        return false;
    }

    @FXML
    private void onUpdate(ActionEvent actionEvent) {
        String plate = CrudFormUtils.readRequired(tfPlate, "Vehiculos", "Placa");
        if (plate == null) {
            return;
        }
        Vehicle vehicle = mainMenuController.readVehicleByPlate(plate);
        vehicle.setPlate(plate);
        vehicle.setVehicleStatus(VehicleStatus.EXITED);
        vehicle.setVehicleType(cbVehicleType.getValue());
//        Customer owner = mainMenuController.getCustomerController().findCustomerById(Integer.parseInt(tfCustomerId.getText().trim()));
        vehicle.setOwners(selectedOwners);
        vehicle.setTicket(null);
        vehicle.setOwners(new ArrayList<>(selectedOwners));
        CrudAlertHelper.showResult("Vehiculos", mainMenuController.updateVehicle(vehicle));
        loadData();
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
        String plate = CrudFormUtils.readRequired(tfPlate, "Vehiculos", "Placa");
        if (plate == null) {
            return;
        }
        Vehicle vehicle = mainMenuController.readVehicleByPlate(plate);
        if (vehicle == null) {
            CrudAlertHelper.showWarning("Vehiculos", "Vehiculo no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Vehiculos", mainMenuController.deleteVehicle(vehicle));
        loadData();
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        if (actionEvent != null && actionEvent.getSource() instanceof Node node) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void onRefresh(ActionEvent actionEvent) {
        loadData();
    }

    @FXML
    public void onClear(ActionEvent actionEvent) {
        if (tfPlate != null) tfPlate.clear();
        if (tfBrand != null) tfBrand.clear();
        if (tfModel != null) tfModel.clear();
        if (tfColor != null) tfColor.clear();
        if (cbVehicleType != null) cbVehicleType.setValue(null);
    }

    @FXML
    public void selectVehicleOnMouseClicked(Event event) {
        fillFields();
    }

    private void fillFields() {
        Vehicle selected = tableVehicles.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tfPlate.setText(selected.getPlate());
            tfBrand.setText(selected.getBrand());
            tfModel.setText(selected.getModel());
            tfColor.setText(selected.getColor());
            if (selected.getVehicleType() != null) {
                cbVehicleType.setValue(selected.getVehicleType());
            }
            selectedOwners.setAll(selected.getOwners());
            refreshOwnersSummary();
        }
    }

    private String fillOwners(List<Customer> owners) {
        StringBuilder sb = new StringBuilder();
        for (Customer owner : owners) {
            sb.append(owner.getId()).append("|");
        }
        return sb.toString();
    }
}
