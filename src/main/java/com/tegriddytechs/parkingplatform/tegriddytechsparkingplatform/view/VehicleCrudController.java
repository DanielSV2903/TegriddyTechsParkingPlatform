package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.RateData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class VehicleCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfPlate;
    @FXML
    private ComboBox<SpaceType> cbVehicleType;

    // Added fields from improved FXML
    @FXML
    private TextField tfBrand;
    @FXML
    private TextField tfModel;
    @FXML
    private TextField tfColor;
    @FXML
    private TextField tfCustomerId;
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

    private ObservableList<Vehicle> masterList = FXCollections.observableArrayList();
    private FilteredList<Vehicle> filteredList = new FilteredList<>(masterList, p -> true);

    public VehicleCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        try {
            loadData();
            cbVehicleType.getItems().setAll(SpaceType.values());
//            cbCustomer.getItems().setAll(mainMenuController.getAllCustomers());
            colPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));
            colBrand.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getVehicleType() != null ? c.getValue().getBrand() : ""));
            colModel.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicleType() != null ? data.getValue().getModel() : ""));
            colColor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicleType() != null ? data.getValue().getColor() : ""));
            colType.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getVehicleType() != null ? c.getValue().getVehicleType().getSpaceType().getType() : ""));

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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        SpaceType spaceType = (SpaceType) CrudFormUtils.readSelection(cbVehicleType, "Vehículo", "Tipo de vehículo");
        Customer customer = mainMenuController.getCustomerController().findCustomerById(Integer.parseInt(tfCustomerId.getText().trim()));
        boolean disabledPermit =customer.isDisability();

        if (plate == null || spaceType == null || customer == null) {
            return;
        }

         Rate rate = mainMenuController.getRateController().findBySpaceType(spaceType);

        if (rate == null) {
            CrudAlertHelper.showWarning(
                    "Vehículo",
                    "No existe una tarifa registrada para el tipo: " + spaceType
            );
            return;
        }

        VehicleType vehicleType = new VehicleType(
                spaceType.ordinal(),
                spaceType.name(),
                defaultTyres(spaceType),
                rate.getFee(),
                spaceType
        );

        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setBrand(tfBrand.getText());
        vehicle.setModel(tfModel.getText());
        vehicle.setColor(tfColor.getText());
        vehicle.setVehicleType(vehicleType);
        vehicle.setVehicleStatus(VehicleStatus.EXITED);
        vehicle.setOwner(customer);
        vehicle.setTicket(null);
        vehicle.setDisabledPermit(disabledPermit);

        CrudAlertHelper.showResult(
                "Vehículo",
                mainMenuController.createVehicle(vehicle)
        );
        loadData();
    }

    private byte defaultTyres(SpaceType spaceType) {
        switch (spaceType) {
            case MOTORCYCLE:
            case BICYCLE:
                return 2;
            case CAR:
                return 4;
            case HEAVY:
                return 6;
            default:
                return 4;
        }
    }



    @Deprecated
    private void onRead(ActionEvent actionEvent) {
        String plate = CrudFormUtils.readRequired(tfPlate, "Vehiculos", "Placa");
        if (plate == null) {
            return;
        }
        Vehicle vehicle = mainMenuController.readVehicleByPlate(plate);
        CrudAlertHelper.showEntity("Vehiculos", vehicle);
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
        vehicle.setVehicleType(defaultType());
        Customer owner = mainMenuController.getCustomerController().findCustomerById(Integer.parseInt(tfCustomerId.getText().trim()));
        vehicle.setOwner(owner);
        vehicle.setTicket(null);
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
        if (tfCustomerId != null) tfCustomerId.clear();
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
            if (selected.getOwner() != null) {
                tfCustomerId.setText(String.valueOf(selected.getOwner().getId()));
            }
            if (selected.getVehicleType() != null) {
                cbVehicleType.setValue(selected.getVehicleType().getSpaceType());
            }
        }
    }

    private VehicleType defaultType() {
        return new VehicleType(1, "Default", (byte) 4, 0.0, SpaceType.CAR);
    }
}
