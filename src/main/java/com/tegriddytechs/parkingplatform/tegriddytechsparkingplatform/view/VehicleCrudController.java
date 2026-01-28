package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.CustomerData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.RateData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class VehicleCrudController {

    private final MainMenuController mainMenuController;
    private CustomerData customerData = new CustomerData();

    @FXML
    private TextField tfPlate;
    @FXML
    private ComboBox<VehicleStatus> cbStatus;
    @FXML
    private CheckBox cbDisabled;
    @FXML
    private ComboBox cbCustomer;
    @FXML
    private ComboBox cbVehicleType;

    public VehicleCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        cbStatus.getItems().setAll(VehicleStatus.values());
        cbVehicleType.getItems().setAll(SpaceType.values());
        cbCustomer.getItems().setAll(mainMenuController.getAllCustomers());
    }

    @FXML
    private void onCreate() {

        String plate = CrudFormUtils.readRequired(tfPlate, "Vehículo", "Placa");
        SpaceType spaceType = (SpaceType) CrudFormUtils.readSelection(cbVehicleType, "Vehículo", "Tipo de vehículo");
        Customer customer = (Customer) CrudFormUtils.readSelection(cbCustomer, "Vehículo", "Propietario");

        boolean disabledPermit = cbDisabled.isSelected();

        if (plate == null || spaceType == null || customer == null) {
            return;
        }

        RateData rateData = new RateData();
        Rate rate = rateData.findBySpaceType(spaceType);

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
        vehicle.setVehicleType(vehicleType);
        vehicle.setVehicleStatus(VehicleStatus.EXITED);
        vehicle.setOwner(customer);
        vehicle.setTicket(null);
        vehicle.setDisabledPermit(disabledPermit);

        CrudAlertHelper.showResult(
                "Vehículo",
                mainMenuController.createVehicle(vehicle)
        );
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



    @FXML
    private void onRead() {
        String plate = CrudFormUtils.readRequired(tfPlate, "Vehiculos", "Placa");
        if (plate == null) {
            return;
        }
        Vehicle vehicle = mainMenuController.readVehicleByPlate(plate);
        CrudAlertHelper.showEntity("Vehiculos", vehicle);
    }

    @FXML
    private void onUpdate() {
        String plate = CrudFormUtils.readRequired(tfPlate, "Vehiculos", "Placa");
        VehicleStatus status = CrudFormUtils.readSelection(cbStatus, "Vehiculos", "Estado");
        if (plate == null || status == null) {
            return;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setVehicleStatus(status);
        vehicle.setVehicleType(defaultType());
        CrudAlertHelper.showResult("Vehiculos", mainMenuController.updateVehicle(vehicle));
    }

    @FXML
    private void onDelete() {
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
    }

    private VehicleType defaultType() {
        return new VehicleType(1, "Default", (byte) 4, 0.0, SpaceType.CAR);
    }
}
