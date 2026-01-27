package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Vehicle;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleStatus;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class VehicleCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfPlate;
    @FXML
    private ComboBox<VehicleStatus> cbStatus;

    public VehicleCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        cbStatus.getItems().setAll(VehicleStatus.values());
    }

    @FXML
    private void onCreate() {
        String plate = CrudFormUtils.readRequired(tfPlate, "Vehiculos", "Placa");
        VehicleStatus status = CrudFormUtils.readSelection(cbStatus, "Vehiculos", "Estado");
        if (plate == null || status == null) {
            return;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setVehicleStatus(status);
        vehicle.setVehicleType(defaultType());
        CrudAlertHelper.showResult("Vehiculos", mainMenuController.createVehicle(vehicle));
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
